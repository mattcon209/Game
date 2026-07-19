import os
import subprocess

print("--- PERFECT COMPILE PROCESS started ---")

# 1. Touch AndroidManifest.xml (add a space to force invalidate resource task caches)
manifest_path = "PolyLoveMarble/app/src/main/AndroidManifest.xml"
with open(manifest_path, "r", encoding="utf-8") as f:
    manifest = f.read()

# Make a tiny insignificant change (add a space at the end of a line)
manifest = manifest.replace("</manifest>", " </manifest>")

with open(manifest_path, "w", encoding="utf-8") as f:
    f.write(manifest)
print("AndroidManifest.xml touched to invalidate resource caches!")

print("--- RUNNING GRADLE DIRECT REBUILD WITH RERUN-TASKS ---")
# Run assembleDebug directly, forcing task rerun to ensure R.java is fully regenerated with the new images
result = subprocess.run(
    ["/home/user/gradle-bin/gradle-8.5/bin/gradle", "assembleDebug", "--no-daemon", "--rerun-tasks"],
    cwd="PolyLoveMarble/",
    capture_output=True,
    text=True
)

print("Gradle STDOUT:")
print(result.stdout[-1500:])
print("Gradle STDERR:")
print(result.stderr)

if result.returncode == 0:
    print("SUCCESSFUL COMPILE! Copying APK...")
    subprocess.run(["cp", "PolyLoveMarble/app/build/outputs/apk/debug/app-debug.apk", "/home/user/PolyKinkMarble.apk"])
    print("Perfect APK compiled successfully!")
else:
    print(f"FAILED PERFECT COMPILE! Code: {result.returncode}")
    exit(1)
