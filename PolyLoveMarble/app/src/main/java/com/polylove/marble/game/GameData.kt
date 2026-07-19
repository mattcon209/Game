package com.polylove.marble.game

import androidx.compose.runtime.mutableStateListOf

import androidx.compose.ui.graphics.Color

// --- OCCULT / VAMPIRE CONFIGURATIONS ---

enum class SpiceLevel(val displayName: String, val description: String) {
    KINKY_LIGHT("Level 1", "Boundary negotiations, seductive queries, light physical contact."),
    TEASE_DENIAL("Level 2", "Blindfolds, restraint, anticipation, sensory deprivation."),
    IMPACT_DOM("Level 3", "Spankings, light impact sensation challenges, strict authority."),
    GROUP_BDSM("Level 4", "Multiplayer BDSM frames, shared circles, group restraint."),
    LEVEL_5("Level 5", "Advanced high-intensity sensations, absolute authority, and custom commands.")
}

enum class TileType(val displayName: String, val emoji: String, val colorHex: String, val isMotif: Boolean = false) {
    START("Portal of Summoning", "🔒", "#8B0000"),
    TRUTH("Rune of Whispers", "💬", "#7A7A8A"),
    DARE("Rune of Hellfire", "🔥", "#D32F2F"),
    KINKY_LIGHT("Rune of Sensation", "✨", "#5C1D8D"),
    TEASE_DENIAL("Rune of the Mask", "⛓️", "#D28EFF"),
    IMPACT_DOM("Rune of the Lash", "🌶️", "#B100CD"),
    GROUP_BDSM("Dungeon Summoning", "🔮", "#00FFCC"),
    
    // --- BOARD GAME MOTIFS ---
    PUNISHMENT("Pillar of Curses", "💢", "#FF007F", true),
    SKIP_TURN("Pillar of Bindings", "🚫", "#4E2F1D", true),
    MOVE_SPACES("Pillar of the Whip", "🌀", "#FFB703", true),
    SWAP_POS("Pillar of Collar Swap", "🔄", "#2EC4B6", true),
    ROLL_AGAIN("Pillar of Double Roll", "🎲", "#C5A059", true),
    BOARD_SHUFFLE("Pillar of Chaos", "🔮", "#C5A059", true)
}

// PROGRAMMABLE TILE ACTIONS
enum class TileAction(val displayName: String, val desc: String) {
    NORMAL_CARD("Standard Prompt Spell", "Draws one runic card from the active Spell Book based on the tile category."),
    DOUBLE_DARE("Double Lash Spell", "Forces the player to complete TWO cards back-to-back from the active Spell Book!"),
    PUNISHMENT("Direct Curse Spell", "No card prompt—immediately draws a randomized BDSM curse penalty."),
    SKIP_TURN("Shackle Binding Spell", "Saves an active restraint constraint. The landing player skips their next turn."),
    MOVE_SPACES("Whip Lash Displacement Spell", "Drives the player forward or backward by X spaces. Cascade-triggers landing."),
    SWAP_POS("Collar Swap Spell", "Swaps the board positions of the active player and a random other player."),
    ROLL_AGAIN("Double Roll Blessing Spell", "Grants the active player an immediate, free bonus roll."),
    BOARD_SHUFFLE("Chaos Reshuffle Curse Spell", "Instantly reshuffles and regenerates all board tiles in real-time when landed on!"),
    CUSTOM_MESSAGE("Custom Spell Command", "Runs a specific BDSM command or custom party rule typed directly onto this space.")
}

data class Player(
    val id: Int,
    var name: String,
    val color: Color,
    var position: Int = 0,
    var lapsCompleted: Int = 0,
    var tasksCompleted: Int = 0,
    var punishmentsTaken: Int = 0,
    var isBound: Boolean = false
)

// FULLY PROGRAMMABLE DYNAMIC BOARD TILE
data class Tile(
    val index: Int,
    var label: String,
    var action: TileAction,
    var cardCategory: TileType = TileType.TRUTH,
    var moveSpacesValue: Int = 0,
    var customMessageText: String = "",
    var emoji: String = "⛓️"
)

data class Prompt(
    val category: TileType,
    val spiceLevel: SpiceLevel,
    val template: String,
    val packName: String = "Base Deck"
)

// --- PROMPT TEMPLATE RESOLVER ---

object PromptResolver {
    private val SENSITIVE_AREAS = listOf("thighs", "inner thighs", "back", "buttocks", "shoulders", "palms", "neck")
    private val INTENSITIES = listOf("gentle", "moderate", "firm", "teasing", "seductive")

