package com.polylove.marble.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.*
import com.polylove.marble.game.*
import com.polylove.marble.ui.theme.*

// --- SCREEN STATES ---
sealed class GameScreen {
    object Setup : GameScreen()
    object Board : GameScreen()
    object Win : GameScreen()
    object Editor : GameScreen()
}

// --- CRASH-SAFE NATIVE SOUND MANAGER ---
class KinkySoundManager(context: Context) {
    private val soundPool: SoundPool
    private val sounds = HashMap<String, Int>()
    
    init {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(attrs)
            .build()
            
        // Load sounds from assets folder (absorbs failures silently if files aren't added yet!)
        try {
            sounds["roll"] = soundPool.load(context.assets.openFd("audio/intro_welcome.mp3"), 1)
            sounds["hop"] = soundPool.load(context.assets.openFd("audio/shackles_skip.mp3"), 1)
            sounds["flip"] = soundPool.load(context.assets.openFd("audio/punishment_drawn.mp3"), 1)
            sounds["lock"] = soundPool.load(context.assets.openFd("audio/shackles_skip.mp3"), 1)
            sounds["shuffle"] = soundPool.load(context.assets.openFd("audio/chaos_shuffle.mp3"), 1)
            sounds["whip"] = soundPool.load(context.assets.openFd("audio/punishment_drawn.mp3"), 1)
        } catch (e: Exception) {
            // Silently swallow missing assets during compilation!
        }
    }
    
    fun play(sound: String) {
        val soundId = sounds[sound] ?: return
        if (soundId != 0) {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }
}

// --- CENTRAL STATE HOLDER ---
class GameViewModel {
    var currentScreen by mutableStateOf<GameScreen>(GameScreen.Setup)
    
    // Players (Role-Neutral Default Names!)
    val players = mutableStateListOf<Player>(
        Player(1, "Alex", GemstoneColors[0].color),
        Player(2, "Sam", GemstoneColors[4].color),
        Player(3, "Charlie", GemstoneColors[5].color)
    )
    
    // Game Settings
    var gridSize by mutableStateOf(5) // 4 (12 tiles), 5 (16 tiles), 6 (20 tiles)
    var isInfiniteMode by mutableStateOf(false)
    var targetLaps by mutableStateOf(2)
    var isBoardRandomized by mutableStateOf(true)
    
    val selectedSpiceLevels = mutableStateListOf<SpiceLevel>(
        SpiceLevel.KINKY_LIGHT,
        SpiceLevel.TEASE_DENIAL,
        SpiceLevel.IMPACT_DOM,
        SpiceLevel.GROUP_BDSM,
        SpiceLevel.LEVEL_5
    )

    // EXPANSION PACKS
    val availableSpellbooks = mutableStateListOf<String>(
        "Base Deck",
        "Sensory & Tease",
        "Bondage & Restraints",
        "Impact Play",
        "Polyamorous Group Scenes"
    )
    val selectedSpellbooks = mutableStateListOf<String>(
        "Base Deck",
        "Sensory & Tease",
        "Bondage & Restraints",
        "Impact Play",
        "Polyamorous Group Scenes"
    )
    
    // Game Loop States (EXCLUSIVELY REQUESTED: 2d6 Double Dice!)
    var currentPlayerIndex by mutableStateOf(0)
    var diceValue1 by mutableStateOf(1)
    var diceValue2 by mutableStateOf(1)
    var isRolling by mutableStateOf(false)
    var isTokenMoving by mutableStateOf(false) // Blocks controls during active walk animation
    var animationStatusText by mutableStateOf("") // Informative suspense tags during walk
    val board = mutableStateListOf<Tile>()
    
    val inactivePillars = mutableStateListOf<Tile>(
        Tile(100, "Sensory Feast", TileAction.NORMAL_CARD, TileType.TEASE_DENIAL, emoji = "⛓️"),
        Tile(101, "Shipwreck", TileAction.SKIP_TURN, TileType.PUNISHMENT, emoji = "🚫"),
        Tile(102, "Lover's Leap", TileAction.MOVE_SPACES, TileType.KINKY_LIGHT, moveSpacesValue = 3, emoji = "🌀"),
        Tile(103, "Collar Exchange", TileAction.SWAP_POS, TileType.SWAP_POS, emoji = "🔄"),
        Tile(104, "Double Whipping", TileAction.DOUBLE_DARE, TileType.IMPACT_DOM, emoji = "🔥"),
        Tile(105, "Chaos Shuffle Space", TileAction.BOARD_SHUFFLE, TileType.BOARD_SHUFFLE, emoji = "🔮")
    )
    
    val vetoedKeywords = mutableStateListOf<String>()
    var gameSpeedMultiplier by mutableStateOf(1f)
    var activeSpellbook by mutableStateOf<String?>(null)
    var isSessionActive by mutableStateOf(false)
    
    val activeConstraints = mutableStateListOf<ActiveConstraint>()
    val disabledPrompts = mutableStateListOf<Prompt>()
    val disabledPunishments = mutableStateListOf<String>()
    var currentPromptDurationValue by mutableStateOf(0)
    var currentPromptDurationUnit by mutableStateOf("")
    
