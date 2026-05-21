# ParkVahan — Smart Parking Android App

A modern Android application that connects parking-spot **owners** with **users** looking for safe, reliable, on-demand parking. Built natively with **Kotlin + Jetpack Compose**, backed by **Firebase**, and styled with **Material 3**.

> **Status:** Active development — core auth, dashboards, and booking flow implemented.

---

## ✨ Features

### For Users
- Onboarding & splash flow
- Sign up / Login (Firebase Authentication — email/password)
- Browse available parking slots near you
- Book a slot in real time
- View and manage your past & upcoming bookings
- Edit profile

### For Parking-Spot Owners
- Owner-specific dashboard with custom Material 3 theme
- Manage parking spots, availability, and pricing
- View incoming booking requests

### Architecture
- Role-based navigation (User vs. Owner)
- Reactive state via `StateFlow` + Jetpack ViewModel
- Dependency injection with **Hilt**
- Single-Activity, Compose-only UI

---

## 🛠 Tech Stack

| Layer | Technology |
|------|------------|
| Language | **Kotlin** (JVM 17) |
| UI | **Jetpack Compose**, Material 3, Compose Navigation |
| Architecture | MVVM, Single-Activity |
| DI | **Hilt** (Dagger) + KSP |
| Backend | **Firebase** — Auth, Realtime Database, Firestore, Storage, Cloud Messaging |
| Network | Retrofit + OkHttp + Gson |
| Maps | OSMDroid (OpenStreetMap — no API key needed) |
| Images | Coil |
| Async | Kotlin Coroutines + Flow |
| Persistence | Jetpack DataStore (Preferences) |
| Splash | AndroidX Core Splash Screen API |

---

## 📁 Project Structure

```
app/src/main/java/com/example/parkvahan/
├── MainActivity.kt                ← Single activity, hosts Compose UI
├── ParkvahanApp.kt                ← @HiltAndroidApp Application class
│
├── data/
│   └── model/                     ← Data classes (UserRole, etc.)
│
├── di/
│   └── FirebaseModule.kt          ← Hilt module for Firebase dependencies
│
├── navigation/
│   ├── nav.kt                     ← NavHost + role-based routing
│   └── navroutes.kt               ← Route constants
│
├── screen/
│   ├── auth/                      ← Splash, onboarding, login, register
│   ├── user/                      ← User home, slots, bookings, profile
│   └── owner/                     ← Owner dashboard + custom theme
│
├── viewmodel/
│   ├── AuthViewModel.kt
│   └── OwnerDashboardModels.kt
│
└── ui/theme/                      ← Material 3 theme, typography
```

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK 17**
- **Min SDK 26** (Android 8.0), **Target/Compile SDK 36**

### Setup

1. Clone the repo
   ```bash
   git clone https://github.com/gurpreet-rrr/parkvahan.git
   cd parkvahan
   ```

2. **Add your own Firebase config:**
   - Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
   - Enable: **Authentication** (Email/Password), **Realtime Database**, **Firestore**, **Storage**
   - Download `google-services.json` and place it in `app/`

3. Open in Android Studio → `Sync Gradle` → `Run` ▶️

> The bundled `google-services.json` is for the developer's demo Firebase project. Replace it with yours before publishing.

---

## 📸 Screenshots<img width="717" height="1600" alt="image" src="https://github.com/user-attachments/assets/1b523274-7b39-4f20-8a1c-30b14e7a3701" />
<img width="717" height="1600" alt="image" src="https://github.com/user-attachments/assets/16ff390b-1f5e-4c83-937d-b94e424b3607" />
<img width="717" height="1600" alt="image" src="https://github.com/user-attachments/assets/6666c6a5-3fa3-4a47-a139-54966111ac2a" />
<img width="717" height="1600" alt="image" src="https://github.com/user-attachments/assets/292c2258-b8cd-4740-9e0e-78e7bceff6c5" />
<img width="717" height="1600" alt="image" src="https://github.com/user-attachments/assets/83602e8c-a935-4bff-8a83-f88e972522de" />

<img width="717" height="1600" alt="image" src="https://github.com/user-attachments/assets/5e6ac13d-eb81-4eee-b0f9-1cefd6d75e1c" />
<img width="717" height="1600" alt="image" src="https://github.com/user-attachments/assets/a05b5497-51d2-43f4-8aee-b913ccaf52ef" />









| Onboarding | Login | User Home |
|---|---|---|
| _coming soon_ | _coming soon_ | _coming soon_ |

| Slot Booking | Owner Dashboard | Profile |
|---|---|---|
| _coming soon_ | _coming soon_ | _coming soon_ |

---

## 🗺 Roadmap

- [x] Auth (login, register, role selection)
- [x] User home + slot listing
- [x] Booking flow
- [x] User profile & past bookings
- [x] Owner dashboard scaffolding
- [ ] Live parking-spot map (OSMDroid)
- [ ] Push notifications (FCM)
- [ ] Payment integration
- [ ] Owner spot CRUD with photos (Storage)
- [ ] Reviews & ratings

---

## 👤 Author

**Gurpreet Chugh**
B.Tech CSE — IK Gujral Punjab Technical University, Kapurthala

- GitHub: [@gurpreet-rrr](https://github.com/gurpreet-rrr)
- Email: gurpjhg@gmail.com

---

## 📄 License

This project is for educational and portfolio purposes. Feel free to study the code, but please don't republish as-is.
