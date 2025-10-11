# Words — Android app for vocabulary learning

> A modular Android project that helps users add new words, practice with exercises, and track
> progress. Supports phone-based sign‑up, user media attachments for words, and a **test** payments
> flow (no real charges).

![Android](https://img.shields.io/badge/Platform-Android-brightgreen)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-orange)
![Java](https://img.shields.io/badge/Language-Java-blue)
![Gradle](https://img.shields.io/badge/Build-Gradle-lightgrey)

---

## 🔎 Features

- **Accounts**: phone registration, sign‑in, profile management.
- **Telegram sign‑in (optional)**: phone confirmation and service messages (login codes, payment
  notices).
- **Add words**: personal dictionaries and playlists, quick input.
- **Exercises**: training tasks to reinforce vocabulary and repetition.
- **Progress & stats**: learning tracking and results.
- **Media for words**: custom images & audio (or system defaults if not set).
- **Spotlight onboarding**: in‑app hints and visual walkthroughs.
- **Payments**: **test mode only**, no real charges (e.g., to unlock premium features).
- **Modular architecture**: independent modules for easy development and maintenance.

> Roadmap ideas: AI hints & word recognition, production payments, achievements screen.

---

## 🧱 Architecture & Modules

```
/app          — app shell & DI composition
/core         — base components, utilities, resources
/auth         — registration, login, Telegram auth
/addword      — adding/editing words
/exercise     — exercises & training flows
/learning     — learning logic, spaced repetition
/profile      — user profile & settings
/documents    — docs (policy, instructions)
/validation   — input validation
```

**Primary pattern:** **MVI (Model‑View‑Intent)**

- **View** renders immutable **State** and emits **Intents** (user actions).
- **Model** (use‑cases/interactors) processes intents, interacts with repositories (Retrofit/Room),
  and reduces results into new **State**.
- Unidirectional flow simplifies testing, error handling, and time‑travel/debug logging.

Modules are isolated and wired via Gradle dependencies and DI (Koin). This simplifies testing and
scaling.

---

## 🧰 Tech stack

- **Languages:** Kotlin, Java
- **Build:** Gradle
- **Android SDK & AndroidX** components
- **Architectural pattern:** **MVI (Model‑View‑Intent)** with unidirectional data flow and immutable
  UI state
- **DI:** Koin
- **Persistence:** **Room** (AndroidX Room Runtime & KTX)
- **Networking:** **Retrofit 2** + **OkHttp** + **Gson** (REST/JSON API)
- **Media:** AndroidX **Media3/ExoPlayer** for audio/video
- **Images:** **Glide**
- **UI onboarding:** **Spotlight**
- **Documents/PDF:** AndroidPdfViewer
- **Scanning/QR (if needed):** Quickie
- **Firebase:** Analytics, Remote Config (via BoM)
- **Payments:** Google Pay / Wallet in **TEST** mode (no real charges)
- **Backend:** service‑oriented architecture (SOA); mobile app communicates via REST endpoints

## 🚀 Quick start

### Requirements

- Android Studio (latest stable)
- Android SDK & platform tools installed
- Emulator or Android device

### Install & run

```bash
git clone https://github.com/VladM31/AndroidWordApplication.git
cd AndroidWordApplication
./gradlew clean assembleDebug
```

1) Open the project in Android Studio
2) Sync Gradle
3) Run the **app** configuration on an emulator/device

---

## 📈 Analytics & data

- The app stores learning progress and exercise results.
- User media (images/audio) attached to words — stored locally/in app storage; system defaults are
  used if none provided.
- See the [Privacy Policy](./documents/policy/en_policy.pdf) for details.

---

## 📎 Useful links

- **Instruction**: [en_instruction.pdf](./documents/instruction/en_instruction.pdf)
- **License**: [LICENSE](./LICENSE)

---

## 📄 License

This project is distributed under a proprietary license — © 2025 VladM31. All rights reserved. Use,
modification, or distribution of this software for commercial purposes is prohibited without
explicit written permission from the author. See the full text here: [LICENSE](./LICENSE)

---

## 📬 Contacts

Questions and suggestions: [javacamp2021@gmail.com](mailto:javacamp2021@gmail.com)

