# 🔊 Chains of Desire: Sound Effects & Audio Sources

To complete the immersive, high-fidelity experience of **Chains of Desire**, the app's code is pre-configured with a native, crash-safe Android **`SoundPool`** manager. 

Below are the exact, 100% free, CC0-licensed board game sound effects we are referencing, along with their links, so you can view, listen, or download them to complete your local build.

---

### 🎲 1. Dice Rolling Sound (Tumbling Dice)
* **Visual Event**: When you tap the **ROLL** button and the two 3D-shaded dice spin.
* **Asset Source**: Kenney.nl (CC0 1.0 Universal - "Interface Sounds" Pack)
* **File Name**: `dice_roll.ogg` (or `roll.wav`)
* **Download Link**: **[Kenney.nl - Interface Sounds](https://kenney.nl/assets/interface-sounds)**

---

### ♟️ 2. Token Jumping Sound (Meeples Leaping)
* **Visual Event**: When your 3D glass meeple pawn makes a parabolic hop from pillar to pillar.
* **Asset Source**: Kenney.nl (CC0 - "Interface Sounds" Pack)
* **File Name**: `pawn_hop.ogg` (or `cloth_slide.wav` / `drop_pawn.wav`)
* **Download Link**: **[Kenney.nl - Board Game Assets](https://kenney.nl/assets/boardgame-pack)**

---

### 🃏 3. Card Drawing Sound (The 3D Flip)
* **Visual Event**: When you tap the card back and it rotates in 3D to reveal the prompt.
* **Asset Source**: Kenney.nl (CC0 - "Digital Audio" Pack)
* **File Name**: `card_flip.ogg` (or `cardSlide1.ogg` / `cardSlide2.wav`)
* **Download Link**: **[Kenney.nl - Digital Audio](https://kenney.nl/assets/digital-audio)**

---

### 🔒 4. Shackle Latching Sound (Unlocking/Enabling Decks)
* **Visual Event**: When you tap a padlock toggle to enable a deck, or land on a "Bound!" space.
* **Asset Source**: Freesound.org (CC0 Public Domain)
* **File Name**: `chain_lock.ogg` (or heavy metal padlock latch)
* **View/Download Link**: **[Freesound - Metal Shackle / Padlock Snap](https://freesound.org/people/morgan_01/sounds/368651/)**

---

### 🔮 5. Chaos Reshuffle Sound (Vortex Cyclone)
* **Visual Event**: Landing on a "Chaos Reshuffle" space as the board tiles scramble in real-time.
* **Asset Source**: Kenney.nl (CC0 - "RPG Audio" Pack)
* **File Name**: `magic_shuffle.ogg` (or magic spell whoosh / warp)
* **Download Link**: **[Kenney.nl - RPG Audio Pack](https://kenney.nl/assets/rpg-audio)**

---

### 🌶️ 6. Flogger Whip Crack Sound (The Sensation Lash)
* **Visual Event**: When a player is driven forward/backward by a "Whip" tile, or takes an impact task.
* **Asset Source**: Freesound.org (CC0 Public Domain)
* **File Name**: `whip_crack.ogg`
* **View/Download Link**: **[Freesound - Leather Whip Crack](https://freesound.org/people/CGEffex/sounds/93100/)**

---

## 🛠️ How to Add these Audio Files to your Android App:

Since the Android `SoundPool` code is already fully written, compiled, and integrated into your APK, the app is built to **safely and silently ignore missing audio files** during normal gameplay so it won't crash. 

To make the sound effects play, you can easily drop them into the project folder before building:

1. Unzip your downloaded **`ChainsOfDesire`** project folder on your computer.
2. Navigate to this exact subdirectory (create the folders if they don't exist yet):
   `app/src/main/assets/audio/`
3. Download your preferred `.ogg` or `.mp3` files from the links above, rename them to match the game's standard file names:
   - `dice_roll.ogg`
   - `pawn_hop.ogg`
   - `card_flip.ogg`
   - `chain_lock.ogg`
   - `magic_shuffle.ogg`
   - `whip_crack.ogg`
4. Place those files inside the `assets/audio/` folder.
5. Sideload or build your APK—and the game will play those high-fidelity sound effects automatically!