    // Popup constraint alerts
    var showConstraintAlert by mutableStateOf(false)
    var constraintAlertTitle by mutableStateOf("")
    var constraintAlertText by mutableStateOf("")
    
    // EXCLUSIVE REQUESTED: HIGH-FIDELITY ACTIVE TOKEN JUMP ANIMATION STATES
    var animatedPlayerIndex by mutableStateOf(-1) // ID of player meeple currently jumping
    var animatedPlayerSourceTile by mutableStateOf(0)
    var animatedPlayerTargetTile by mutableStateOf(0)
    var animatedPlayerStepProgress by mutableStateOf(0f) // 0.0f to 1.0f of current step
    
    // Card Modal Dialogue States
    var showCardDialog by mutableStateOf(false)
    var cardDialogType by mutableStateOf("PROMPT") // "PROMPT", "PUNISHMENT", "MOTIF", "DOUBLE_CHALLENGE"
    var currentPromptText by mutableStateOf("")
    var currentPromptCategory by mutableStateOf(TileType.TRUTH)
    var currentPromptSpice by mutableStateOf(SpiceLevel.KINKY_LIGHT)
    var currentPromptPack by mutableStateOf("Base Deck")
    var motifTitle by mutableStateOf("")
    var triggerNextTurnOnClose by mutableStateOf(true)
    
    // Double challenge (Draw 2 Card space) tracking
    var isDoubleChallenge by mutableStateOf(false)
    var doubleChallengeStage by mutableStateOf(1) // 1 or 2
    var doubleChallengeCategory by mutableStateOf(TileType.DARE)
    
    // Setup UI helpers
    var activeColorPickerIndex by mutableStateOf(-1) // Keeps track of which player has color picker open!
    
    // Editor Overhaul State Helpers
    var selectedEditorPackName by mutableStateOf("Base Deck") // Active pack inside Workshop
    
    // Win Screen State
    var winnerName by mutableStateOf("")
    var victorySummaryText by mutableStateOf("")

    init {
        regenerateBoard()
    }

    fun regenerateBoard() {
        board.clear()
        board.addAll(BoardCreator.createBoardForPacks(gridSize, selectedSpellbooks.toSet(), isBoardRandomized))
    }

    fun shuffleActiveBoard() {
        val size = board.size
        if (size <= 1) return
        
        val startTile = board.first()
        val restOfTiles = board.subList(1, size).shuffled()
        
        board.clear()
        board.add(startTile)
        restOfTiles.forEachIndexed { index, tile ->
            board.add(tile.copy(index = index + 1))
        }
    }

    fun addPlayer() {
        if (players.size < 8) {
            val nextId = (players.maxOfOrNull { it.id } ?: 0) + 1
            val nextColor = GemstoneColors[players.size % GemstoneColors.size].color
            players.add(Player(nextId, "Kinkster $nextId", nextColor))
        }
    }

    fun removePlayer(player: Player) {
        if (players.size > 2) {
            players.remove(player)
        }
    }

    fun toggleSpiceLevel(level: SpiceLevel) {
        if (selectedSpiceLevels.contains(level)) {
            if (selectedSpiceLevels.size > 1) {
                selectedSpiceLevels.remove(level)
            }
        } else {
            selectedSpiceLevels.add(level)
        }
    }

    fun togglePack(packName: String) {
        if (selectedSpellbooks.contains(packName)) {
            if (selectedSpellbooks.size > 1) {
                selectedSpellbooks.remove(packName)
            }
        } else {
            selectedSpellbooks.add(packName)
        }
        regenerateBoard()
    }

    fun resetGame() {
        players.forEach {
            it.position = 0
            it.lapsCompleted = 0
            it.tasksCompleted = 0
            it.punishmentsTaken = 0
            it.isBound = false
        }
        currentPlayerIndex = 0
        currentScreen = GameScreen.Setup
        showCardDialog = false
        isDoubleChallenge = false
        isTokenMoving = false
        animatedPlayerIndex = -1
        winnerName = ""
        activeConstraints.clear()
    }