    fun resolve(
        template: String,
        currentPlayer: Player,
        allPlayers: List<Player>
    ): String {
        val otherPlayers = allPlayers.filter { it.id != currentPlayer.id }
        if (otherPlayers.isEmpty()) return template.replace("{player}", currentPlayer.name)

        val shuffledOthers = otherPlayers.shuffled()
        val target1 = shuffledOthers.first()
        val target2 = if (shuffledOthers.size > 1) shuffledOthers[1] else target1

        var resolved = template
            .replace("{player}", currentPlayer.name)
            .replace("{target1}", target1.name)
            .replace("{target2}", target2.name)
            .replace("{choice_player}", "the player of your choice")
            .replace("{all_others}", if (otherPlayers.size > 1) "all other players" else otherPlayers.first().name)
            .replace("{area}", SENSITIVE_AREAS.random())
            .replace("{intensity}", INTENSITIES.random())

        if (otherPlayers.size >= 2) {
            resolved = resolved.replace("{others_pair}", "${target1.name} and ${target2.name}")
        } else {
            resolved = resolved.replace("{others_pair}", "${target1.name} and anyone else")
        }

        return resolved
    }
}

// --- KINKY DATABASE ---

object PromptDatabase {
    val prompts = mutableStateListOf(
        // === PACK 1: "Base Deck" ===
        Prompt(
            TileType.TRUTH, SpiceLevel.KINKY_LIGHT,
            "Truth: Tell {target1} your absolute biggest kinky fantasy involving them.",
            "Base Deck"
        ),
        Prompt(
            TileType.TRUTH, SpiceLevel.KINKY_LIGHT,
            "Truth: What is your favorite safe-word, and what is the story behind why you chose it?",
            "Base Deck"
        ),
        Prompt(
            TileType.TRUTH, SpiceLevel.KINKY_LIGHT,
            "Truth: Have you ever wanted to try being completely submissive to {target1}?",
            "Base Deck"
        ),
        Prompt(
            TileType.TRUTH, SpiceLevel.KINKY_LIGHT,
            "Truth: What is one boundary you are curious about pushing tonight with {target1}?",
            "Base Deck"
        ),
        Prompt(
            TileType.DARE, SpiceLevel.KINKY_LIGHT,
            "Dare: Whisper a highly detailed command into {target1}'s ear. They must follow it during their next turn.",
            "Base Deck"
        ),
        Prompt(
            TileType.DARE, SpiceLevel.KINKY_LIGHT,
            "Dare: Seductively describe the last kinky dream you had to {all_others}.",
            "Base Deck"
        ),
        Prompt(
            TileType.DARE, SpiceLevel.KINKY_LIGHT,
            "Dare: Crawl on your hands and knees around the circle, stopping to brush your head against {target1}'s leg like a loyal pet.",
            "Base Deck"
        ),

        // === PACK 2: "Sensory & Tease" ===
        Prompt(
            TileType.TEASE_DENIAL, SpiceLevel.TEASE_DENIAL,
            "Blindfold yourself (or close your eyes). Let {target1} trace three different objects along your skin. Guess them correctly or take a punishment!",
            "Sensory & Tease"
        ),
        Prompt(
            TileType.TEASE_DENIAL, SpiceLevel.TEASE_DENIAL,
            "Blindfold {target1}. Give them 10 {intensity} feather-light kisses on their {area}, but deny them a kiss on the lips.",
            "Sensory & Tease"
        ),
        Prompt(
            TileType.TEASE_DENIAL, SpiceLevel.TEASE_DENIAL,
            "Sit completely still. Let {target1} slowly run their fingers along your {area} for 1 minute. You must remain silent and cannot touch them back.",
            "Sensory & Tease"
        ),
        Prompt(
            TileType.TEASE_DENIAL, SpiceLevel.TEASE_DENIAL,
            "Feed {target1} a sip of water or a small snack in a highly dominant, slow-paced manner, denying them until they ask politely.",
            "Sensory & Tease"
        ),

        // === PACK 3: "Bondage & Restraints" ===
        Prompt(
            TileType.KINKY_LIGHT, SpiceLevel.KINKY_LIGHT,
            "Let {target1} inspect you closely and negotiate one light BDSM roleplay scenario to try for 2 minutes.",
            "Bondage & Restraints"
        ),
        Prompt(
            TileType.KINKY_LIGHT, SpiceLevel.KINKY_LIGHT,
            "Gently collar {target1} with your hands or a soft piece of clothing, and make them repeat: 'I belong to this circle tonight.'",
            "Bondage & Restraints"
        ),
        Prompt(
            TileType.KINKY_LIGHT, SpiceLevel.KINKY_LIGHT,
            "Tie {target1}'s wrists together using a tie/scarf (or hold them behind their back) for the next 2 rounds.",
            "Bondage & Restraints"
        ),

        // === PACK 4: "Impact Play" ===
        Prompt(
            TileType.IMPACT_DOM, SpiceLevel.IMPACT_DOM,
            "Bend over {target1}'s lap. Let them give you 5 {intensity} spanks with an open hand on your bare or clothed buttocks.",
            "Impact Play"
        ),
        Prompt(
            TileType.IMPACT_DOM, SpiceLevel.IMPACT_DOM,
            "Command {target1} to kneel before you. Put your foot on their knee or thigh and explain why they have been a good submissive tonight.",
            "Impact Play"
        ),
        Prompt(
            TileType.IMPACT_DOM, SpiceLevel.IMPACT_DOM,
            "Hand {target1} a light impact tool (paddle, flogger, hairbrush, or hand) and let them strike your {area} 6 times at their preferred safe intensity.",
            "Impact Play"
        ),
        Prompt(
            TileType.IMPACT_DOM, SpiceLevel.IMPACT_DOM,
            "Remove one article of clothing, or let {target1} lightly pinch/bite your {area}.",
            "Impact Play"
        ),

        // === PACK 5: "Polyamorous Group Scenes" ===
        Prompt(
            TileType.GROUP_BDSM, SpiceLevel.GROUP_BDSM,
            "Group Restraint! Let {target1} and {target2} bind your hands behind your back or to a chair for the next 2 turns.",
            "Polyamorous Group Scenes"
        ),
        Prompt(
            TileType.GROUP_BDSM, SpiceLevel.GROUP_BDSM,
            "Command {target1} and {target2} to cuddle or caress you, while you decide who is doing a better job.",
            "Polyamorous Group Scenes"
        ),
        Prompt(
            TileType.GROUP_BDSM, SpiceLevel.GROUP_BDSM,
            "Polyamorous Worship: Kneel in the center. Let {all_others} take turns whispering a submissive praise or dominant command to you.",
            "Polyamorous Group Scenes"
        ),
        Prompt(
            TileType.GROUP_BDSM, SpiceLevel.GROUP_BDSM,
            "Let {target1} and {target2} lock your collar or shackles. You must stay seated next to them, holding hands, for the next 3 rounds.",
            "Polyamorous Group Scenes"
        ),
        Prompt(
            TileType.DARE, SpiceLevel.LEVEL_5,
            "Dare: Let {target1} tie your hands behind your back for the next 2 turns. If you refuse, take a curse!",
            "Base Deck"
        ),
        Prompt(
            TileType.TRUTH, SpiceLevel.LEVEL_5,
            "Truth: Tell {target1} about your absolute deepest, most intense submissive or dominant desire that you have never spoken aloud before.",
            "Base Deck"
        )
    )

