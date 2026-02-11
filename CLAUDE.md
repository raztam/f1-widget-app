# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
./gradlew assembleDebug        # Build debug APK
./gradlew assembleRelease      # Build release APK
./gradlew test                 # Run unit tests
./gradlew connectedAndroidTest # Run instrumented tests (device/emulator required)
./gradlew check                # Run all checks (lint + tests)
./gradlew clean                # Clean build cache
```

## Project Overview

Android home screen widget app displaying Formula 1 standings. Three widget types: individual driver card (`DriverWidget`), full driver standings table (`DriverStandingsWidget`), and individual constructor card (`ConstructorWidget`). minSdk 31 (Android 12+), targetSdk 34.

## Architecture

Single-module Gradle project (`app/`) using Kotlin DSL. Clean architecture with three layers:

- **Data layer** (`data/`): Jolpica/Ergast F1 API via Retrofit (`F1InfoApi`), Room database for caching (`AppDatabase` with `DriverDao`/`RaceDao`/`ConstructorDao`), local JSON assets (`LocalDataSource`). `Api.kt` fuses remote API data with local JSON files (`driver_additional_info.json`, `constructor_additional_info.json`) to enrich records with team colors/images.
- **Domain/ViewModel layer** (`viewmodels/`): `DriversViewModel` and `ConstructorsViewModel` manage state via `MutableStateFlow`.
- **Presentation layer**: Jetpack Compose for activities, Glance framework for widgets.

**Data flow:** Jolpica API → `Api.kt` (merge with local JSON) → `Repository` (cache in Room) → `ViewModel` → Compose/Glance UI

## Key Technical Decisions

- **DI: Koin** (not Hilt, despite README stating Hilt). Modules defined in `di/appModule.kt`, initialized in `F1WidgetApplication.kt`.
- **Widgets use Glance** (not legacy AppWidget XML). Widget state persisted via DataStore Preferences (`GlanceStateDefinition`).
- **Custom font rendering** in Glance widgets via bitmap conversion (`GlanceText.kt`) — workaround for Glance's limited font support.
- **Room uses `fallbackToDestructiveMigration()`** — schema changes will wipe local data. Current DB version: 11.
- **Widget settings** stored in SharedPreferences (per-widget, keyed by widget ID), not Room. Driver settings use `"widget_drivers"` prefs file, constructor settings use `"widget_constructors"` prefs file.
- **Background updates** via WorkManager (`UpdateWidgetsWorker`), scheduled based on next F1 race/sprint event time, not fixed intervals. Updates all widget types (Driver, DriverStandings, Constructor).
- **API base URL:** `https://api.jolpi.ca/ergast/` (Jolpica mirror of deprecated Ergast API).

## Widget Configuration Flow

Each widget type has its own configuration activity:
- `DriverWidgetSettingsActivity` — configures driver widgets (selected driver, background color as ARGB Int, transparency as 0.0-1.0 Float).
- `ConstructorWidgetSettingsActivity` — configures constructor widgets (selected constructor, background color, transparency). Same UI pattern as driver settings.

Both receive a widget ID via intent extras and persist settings to their respective SharedPreferences files.

## Dependencies (version catalog: `gradle/libs.versions.toml`)

Core: Compose BOM 2024.04.01, Glance 1.1.1, Room 2.6.1, Retrofit 2.11.0, Koin 3.5.3, WorkManager 2.10.0, colorpicker-compose 1.0.4.
