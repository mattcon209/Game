#!/bin/bash
set -e

echo "=== ☕ Reinstalling JDK 21 ==="
sudo apt-get update -y
sudo apt-get install openjdk-21-jdk -y

echo "=== 🔏 Updating CA Certificates ==="
sudo update-ca-certificates -f

echo "=== 📥 Downloading and Extracting Gradle 8.5 ==="
if [ ! -f /home/user/gradle-8.5-bin.zip ]; then
  wget -q https://services.gradle.org/distributions/gradle-8.5-bin.zip -O /home/user/gradle-8.5-bin.zip
fi
rm -rf /home/user/gradle-bin
unzip -q /home/user/gradle-8.5-bin.zip -d /home/user/gradle-bin

echo "=== 🛠️ Slicing and Regenerating All Assets ==="
python3 recreate_all_pngs.py

echo "=== 🚀 Patching Code and Compiling APK ==="
python3 patch_and_build_ultimate_apk.py

echo "=== 🎉 DONE! APK compiled successfully! ==="
