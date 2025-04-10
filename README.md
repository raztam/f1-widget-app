🏎️ F1 Widget App
A lightweight Android app that provides Formula 1 driver standings directly on your home screen — no need to open an app!
This app shows driver scores and championship positions, based on the latest available data, not real-time race updates(yet).

✨ Features
📋 Display current F1 driver standings.

🎯 Choose your favorite driver to follow.

🧩 Multiple widget types available.

⚡ Lightweight and battery-friendly.

🛠️ Architecture
The app follows a modular clean architecture approach:

Data Layer (data/):
Manages local assets and driver data (e.g., reading JSON files).

Domain Layer:
Business logic (inside viewmodels/).

Presentation Layer:
UI elements built with Jetpack Compose (composables/, activities/, widgets/).

It also uses Dependency Injection via Hilt (di/ folder).

📂 Folder Structure
Folder	Purpose
activities/	Main activities like launcher or settings.
composables/	Reusable Compose UI components.
data/	Data sources (e.g., local JSON files with driver information).
di/	Hilt modules for dependency injection.
ui/theme/	App theming (colors, typography).
viewmodels/	ViewModels managing UI-related data.
widgets/	Homescreen widgets (Driver Standings, etc.).
workers/	Background tasks (e.g., widget updates).
F1WidgetApplication.kt	Application class for initializing Hilt.

🔧 Tech Stack
Kotlin + Jetpack Compose for UI

Android Widgets (Glance / App Widgets)

Hilt for Dependency Injection

WorkManager for periodic widget updates

Local JSON assets for offline driver data

🚀 Getting Started
Clone the repo:

bash
Copy
Edit
git clone https://github.com/raztam/f1-widget-app.git
Open the project in Android Studio, build, and run it on a device (widgets require a real device or widget-enabled emulator).

📈 Roadmap / Future Improvements
Live data integration (optional, if needed).

More widget customization (themes, favorite teams).

Support for Constructors' Championship.

🏁 License
This project is licensed under the MIT License.

