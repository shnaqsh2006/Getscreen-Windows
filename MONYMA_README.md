# MONYMA – Ultimate Finance Tracker (Android)

MONYMA is a modern personal finance manager for Android built with Kotlin, Jetpack Compose (Material 3), Room, Hilt and MVVM. This repository contains a multi-module project with a local-only baseline that runs entirely offline (no Firebase). Cloud sync (Firebase) can be added later without breaking changes.

## Modules
- `app`: Android application, navigation and DI setup
- `core-model`: Domain models and enums
- `core-database`: Room entities, DAOs, and database
- `core-data`: Repository interfaces and implementations
- `feature-dashboard`, `feature-transactions`, `feature-budgets`, `feature-reports`, `feature-settings`: UI features for the 5-tab bottom navigation

## Highlights
- Jetpack Compose + Material 3 UI
- MVVM with StateFlow
- Room for offline storage
- Hilt for dependency injection
- Bottom navigation with 5 sections (Dashboard, Transactions, Budgets, Reports, Settings)

## Getting Started
1. Open this project in Android Studio (Giraffe or newer recommended)
2. Ensure you have JDK 17 configured
3. Let Android Studio sync Gradle. If the Gradle Wrapper is missing, use "Generate Gradle Wrapper" or run Gradle from Android Studio to create it.
4. Select `app` run configuration and launch on an emulator or device

## Next Steps
- Implement Firebase Auth/Firestore for cloud sync (separate flavor)
- Add recurring transactions and budget analytics
- Implement exports (CSV/Excel)
- Add biometric lock screen and encryption for local data

## License
All rights reserved. For evaluation and personal use only.