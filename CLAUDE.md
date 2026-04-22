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

Clean Architecture with four main layers inside `app/src/main/kotlin/`:

```
domain/       → Domain entities (JPA), interfaces (IDao contracts), enums
application/     → Business logic: Handler classes coordinate UseCases per feature
infrastructure/      → Hibernate/SQLite config, DAO implementations, Flyway migrations
view/       → Compose Desktop UI: screens, dialogs, forms, shared components
viewModel/  → Kotlin Flow-based state management for each screen
utils/      → DateTimeUtils, CurrencyUtils, IconPaths
```

**Data flow:** `View` → `ViewModel` → `Domain Handler` → `UseCase` → `DAO (infrastructure)` → SQLite

### Key Patterns

- **Handlers** (`application/*/Handler`) coordinate multiple use cases for a feature area (e.g., `TransactionHandler`).
- **Use cases** (`application/*/*UseCase`) contain single-responsibility business operations.
- **DAOs** in `infrastructure/` implement interfaces from `domain/` using Hibernate Criteria API.
- **Database** is SQLite at `~/.granawiz/database/granawiz.db`, managed by Flyway migrations in `src/main/resources/db/migration/`. Migrations run automatically on startup via `DatabaseConfig.runMigrations()`.
- **Account types** use JPA inheritance: `BankAccount` → `CheckingAccount`, `SavingsAccount`, `CreditCardAccount`.

### Tech Stack

| Layer | Technology |
|-------|-----------|
| UI | Jetpack Compose Desktop 1.7.3 |
| Language | Kotlin 2.1.0 |
| ORM | Hibernate 6.4.4 + Jakarta JPA 3.1 |
| Database | SQLite 3.48 |
| Migrations | Flyway 11.1.1 |
| Logging | Logback 1.5.16 |
| Icons | Phosphor Icons |
| Packaging | JPackage via Compose Multiplatform plugin |

## Entry Point

`Main.kt` — sets up the Compose Desktop `application` window. `AppConfig.kt` defines paths (e.g., DB location).