    val punishments = mutableStateListOf(
        "Kneel on the floor in the corner of the room for 1 full round without speaking.",
        "Let {target1} administer 10 firm spanks to your clothed backside.",
        "Let {target1} put a blindfold on you and leave it on for the next 2 turns around the board.",
        "Remove an article of clothing. If you are already down to your underwear, perform 10 submissive squats.",
        "Drink a shot/sip of your drink from {target1}'s hand while kneeling on the floor.",
        "Let {target1} write a kinky word of their choice on your collarbone or forehead with a pen or lipstick. It must stay there for the rest of the game.",
        "Place your hands behind your head and let {target1} tickle or tease your {area} for 30 seconds. You must endure it without moving your hands.",
        "Let {all_others} decide on a consensual, safe BDSM punishment of their choice for you right now."
    )

    fun getPromptsForLevels(levels: Set<SpiceLevel>, activePacks: Set<String>): List<Prompt> {
        return prompts.filter { it.spiceLevel in levels && it.packName in activePacks }
    }
}

// --- DYNAMIC BOARD SPAWNER (BASED ON CHOSEN PACKS & RANDOMIZATION!) ---

object BoardCreator {
    // Generates a fully tailored loop board dynamically based on size AND selected themes/packs!
    // If randomizeBoard is enabled, the tiles are completely shuffled at game start!
    fun createBoardForPacks(gridSize: Int, selectedPacks: Set<String>, randomizeBoard: Boolean = false): List<Tile> {
        val totalTiles = 4 * (gridSize - 1)
        val tiles = ArrayList<Tile>()
        
        // Match tile categories with checked theme packs
        val matchingTypes = mutableListOf<TileType>()
        if (selectedPacks.contains("Base Deck")) {
            matchingTypes.add(TileType.TRUTH)
            matchingTypes.add(TileType.DARE)
        }
        if (selectedPacks.contains("Sensory & Tease")) {
            matchingTypes.add(TileType.TEASE_DENIAL)
        }
        if (selectedPacks.contains("Bondage & Restraints")) {
            matchingTypes.add(TileType.KINKY_LIGHT)
        }
        if (selectedPacks.contains("Impact Play")) {
            matchingTypes.add(TileType.IMPACT_DOM)
        }
        if (selectedPacks.contains("Polyamorous Group Scenes")) {
            matchingTypes.add(TileType.GROUP_BDSM)
        }
        
        val hasCustomPacks = selectedPacks.any { it !in listOf("Base Deck", "Sensory & Tease", "Bondage & Restraints", "Impact Play", "Polyamorous Group Scenes") }
        if (hasCustomPacks || matchingTypes.isEmpty()) {
            matchingTypes.add(TileType.TRUTH)
            matchingTypes.add(TileType.DARE)
            matchingTypes.add(TileType.KINKY_LIGHT)
        }
        
        for (i in 0 until totalTiles) {
            if (i == 0) {
                tiles.add(Tile(0, "START", TileAction.NORMAL_CARD, TileType.START, emoji = "🔒"))
                continue
            }
            
            // Sprinkle standard board game motifs evenly around the perimeter
            val tile = when {
                i % 6 == 0 -> Tile(i, "Punish", TileAction.PUNISHMENT, emoji = "💢")
                i % 7 == 0 -> Tile(i, "Bound!", TileAction.SKIP_TURN, emoji = "🚫")
                i == 3 || i == 11 -> Tile(i, "Move", TileAction.MOVE_SPACES, moveSpacesValue = if (i % 2 == 0) 2 else -1, emoji = "🌀")
                i == 5 || i == 15 -> Tile(i, "Collar Swap", TileAction.SWAP_POS, emoji = "🔄")
                i == 9 -> Tile(i, "Roll x2", TileAction.ROLL_AGAIN, emoji = "🎲")
                
                // EXCLUSIVE REQUESTED: Shuffle Board Tile Space Motif!
                i == 17 -> Tile(i, "Shuffle", TileAction.BOARD_SHUFFLE, emoji = "🔮")
                
                // Double Dare layout
                i == 2 || i == 8 || i == 14 -> {
                    val cat = matchingTypes[i % matchingTypes.size]
                    Tile(i, "Double Card", TileAction.DOUBLE_DARE, cardCategory = cat, emoji = "🔥")
                }
                
                // Normal theme cards matching selected expansion packs!
                else -> {
                    val chosenType = matchingTypes[i % matchingTypes.size]
                    val label = when (chosenType) {
                        TileType.TRUTH -> "Truth"
                        TileType.DARE -> "Dare"
                        TileType.KINKY_LIGHT -> "Light Kink"
                        TileType.TEASE_DENIAL -> "Tease/Deny"
                        TileType.IMPACT_DOM -> "Impact/Dom"
                        TileType.GROUP_BDSM -> "Group Scene"
                        else -> "Card"
                    }
                    Tile(i, label, TileAction.NORMAL_CARD, cardCategory = chosenType, emoji = chosenType.emoji)
                }
            }
            tiles.add(tile)
        }
        
        // EXCLUSIVE REQUESTED: Shuffles and randomizes board tiles on game start if enabled!
        if (randomizeBoard && tiles.size > 1) {
            val start = tiles.first()
            val rest = tiles.subList(1, tiles.size).shuffled()
            val shuffledBoard = ArrayList<Tile>()
            shuffledBoard.add(start)
            rest.forEachIndexed { index, t ->
                shuffledBoard.add(t.copy(index = index + 1))
            }
            return shuffledBoard
        }
        
        return tiles
    }

    // Dynamic grid coordinate mapping for ANY size loop (S x S grid)
    fun getGridCoordinates(index: Int, gridSize: Int): Pair<Int, Int> {
        val maxIndex = 4 * (gridSize - 1)
        val normIndex = index % maxIndex
        
        return when {
            normIndex < gridSize -> Pair(normIndex, 0)
            normIndex < 2 * gridSize - 1 -> Pair(gridSize - 1, normIndex - (gridSize - 1))
            normIndex < 3 * gridSize - 2 -> Pair((3 * gridSize - 3) - normIndex, gridSize - 1)
            else -> Pair(0, maxIndex - normIndex)
        }
    }
}
