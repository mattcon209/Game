file_path = "/home/user/PolyLoveMarble/app/src/main/java/com/polylove/marble/game/GameData.kt"

with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

content = content.replace("\r\n", "\n")

if "fun getPunishmentsForSpellbook" in content:
    print("YES! getPunishmentsForSpellbook is in GameData.kt!")
elif "fun getPunishmentsForBooklet" in content:
    print("NO! getPunishmentsForBooklet is in GameData.kt! Changing it to getPunishmentsForSpellbook...")
    content = content.replace("fun getPunishmentsForBooklet", "fun getPunishmentsForSpellbook")
    with open(file_path, "w", encoding="utf-8") as f:
        f.write(content)
    print("SUCCESS! Renamed to getPunishmentsForSpellbook!")
else:
    print("Neither method was found! Let's inspect PromptDatabase...")
    idx = content.find("object PromptDatabase")
    if idx != -1:
        print(content[idx:idx+400])
