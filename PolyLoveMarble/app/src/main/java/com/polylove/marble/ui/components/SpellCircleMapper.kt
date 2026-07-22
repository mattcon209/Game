package com.polylove.marble.ui.components

import com.polylove.marble.R
import com.polylove.marble.game.*

object SpellCircleMapper {
    fun getSpellCircleResId(type: TileType, action: TileAction): Int {
        return when (action) {
            TileAction.PUNISHMENT -> R.drawable.spell_circle_01
            TileAction.SKIP_TURN -> R.drawable.spell_circle_02
            TileAction.MOVE_SPACES -> R.drawable.spell_circle_03
            TileAction.SWAP_POS -> R.drawable.spell_circle_04
            TileAction.ROLL_AGAIN -> R.drawable.spell_circle_05
            TileAction.BOARD_SHUFFLE -> R.drawable.spell_circle_06
            TileAction.DOUBLE_DARE -> R.drawable.spell_circle_07
            TileAction.CUSTOM_MESSAGE -> R.drawable.spell_circle_08
            TileAction.NORMAL_CARD -> when (type) {
                TileType.START -> R.drawable.spell_circle_09
                TileType.TRUTH -> R.drawable.spell_circle_10
                TileType.DARE -> R.drawable.spell_circle_11
                TileType.KINKY_LIGHT -> R.drawable.spell_circle_12
                TileType.TEASE_DENIAL -> R.drawable.spell_circle_13
                TileType.IMPACT_DOM -> R.drawable.spell_circle_14
                TileType.GROUP_BDSM -> R.drawable.spell_circle_15
                else -> R.drawable.spell_circle_16
            }
        }
    }
    
    fun getCustomSpellCircle(index: Int): Int {
        val clampedIndex = index.coerceIn(1, 45)
        return when (clampedIndex) {
            1 -> R.drawable.spell_circle_01
            2 -> R.drawable.spell_circle_02
            3 -> R.drawable.spell_circle_03
            4 -> R.drawable.spell_circle_04
            5 -> R.drawable.spell_circle_05
            6 -> R.drawable.spell_circle_06
            7 -> R.drawable.spell_circle_07
            8 -> R.drawable.spell_circle_08
            9 -> R.drawable.spell_circle_09
            10 -> R.drawable.spell_circle_10
            11 -> R.drawable.spell_circle_11
            12 -> R.drawable.spell_circle_12
            13 -> R.drawable.spell_circle_13
            14 -> R.drawable.spell_circle_14
            15 -> R.drawable.spell_circle_15
            16 -> R.drawable.spell_circle_16
            17 -> R.drawable.spell_circle_17
            18 -> R.drawable.spell_circle_18
            19 -> R.drawable.spell_circle_19
            20 -> R.drawable.spell_circle_20
            21 -> R.drawable.spell_circle_21
            22 -> R.drawable.spell_circle_22
            23 -> R.drawable.spell_circle_23
            24 -> R.drawable.spell_circle_24
            25 -> R.drawable.spell_circle_25
            26 -> R.drawable.spell_circle_26
            27 -> R.drawable.spell_circle_27
            28 -> R.drawable.spell_circle_28
            29 -> R.drawable.spell_circle_29
            30 -> R.drawable.spell_circle_30
            31 -> R.drawable.spell_circle_31
            32 -> R.drawable.spell_circle_32
            33 -> R.drawable.spell_circle_33
            34 -> R.drawable.spell_circle_34
            35 -> R.drawable.spell_circle_35
            36 -> R.drawable.spell_circle_36
            37 -> R.drawable.spell_circle_37
            38 -> R.drawable.spell_circle_38
            39 -> R.drawable.spell_circle_39
            40 -> R.drawable.spell_circle_40
            41 -> R.drawable.spell_circle_41
            42 -> R.drawable.spell_circle_42
            43 -> R.drawable.spell_circle_43
            44 -> R.drawable.spell_circle_44
            45 -> R.drawable.spell_circle_45
            else -> R.drawable.spell_circle_01
        }
    }
}
