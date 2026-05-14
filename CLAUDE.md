# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

**GranaWiz** is a Kotlin desktop application for personal finance management, targeting macOS, Windows, and Linux. UI is in Portuguese (pt-BR). Currently in alpha.

## Build Commands

All Gradle commands must be run from the `app/` directory (where `gradlew` lives):

```bash
cd app

./gradlew run                              # Run in development mode
./gradlew packageDistributionForCurrentOS  # Build installer for current OS
./gradlew packageDmg                       # macOS .dmg
./gradlew packageMsi                       # Windows .msi
./gradlew packageDeb                       # Linux .deb
./gradlew packageReleaseDmg                # macOS release build
./gradlew clean                            # Clean build artifacts
./gradlew run --debug-jvm                  # Run with debugger on port 5005
```

There are currently no automated tests. The `test/kotlin/` directory is empty.

## Architecture

Hexagonal (Ports & Adapters) architecture with five main layers inside `app/src/main/kotlin/`:

```
domain/         → Entities (JPA), repository contracts (IXxxRepository), enums, structs
application/    → Business logic: Handler classes coordinate UseCases per feature
infrastructure/ → Hibernate/SQLite config, repository implementations, Flyway migrations
view/           → Compose Desktop UI: screens, dialogs, forms (modules/, shared/, theme/)
viewModel/      → Kotlin Flow-based state management for each screen
utils/          → DateTimeUtils, CurrencyUtils, PainterUtils, IconPaths
```

**Data flow:** `View` → `ViewModel` → `Handler` → `UseCase` → `Repository (infrastructure)` → SQLite

### Key Patterns

- **Handlers** (`application/*/Handler`) act as facades, coordinating multiple use cases for a feature area.
- **Use cases** (`application/*/*UseCase`) are single-responsibility business operations (61 total). Note: many are thin pass-throughs to repositories and could be inlined; new logic should only become a use case when it carries actual business rules.
- **Repository contracts** in `domain/contracts/` define interfaces (`IAccountRepository`, etc.) implemented by `infrastructure/repository/`. Repositories are currently instantiated as constructor defaults inside each use case (`= XxxRepository()`), which blocks DI and testing — keep this in mind before adding new use cases.
- **Repositories** use Hibernate Criteria API with explicit `Hibernate.initialize()` calls to handle lazy loading. This produces N+1 patterns in `TransactionRepository`; prefer `JOIN FETCH` (JPQL) for new read paths.
- **Database** is SQLite at `~/.granawiz/database/granawiz.db`, managed by Flyway migrations in `src/main/resources/db/migration/`. Migrations run automatically on startup via `DatabaseConfig.runMigrations()`. **Caveat:** `V3__insert-fake-datas.sql` ships demo data — do not treat it as production-ready. SQLite `PRAGMA foreign_keys` is **not** enabled, so declared FKs are not enforced at runtime.
- **Account types** use JPA single-table inheritance with discriminator column: `BankAccount` → `CheckingAccount`, `SavingsAccount`, `CreditCardAccount`.
- **IFilterable** is a marker interface implemented by entities that support filtering (party, account, category, etc.).
- **Navigation** uses a `Screen` sealed class (`view/modules/Screen.kt`) routed by `MainContent.kt`. Defined screens: `Dashboard`, `Schedules`, `Categories`, `Tags`, `Receivers`, `Payers`, `Transactions(account, showAddButton)`, `NewTransactionForm(transactions)`, `ImportStatement(account)`.
- **UserPreferences** (`view/modules/UserPreferences.kt`) is an app-wide singleton for theme state.
- **CustomTitleBar** (`view/modules/CustomTitleBar.kt`) — custom window title bar component.
- **SplashWindow** (`view/modules/SplashWindow.kt`) — splash screen shown during app initialization.
- **Shared components** (`view/shared/`) — 28 reusable Compose components (text fields, dropdowns, dialogs, list items, date pickers, etc.).
- **Theme** (`view/theme/`) — `Theme.kt` (light/dark color schemes) and `Type.kt` (typography).
- **i18n** — user-facing strings live in `composeResources/values/strings.xml` (default, English) and `composeResources/values-pt-rBR/strings.xml`. Never hardcode Portuguese strings in Kotlin source; access via `stringResource(Res.string.*)`.
- **ViewModels** currently expose `MutableStateFlow` publicly and most lack `try/catch`. New ViewModels should follow the `private val _state = MutableStateFlow(...)` + `val state = _state.asStateFlow()` pattern and wrap repository calls with `runCatching`.
- **Layering rule** — `domain/` must not import from `infrastructure/` or `application/`. (There are existing violations in `domain/entity/Transaction.kt` and `Schedule.kt` importing `infrastructure.config.LocalDateTimeConverter` — do not replicate this pattern.)

