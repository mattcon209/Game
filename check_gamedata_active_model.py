file_path = "/home/user/PolyLoveMarble/app/src/main/java/com/polylove/marble/game/GameData.kt"

with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

content = content.replace("\r\n", "\n")

if "data class ActiveConstraint" in content:
    print("YES! ActiveConstraint is declared inside GameData.kt!")
    # Let's print the declaration
    idx = content.find("data class ActiveConstraint")
    print(content[idx:idx+350])
else:
    print("NO! ActiveConstraint is NOT declared in GameData.kt!")
