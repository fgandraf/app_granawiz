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
view/           → Compose Desktop UI: screens, dialogs, forms, shared components
viewModel/      → Kotlin Flow-based state management for each screen
utils/          → DateTimeUtils, CurrencyUtils, PainterUtils, IconPaths
```

**Data flow:** `View` → `ViewModel` → `Handler` → `UseCase` → `Repository (infrastructure)` → SQLite

### Key Patterns

- **Handlers** (`application/*/Handler`) act as facades, coordinating multiple use cases for a feature area.
- **Use cases** (`application/*/*UseCase`) are single-responsibility business operations (~44 total).
- **Repository contracts** in `domain/contracts/` define interfaces (`IAccountRepository`, etc.) implemented by `infrastructure/repository/`.
- **Repositories** use Hibernate Criteria API with explicit `Hibernate.initialize()` calls to handle lazy loading.
- **Database** is SQLite at `~/.granawiz/database/granawiz.db`, managed by Flyway migrations in `src/main/resources/db/migration/`. Migrations run automatically on startup via `DatabaseConfig.runMigrations()`.
- **Account types** use JPA single-table inheritance with discriminator column: `BankAccount` → `CheckingAccount`, `SavingsAccount`, `CreditCardAccount`.
- **IFilterable** is a marker interface implemented by entities that support filtering (party, account, category, etc.).
- **Navigation** uses a `Screen` sealed class (`view/modules/Screen.kt`) routed by `MainContent.kt`.
- **UserPreferences** (`view/modules/UserPreferences.kt`) is an app-wide singleton for theme state.

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
| UI | Jetpack Compose Desktop 1.7.3 |
| Language | Kotlin 2.1.0 |
| ORM | Hibernate 6.4.4 + Jakarta JPA 3.1 |
| Database | SQLite 3.48 |
| Migrations | Flyway 11.1.1 |
| Logging | Logback 1.5.16 |
| Icons | Phosphor Icons 1.0.0 (Bold & Regular variants) |
| Packaging | JPackage via Compose Multiplatform plugin |

## Entry Point

`Main.kt` — sets up the Compose Desktop `application` window. `AppConfig.kt` defines paths (e.g., DB location).