    // --- PROGRAMMABLE TILE ACTION TRIGGER ENGINE ---
    fun handleTileActionTrigger(player: Player, tile: Tile, soundManager: KinkySoundManager, onCascade: () -> Unit = {}) {
        triggerNextTurnOnClose = true
        isDoubleChallenge = false
        
        // Reset current prompt duration value so old constraints don't carry over
        currentPromptDurationValue = 0
        currentPromptDurationUnit = ""
        
        when (tile.action) {
            TileAction.NORMAL_CARD -> {
                triggerStandardCard(player, tile.cardCategory)
                soundManager.play("flip")
            }
            
            TileAction.DOUBLE_DARE -> {
                isDoubleChallenge = true
                doubleChallengeStage = 1
                doubleChallengeCategory = tile.cardCategory
                
                triggerStandardCard(player, tile.cardCategory)
                soundManager.play("flip")
                
                cardDialogType = "DOUBLE_CHALLENGE"
                motifTitle = "🔥 DOUBLE CHALLENGE (Card 1 of 2) 🔥"
            }
            
            TileAction.PUNISHMENT -> {
                cardDialogType = "PUNISHMENT"
                val rawPunishment = PromptDatabase.getPunishmentsForSpellbook(activeSpellbook ?: "Base Deck").random()
                currentPromptText = PromptResolver.resolve(rawPunishment, player, players.toList())
                currentPromptCategory = TileType.PUNISHMENT
                showCardDialog = true
                soundManager.play("whip")
            }
            
            TileAction.SKIP_TURN -> {
                cardDialogType = "MOTIF"
                motifTitle = "BOUND IN SHACKLES! 🚫"
                currentPromptText = "${player.name} landed on a restriction space! Your next turn is skipped so you can stay tied up."
                player.isBound = true
                currentPromptCategory = TileType.SKIP_TURN
                showCardDialog = true
                soundManager.play("lock")
            }
            
            TileAction.MOVE_SPACES -> {
                cardDialogType = "MOTIF"
                val moveVal = tile.moveSpacesValue
                val dir = if (moveVal > 0) "forward" else "backward"
                motifTitle = "WHIP CRACK! 🌀"
                currentPromptText = "Lash! ${player.name} is driven $dir ${kotlin.math.abs(moveVal)} space(s)!"
                currentPromptCategory = TileType.MOVE_SPACES
                triggerNextTurnOnClose = false
                showCardDialog = true
                soundManager.play("whip")
                onCascade()
            }
            
            TileAction.SWAP_POS -> {
                cardDialogType = "MOTIF"
                motifTitle = "COLLAR SWAP 🔄"
                val otherPlayers = players.filter { it.id != player.id }
                if (otherPlayers.isNotEmpty()) {
                    val swapTarget = otherPlayers.random()
                    val tempPos = player.position
                    player.position = swapTarget.position
                    swapTarget.position = tempPos
                    currentPromptText = "${player.name} swaps positions with ${swapTarget.name}! You are now on Tile ${player.position}."
                } else {
                    currentPromptText = "No other active partners to swap with!"
                }
                currentPromptCategory = TileType.SWAP_POS
                showCardDialog = true
                soundManager.play("lock")
            }
            
            TileAction.ROLL_AGAIN -> {
                cardDialogType = "MOTIF"
                motifTitle = "EXTRA LASH / DOUBLE ROLL 🎲"
                currentPromptText = "${player.name} enjoys this round so much they receive an immediate extra roll!"
                currentPromptCategory = TileType.ROLL_AGAIN
                triggerNextTurnOnClose = false
                showCardDialog = true
                soundManager.play("roll")
            }
            
            TileAction.BOARD_SHUFFLE -> {
                cardDialogType = "MOTIF"
                motifTitle = "🔮 CHAOS SHUFFLE! 🔮"
                currentPromptText = "CHAOS STORM! ${player.name} has unlocked the handcuffs of entropy! The entire board has instantly shuffled and regenerated in real-time!"
                currentPromptCategory = TileType.BOARD_SHUFFLE
                triggerNextTurnOnClose = true
                showCardDialog = true
                soundManager.play("shuffle")
                shuffleActiveBoard()
            }
            
            TileAction.CUSTOM_MESSAGE -> {
                cardDialogType = "MOTIF"
                motifTitle = tile.label.uppercase()
                currentPromptText = PromptResolver.resolve(tile.customMessageText.ifBlank { "Do a light BDSM action, or take a sip!" }, player, players.toList())
                currentPromptCategory = TileType.KINKY_LIGHT
                showCardDialog = true
                soundManager.play("flip")
            }
        }
    }
    
    private fun triggerStandardCard(player: Player, category: TileType) {
        val validPrompts = PromptDatabase.getPromptsForLevels(
            selectedSpiceLevels.toSet(),
            selectedSpellbooks.toSet()
        ).filter { it.category == category }
         .filter { it !in disabledPrompts } // SKIP DISABLED INDIVIDUAL SPELLS
         .filter { prompt ->
             vetoedKeywords.none { vetoWord ->
                 prompt.template.contains(vetoWord, ignoreCase = true)
             }
         }
        
        val selectedPrompt = if (validPrompts.isNotEmpty()) {
            validPrompts.random()
        } else {
            Prompt(category, SpiceLevel.KINKY_LIGHT, "Truth: Tell {target1} one kinky fantasy or boundary you enjoy.", "Base Deck")
        }
        
        currentPromptText = PromptResolver.resolve(selectedPrompt.template, player, players.toList())
        currentPromptCategory = selectedPrompt.category
        currentPromptSpice = selectedPrompt.spiceLevel
        currentPromptPack = selectedPrompt.packName
        currentPromptDurationValue = selectedPrompt.durationValue
        currentPromptDurationUnit = selectedPrompt.durationUnit
        cardDialogType = "PROMPT"
        triggerNextTurnOnClose = true
        showCardDialog = true
    }
}
