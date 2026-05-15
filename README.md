# Shishu-Sneh Android App

Kotlin + Jetpack Compose Android scaffold generated from `readme.md.md`.

Included: app module, package `com.shishusneh.app`, Compose navigation for all 19 screens, MVVM/Hilt repositories, Room database with 9 entities, Gemini Retrofit wrapper, WorkManager stubs, TTS/PDF utilities, multilingual resources, and JSON assets.

## Setup
1. Open this folder in Android Studio.
2. Add Firebase `google-services.json` to `app/` for phone OTP, Firestore, and FCM.
3. Add keys to `local.properties`:

```properties
GEMINI_API_KEY=your_key_here
FIREBASE_PROJECT_ID=your_project
```

4. Sync Gradle and run the `app` configuration.

This is a working MVP foundation. Production completion still needs Firebase console wiring, JSON seeding at startup, notification scheduling, runtime permissions, chart rendering, release signing, and deeper screen polish.
