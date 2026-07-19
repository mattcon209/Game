# 🖤 Chains of Desire: Private BDSM Board Game

Welcome to **Chains of Desire**, a beautiful, seductive, offline private board game app designed specifically for polyamorous BDSM pods. This app is written as a **native Android Studio project** using **Kotlin** and **Jetpack Compose**. It runs 100% offline, protecting your group's privacy with absolutely no servers, accounts, or analytics.

---

## 🌪️ Brand New Chaos & Reshuffling Features Added!

I have updated the game's engine and UI to support your exact requests for extreme unpredictability, diversity, and dynamic shuffles:

1. **Randomize Board Layout Toggle**:
   - On the Setup Screen, you can now toggle **"Randomize Board Layout"**.
   - When enabled, clicking **"Begin Session"** will take all your active card spaces and board motifs and **completely shuffle and scramble them around the board perimeter** (keeping Tile #0 as "START" safe).
   - This ensures that every single game night features a completely custom, randomized, and unique board layout! You will never play the same map twice.
2. **Real-Time Board Reshuffle Space (🔮)**:
   - Added a new board game motif tile and action: **`BOARD_SHUFFLE` (Chaos Shuffle Space)**.
   - When a player lands on this space, a major event occurs: **"CHAOS STORM! All restraints are broken and reshuffled!"**
   - The game engine instantly calls `shuffleActiveBoard()` which **scrambles all active board tiles in real-time**! Emojis, labels, card-bindings, and custom rules swap places immediately on your screen while players' active positions remain unchanged. It's a true cyclone of entropy that turns the tide of the session instantly!
3. **Double Challenge Card Spaces (🔥)**:
   - Added a programmable action **`DOUBLE_DARE`** which forces the landing player to complete **two cards back-to-back** (Stage 1 of 2 and Stage 2 of 2) to successfully claim points. If they refuse either, they take a spanking or punishment!
4. **Fully Programmable Custom Text Spaces**:
   - Create tiles that run your own explicit commands or custom party rules (e.g., *"Take off {player}'s collar"* or *"{player} must obey {target1} for 1 minute"*), fully supporting polyamorous name replacements.

---

## 🚀 How to Run this on your Samsung Galaxy S24+

### Step 1: Download the Project
1. Download this updated `ChainsOfDesire` folder as a ZIP file from your Arena workspace.
2. Extract the ZIP folder on your computer.

### Step 2: Open in Android Studio
1. Launch **[Android Studio](https://developer.android.com/studio)** (official, free).
2. Click **Open** (or **File > Open**), select the extracted `ChainsOfDesire` folder, and wait a moment for the Gradle sync process to complete.

### Step 3: Enable Developer Options on your Galaxy S24+
To let your PC compile and transfer the game directly to your phone:
1. On your S24+, open **Settings > About Phone > Software Information**.
2. Tap **Build Number** exactly **7 times** in a row. Enter your pattern/pin if prompted.
3. Go back to main **Settings**, tap **Developer Options** at the bottom, and enable **USB Debugging**.

### Step 4: Plug in and Play!
1. Plug your Samsung S24+ into your computer with a USB-C cable. Select **Allow USB Debugging** if prompted on your screen.
2. In Android Studio's top bar, select your device (e.g. `Samsung SM-S928B`) and click the green **Run (Play button)** (or press `Shift + F10`).
3. The app will compile and launch directly on your phone!

---

## 🛠️ File Layout Reference
All updated code is live and written inside:
* 📁 `gradle/libs.versions.toml`: Handles versions and dependency setups.
* 📁 `app/build.gradle.kts`: Configures Jetpack Compose & Android compile SDK 34 (Android 14/15).
* 📁 `app/src/main/AndroidManifest.xml`: Locks screen to Portrait layout (very convenient for passing around a single device).
* 📁 `app/src/main/java/com/polylove/marble/`
  - `MainActivity.kt`: Starts the app with the dark BDSM theme.
  - `ui/theme/Color.kt` & `Theme.kt`: Defines the latex, leather, and crimson visual styles.
  - `game/GameData.kt`: Holds the custom BDSM prompt & punishment databases, dynamic placeholder parser, and grid math coordinate structures with randomized board spawning.
  - `ui/GameScreens.kt`: Houses the full interactive gameplay screens, Setup, the board rendering, card modal dialogs, and the built-in Creation Mode Editor with programmable tiles and active board reshuffling logic!
