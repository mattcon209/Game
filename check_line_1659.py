file_path = "/home/user/PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/GameScreens.kt"

with open(file_path, "r", encoding="utf-8") as f:
    lines = f.readlines()

# Print lines 1640 to 1680 (0-indexed, so 1639 to 1679)
for i in range(1640, 1685):
    if i < len(lines):
        print(f"{i+1}: {repr(lines[i])}")
