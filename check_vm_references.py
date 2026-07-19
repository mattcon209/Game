file_path_vm = "/home/user/PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/GameViewModel.kt"

with open(file_path_vm, "r", encoding="utf-8") as f:
    lines = f.readlines()

# Print lines 100 to 130 of GameViewModel.kt (0-indexed, so 99 to 129)
for i in range(100, 130):
    if i < len(lines):
        print(f"{i+1}: {repr(lines[i])}")
