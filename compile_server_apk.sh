#!/bin/bash
set -e

echo "=== ☕ Installing OpenJDK 21 in this session ==="
sudo apt-get update -y
sudo apt-get install openjdk-21-jdk -y

# Force Java 21 (JDK 21) across the entire build context
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "=== 📥 Downloading Android Commandline Tools ==="
mkdir -p /home/user/android-sdk/cmdline-tools
wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O /home/user/cmdline-tools.zip

echo "=== 📂 Extracting Tools ==="
rm -rf /home/user/android-sdk/cmdline-tools/latest
unzip -q /home/user/cmdline-tools.zip -d /home/user/android-sdk/cmdline-tools
mv /home/user/android-sdk/cmdline-tools/cmdline-tools /home/user/android-sdk/cmdline-tools/latest

echo "=== 🔏 Auto-Accepting Google SDK Licenses ==="
yes | /home/user/android-sdk/cmdline-tools/latest/bin/sdkmanager --licenses --sdk_root=/home/user/android-sdk

echo "=== ⚙️ Installing SDK Platforms and Build-Tools (Android 14) ==="
/home/user/android-sdk/cmdline-tools/latest/bin/sdkmanager --sdk_root=/home/user/android-sdk "platforms;android-34" "build-tools;34.0.0"

echo "=== 📥 Downloading Gradle 8.5 ==="
if [ ! -f /home/user/gradle-8.5-bin.zip ]; then
  wget -q https://services.gradle.org/distributions/gradle-8.5-bin.zip -O /home/user/gradle-8.5-bin.zip
fi

echo "=== 📂 Extracting Gradle 8.5 ==="
rm -rf /home/user/gradle-bin
unzip -q /home/user/gradle-8.5-bin.zip -d /home/user/gradle-bin

echo "=== 🛠️ Linking SDK and Preparing Build ==="
cd /home/user/PolyLoveMarble
echo "sdk.dir=/home/user/android-sdk" > local.properties

export ANDROID_HOME=/home/user/android-sdk

echo "=== 🚀 Compiling Android APK in Sandbox ==="
/home/user/gradle-bin/gradle-8.5/bin/gradle assembleDebug --no-daemon

echo "=== 📥 Copying APK to Workspace Root ==="
cp app/build/outputs/apk/debug/app-debug.apk /home/user/PolyKinkMarble.apk

echo "================================================="
echo "🎉 SUCCESS! Your APK is ready at /home/user/PolyKinkMarble.apk!"
echo "================================================="