### Feature Modules

| Feature | Handler | ViewModel | Screens / Forms |
|---------|---------|-----------|-----------------|
| Dashboard | `DashboardHandler` | `DashboardViewModel` | `DashboardScreen` (9 metric cards) |
| Transactions | `TransactionHandler` | `TransactionViewModel`, `TransactionFormViewModel` | `TransactionsScreen`, `TransactionForm` |
| Schedules | `ScheduleHandler` | `ScheduleViewModel` | `ScheduleScreen` |
| Accounts | `AccountHandler` | `AccountFormViewModel`, `SidebarViewModel` | `AccountForm`, sidebar |
| Groups | `GroupHandler` | `SidebarViewModel` | `GroupForm`, sidebar |
| Categories | `CategoryHandler` | `CategoryViewModel` | `CategoriesScreen` |
| Tags | `TagHandler` | `TagViewModel` | `TagsScreen` |
| Parties | `PartyHandler` | `PartyViewModel` | `PayersScreen`, `ReceiversScreen` |
| Import Statement | `ImportHandler` | `ImportStatementViewModel` | `ImportStatementScreen` (CSV + OFX) |
| Settings | `UserPreferenceHandler` | `SettingsViewModel` | `SettingsScreen` |

### Domain Structs

`domain/structs/` contains composite data classes used across layers:
- `DashboardSummary` — aggregates 9 metrics (NetWorthSnapshot, CashFlow, SpendingPace, etc.)
- `DashboardPeriod` — period selector state
- `FilterEntry` — generic filter representation
- `PageAddress` — navigation address

### Tech Stack

| Layer | Technology |
|-------|-----------|
| UI | Jetpack Compose Desktop 1.10.3 |
| Language | Kotlin 2.3.21 |
| JVM | Toolchain 25 (note: not LTS — Java 21 is the LTS target to consider for release) |
| ORM | Hibernate 6.4.4 + Jakarta JPA 3.1 |
| Database | SQLite 3.48 (xerial sqlite-jdbc, no encryption) |
| Migrations | Flyway 11.1.1 |
| Logging | Logback 1.5.16 (no `logback.xml` shipped — uses defaults) |
| Icons | Phosphor Icons 1.0.0 (Bold & Regular variants) |
| Excel/CSV Export | Apache POI OOXML 5.4.0 |
| OFX Import | ofx4j 1.39 |
| Packaging | JPackage via Compose Multiplatform plugin (no signing/notarization configured) |

## Entry Point

`Main.kt` — sets up the Compose Desktop `application` window. `AppConfig.kt` defines paths (e.g., DB location). On startup, `DatabaseConfig.runMigrations()` runs Flyway before the UI is shown.

## Release Readiness

A full production-readiness audit lives in `report.md` at the repo root. Before recommending a `v1.0` release, check that file for outstanding blockers (fake-data migration, missing tests, missing CI/CD, unsigned installers, plaintext DB, FK pragma off, etc.). The CLAUDE.md "Key Patterns" section flags the most load-bearing constraints so day-to-day changes don't make them worse.
