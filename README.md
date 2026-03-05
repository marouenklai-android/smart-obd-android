# 🚗 Smart OBD Android

© 2026 Marouen Klai — All rights reserved.

A modern Android application that connects to a vehicle’s **OBD-II interface via Bluetooth (ELM327)** and provides **real-time diagnostics, sensor monitoring, and intelligent vehicle health reports**.

This project demonstrates practical experience with **Android development, Bluetooth hardware communication, clean architecture, and automotive diagnostics systems**.

---

# 🎯 Project Goal

Build a smart diagnostic Android application capable of:

- Reading live vehicle data
- Detecting and explaining OBD-II trouble codes (DTC)
- Visualizing engine data clearly
- Generating online diagnostic reports based on vehicle data
- Working offline for core diagnostics

---

# ✨ Key Features

## 🚘 Real-time Vehicle Monitoring

- Speed
- RPM
- Engine temperature
- Sensor data
- Live dashboard and charts

## 🔧 DTC Detection

- Scan and decode **OBD-II Diagnostic Trouble Codes**
- Offline explanation for common codes

## 📊 Intelligent Diagnostic Reports

- Combines vehicle data and detected faults
- Sends structured vehicle data to a remote API
- Generates a clear diagnostic report

*(Requires internet connection and API token)*

---

# 📱 Modern Android UI

- Built with **Jetpack Compose**
- Clean dashboard layout
- Onboarding screens

---

# 🧠 Tech Stack

- **Language:** Kotlin
- **Framework:** Android
- **UI:** Jetpack Compose
- **Architecture:** MVVM
- **Dependency Injection:** Dagger
- **Networking:** Retrofit
- **State Management:** StateFlow / SharedFlow
- **Vehicle Communication:** Bluetooth (ELM327)

---

# 🏗 Architecture

The application follows a **clean MVVM architecture** separating UI, business logic, and data sources.

Vehicle (OBD-II)
│
│ Bluetooth (ELM327)
▼
Bluetooth Service
│
▼
Repository
├─ Local parsing (Sensors + DTC)
└─ Remote API (Diagnostic report)
│
▼
ViewModel
│
▼
Jetpack Compose UI


---

# ⚙️ Communication Flow

1. App connects to **ELM327 Bluetooth adapter**
2. OBD commands are sent to the vehicle
3. Vehicle responses are parsed
4. Sensor data and DTC codes are extracted
5. ViewModel updates application state
6. UI updates dashboards and charts
7. Optional: vehicle data is sent to API to generate a diagnostic report

---

# 🔗 External Libraries & Tools

## 📦 obd-java-api

https://github.com/pires/obd-java-api

This library provides a set of ready-to-use **OBD commands and response parsers**.

It helps with:

- Executing standard OBD commands
- Parsing raw ELM327 responses
- Simplifying OBD-II communication

Using this library ensures compatibility with most standard **OBD-II vehicles**.

---

## 🧪 ELM327 Emulator

https://github.com/Ircama/ELM327-emulator

This emulator simulates an **ELM327 adapter**, allowing development and testing **without a physical vehicle**.

It allows:
- Testing Bluetooth communication
- Simulating sensor values
- Debugging OBD commands
- Faster development cycles

This improves reliability before real-world testing.

---

# 🧪 Hardware Requirements

To run the application with a real vehicle you need:

- ELM327 Bluetooth OBD-II adapter
- OBD-II compatible vehicle
- Android device with Bluetooth support

---

## 🎬 Demo Video

Watch the Smart OBD Android app in action:

[![Watch the video](https://img.youtube.com/vi/t6mYfH1Z9f4/0.jpg)](https://www.youtube.com/@MarouenKlai-t6m)

---

# 💡 Future Improvements

Possible enhancements:

- Export diagnostic reports as PDF
- Multi-vehicle profiles
- Historical diagnostic tracking
- Expanded DTC database
- Advanced analytics and insights

---

# ⚠️ Disclaimer

This application is for **informational and educational purposes only** and does **not replace professional mechanical diagnostics**.

---

# 📄 License & Usage

This project is published for **educational and portfolio purposes**.

You are allowed to:

- Study and modify the source code
- Run the application locally
- Contribute via pull requests

You are **NOT allowed to**:

- Publish this application to Google Play or other app stores
- Rebrand and redistribute it as your own product
- Monetize this project without explicit permission from the author

For commercial or store distribution, please contact the author.

---

# 🤝 Contributions

Contributions, improvements, and suggestions are welcome.

---

# ⭐ Author

**Marouen Klai**  
Android Engineer

Portfolio:  
https://marouenklai-android.github.io
