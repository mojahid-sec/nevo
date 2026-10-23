# NIVO — One Day at a Time.

**NIVO** is an offline-first personal goal and habit tracker built natively for Android using Kotlin and Jetpack Compose. It is designed around the core philosophy of self-discipline: focusing entirely on today's milestone without intrusive ads, online trackers, accounts, or noisy distractions.

---

## 🌟 Key Features

1. **Strict 24-Hour Check-In Rule**:
   - Each goal maintains an independent timer.
   - Successful check-in locks subsequent check-ins for exactly 24 hours (86,400,000 milliseconds).
   - Real-time countdown ticker displays hours, minutes, and seconds until the next check-in unlocks.
   - Atomic lock prevents duplicate or accidental double taps.

2. **Goal Duration Flexibility (2 to 365 Days)**:
   - Full support for any integer duration between 2 and 365 days (e.g., 2, 7, 15, 30, 90, 180, 365 days).
   - Direct numerical input, stepper controls (`-` and `+`), and quick preset chips.
   - Real-time expected completion date calculation.

3. **Complete English & Bangla Localization (বাংলা)**:
   - Instant language switching in Settings without restarting the app.
   - Full translation of all UI screens, calendar elements, navigation, buttons, error messages, and dialogs.
   - Automatic Bangla numeral conversion (`০, ১, ২, ৩, ৪, ৫, ৬, ৭, ৮, ৯`).
   - 55+ unique English and 55+ unique Bangla motivational quotes.

4. **100% Offline-First Architecture**:
   - Zero internet permissions (`android.permission.INTERNET` is omitted).
   - Local storage persistence for goals, check-in history, last check-in timestamps, and user preferences.
   - Easy backup export and clipboard copy in Settings.

5. **Optional App Lock**:
   - Optional 4-digit PIN lock with salted SHA-256 secure verification (never stored in plain text).
   - Keypad lock screen protecting goal records upon launch and resume.

6. **Dedicated About Developer Screen**:
   - **Developer**: MD. MOJAHIDUR RAHMAN SAJID
   - **Department**: Computer Science & Technology
   - **Institution**: Bogura Polytechnic Institute
   - **Location**: Bogura, Bangladesh
   - Safe photo display (`ContentScale.Fit`) with graceful fallback placeholder.

7. **10 Selectable Premium Themes**:
   - **Crimson**: Original OLED Minimalist — pure pitch black and vivid scarlet crimson.
   - **Ocean**: Deep navy & azure wave — calming oceanic discipline.
   - **Violet**: Midnight purple & electric violet — deep mystical focus.
   - **Amber**: Warm obsidian & golden amber — rich comforting energy.
   - **Monochrome**: Pitch black & silver white — stealth minimalism.
   - **Pure White**: True light theme with pure white background, dark high-contrast text, and scarlet accent.
   - **Rose**: Espresso dark plum & vibrant neon rose — passionate perseverance.
   - **Arctic**: Glacial obsidian & frost cyan — cool razor-sharp clarity.
   - **Luxury Gold**: Imperial onyx & royal 24K gold — timeless prestige.
   - **Slate**: Industrial graphite & titanium steel — modern refined strength.
   - Zero green anywhere across all palettes. Live theme switching with 4-color swatch preview in Settings.

8. **Home Screen Quick Goal Switcher**:
   - Seamlessly toggle between multiple active goals with a single tap directly from the Home screen header pill.

---

## 🛠️ Tech Stack & Requirements

- **Language**: Kotlin 2.2.10
- **UI Toolkit**: Jetpack Compose (Material 3)
- **Architecture**: MVVM with Repository Pattern and Kotlin Coroutines / StateFlow
- **Persistence**: Offline SharedPreferences with JSON records
- **JDK Requirement**: OpenJDK 17 or OpenJDK 21
- **Android SDK**: `compileSdk = 36`, `minSdk = 24`, `targetSdk = 36`

---

## 🚀 Getting Started

### Opening in Android Studio or VS Code
1. Clone or extract the project to your local machine.
2. Open Android Studio (Ladybug / Iguana or later recommended).
3. Select **File > Open...** and navigate to the project directory.
4. Allow Gradle to synchronize dependencies automatically.

### Building the Debug APK Locally
In your terminal at the root directory:
```bash
# On Linux / macOS:
./gradlew assembleDebug

# On Windows:
gradlew.bat assembleDebug
```

The generated APK will be output to:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Installing the APK on an Android Phone
1. Connect your Android device via USB with **USB Debugging** enabled in Developer Options.
2. Run:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
   *Alternatively*, transfer `app-debug.apk` directly to your phone via USB/drive and tap to install (enable "Install unknown apps" if prompted).

### Running Unit Tests
```bash
./gradlew :app:testDebugUnitTest
```

---

## 📷 Replacing Developer Photograph
The real developer photo is located at:
```
app/src/main/res/drawable/developer_photo.png
```
To update the photograph:
1. Save your photograph as a PNG file named `developer_photo.png`.
2. Replace `app/src/main/res/drawable/developer_photo.png`.
3. The UI automatically displays the new photo with `ContentScale.Fit` in the reserved frame on the About Developer screen. If the file is absent, a monogram placeholder is rendered automatically.

---

## 🤝 Project Structure
```
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── data/
│   │   │   │   │   ├── localization/ (AppLanguage, NivoStrings, MotivationalQuotes)
│   │   │   │   │   ├── model/ (Goal, CheckInRecord)
│   │   │   │   │   ├── repository/ (GoalRepository)
│   │   │   │   │   └── security/ (PinManager)
│   │   │   │   └── ui/
│   │   │   │       ├── components/ (NivoRibbonSymbol, NivoLogo, CircularProgressRing, NivoButton, MotivationalCard)
│   │   │   │       ├── navigation/ (Screen, BottomNavDestination)
│   │   │   │       ├── screens/ (HomeScreen, CalendarScreen, GoalsScreen, CreateGoalScreen, GoalCompletionScreen, SettingsScreen, AboutDeveloperScreen, PinLockScreen)
│   │   │   │       ├── theme/ (Color, Theme, Type)
│   │   │   │       └── viewmodel/ (NivoViewModel)
│   │   │   └── res/
│   │   │       ├── drawable/ (ic_nivo_ribbon.xml, developer_photo.png, ic_launcher_background.xml, ic_launcher_foreground.xml)
│   │   │       └── mipmap-*/ (Adaptive launcher icons)
│   │   └── test/
│   │       └── java/com/example/ (NivoGoalTest.kt, ExampleUnitTest.kt)
│   └── build.gradle.kts
├── settings.gradle.kts
├── build.gradle.kts
└── README.md
```

---

## 📄 License
Created by **MD. MOJAHIDUR RAHMAN SAJID**. Built with care for discipline and personal growth.
