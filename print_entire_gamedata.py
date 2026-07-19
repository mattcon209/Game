file_path = "/home/user/PolyLoveMarble/app/src/main/java/com/polylove/marble/game/GameData.kt"

with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

content = content.replace("\r\n", "\n")
lines = content.split("\n")

print(f"Total lines: {len(lines)}")
for i, line in enumerate(lines[:150]):
    print(f"{i+1}: {line}")
