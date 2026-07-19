# 📱 How to Build this APK Directly on your Samsung Galaxy S24+ (No PC Required!)

If you do not want to use a computer with Android Studio, you can actually compile this entire project and build the installable **APK** directly on your **Samsung Galaxy S24+**! 

Because the S24+ has a state-of-the-art processor (Snapdragon 8 Gen 3) and plenty of RAM, it can run a full Gradle build extremely fast.

Here are the two best ways to compile this project directly on your phone:

---

## 🛠️ Method 1: Using AndroidIDE (Recommended & Easiest)

**AndroidIDE** is an incredible, modern, open-source Android app that acts as a full-featured IDE (similar to Android Studio) running directly on Android. It has full support for **Gradle, Kotlin, and Jetpack Compose**, meaning it can open and build this exact project on your S24+ with a single tap.

### Step-by-Step Instructions:
1. **Download AndroidIDE**:
   - Open your browser on your S24+ and go to the official **[AndroidIDE GitHub Releases](https://github.com/AndroidIDE-Official/AndroidIDE/releases)** or download it from **F-Droid**.
   - Download the latest `.apk` file (make sure to select the `arm64-v8a` version, which is the high-performance architecture of your S24+).
   - Install the AndroidIDE app on your phone.
2. **Transfer the Project to your Phone**:
   - Download the `ChainsOfDesire` folder from your Arena workspace as a ZIP file directly onto your S24+.
   - Use Samsung's "My Files" app (or any file manager) to extract the ZIP file into your phone's internal storage (e.g., inside a folder like `/Documents` or `/Download`).
3. **Open the Project in AndroidIDE**:
   - Launch **AndroidIDE**.
   - On the first boot, it will ask to download necessary SDK components (Build Tools, JDK). Let it download them (takes 1-2 minutes).
   - Tap **Open Project** and navigate to your extracted `ChainsOfDesire` folder.
4. **Compile the APK**:
   - Once the project is open, AndroidIDE will perform a Gradle sync (you will see a progress bar at the bottom).
   - Tap the **Build / Hammer** icon in the top toolbar or select **Build APK**.
   - AndroidIDE will compile the project and notify you when the build succeeds.
5. **Install and Play**:
   - Tap the built `.apk` file directly inside AndroidIDE's file manager to install it on your S24+, and start playing!

---

## 💻 Method 2: Using Termux (Command Line / Pro Method)

If you love using command-line tools, you can run a full Linux-like Gradle build inside **Termux**, a powerful terminal emulator for Android.

### Step-by-Step Instructions:
1. **Install Termux**:
   - Download and install **Termux** on your S24+ (obtain it from **F-Droid** or GitHub, as the Play Store version of Termux is outdated).
2. **Install OpenJDK and Build Tools**:
   - Launch Termux and run the following setup commands:
     ```bash
     pkg update && pkg upgrade -y
     pkg install openjdk-17 -y
     ```
3. **Grant Storage Permission & Copy Files**:
   - Give Termux permission to access your phone's files:
     ```bash
     termux-setup-storage
     ```
   - Copy your extracted `ChainsOfDesire` folder from your downloads folder into your Termux home directory:
     ```bash
     cp -r /sdcard/Download/ChainsOfDesire ~/
     cd ~/ChainsOfDesire
     ```
4. **Compile the APK via Gradle**:
   - Set Gradle wrapper execution permission and run the debug build command:
     ```bash
     chmod +x gradlew
     ./gradlew assembleDebug
     ```
   - Gradle will compile the app directly in Termux! When completed, you will see `BUILD SUCCESSFUL`.
5. **Install the built APK**:
   - The compiled `.apk` will be saved inside:
     `app/build/outputs/apk/debug/app-debug.apk`
   - Copy it back to your downloads folder so you can tap and install it easily:
     ```bash
     cp app/build/outputs/apk/debug/app-debug.apk /sdcard/Download/ChainsOfDesire.apk
     ```
   - Open your Samsung **My Files** app, navigate to your Downloads, tap `ChainsOfDesire.apk`, and install it!
