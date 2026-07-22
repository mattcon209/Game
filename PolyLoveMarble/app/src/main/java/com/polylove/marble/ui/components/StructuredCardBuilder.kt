package com.polylove.marble.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.game.*
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.theme.*

data class PromptSegment(
    val playerTag: String = "{player}",
    val statementText: String = ""
)

@Composable
fun StructuredCardBuilder(
    viewModel: GameViewModel,
    activeSpellbook: String,
    hapticFeedback: HapticFeedback,
    modifier: Modifier = Modifier
) {
    // Structured Segment Blocks - Clicking "Add New" adds a new player-tag & statement block!
    val segments = remember { mutableStateListOf<PromptSegment>(PromptSegment("{player}", "")) }
    
    var selectMidText by remember { mutableStateOf("for") }
    var selectDurationValue by remember { mutableStateOf(2) }
    var selectDurationUnit by remember { mutableStateOf("minutes") }
    var customMidInput by remember { mutableStateOf("") }
    
    var cardCategory by remember { mutableStateOf(TileType.TRUTH) }
    var cardSpice by remember { mutableStateOf(SpiceLevel.KINKY_LIGHT) }

    val finalMid = if (selectMidText == "Custom...") customMidInput else selectMidText
    
    // Synthesize the final template prompt block-by-block
    val synthesizedPrompt = buildString {
        segments.forEach { segment ->
            append(segment.playerTag)
            if (segment.statementText.isNotBlank()) {
                append(" ")
                append(segment.statementText.trim())
            }
            append(" ")
        }
        if (selectDurationUnit != "times" && finalMid.isNotBlank()) {
            append(finalMid)
            append(" ")
        }
        append(selectDurationValue)
        append(" ")
        append(selectDurationUnit)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Occult Structured Card Builder",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = GoldPrimary
        )
        Text(
            text = "Build a custom sentence program block-by-block. Always ends with a time or rep format:",
            fontSize = 9.sp,
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Live Preview Card
        Card(
            colors = CardDefaults.cardColors(containerColor = ObsidianBlack),
            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("LIVE PROMPT PREVIEW:", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                Text(
                    text = synthesizedPrompt,
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(0.9f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Render the sentence segments rows
            itemsIndexed(segments) { index, segment ->
                KinkyCard(
                    borderColor = CrimsonGlow.copy(alpha = 0.25f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Segment Block #${index + 1}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        if (segments.size > 1) {
                            IconButton(
                                onClick = {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    segments.removeAt(index)
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(Icons.Default.Delete, "Remove Block", tint = CrimsonGlow, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Player Tag Selector
                        var expandedTag by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.weight(1.2f)) {
                            Button(
                                onClick = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); expandedTag = true },
                                colors = ButtonDefaults.buttonColors(containerColor = ObsidianBlack),
                                contentPadding = PaddingValues(horizontal = 6.dp),
                                modifier = Modifier.height(38.dp).fillMaxWidth()
                            ) {
                                val tagLabel = when (segment.playerTag) {
                                    "{player}" -> "Active Player"
                                    "{target1}" -> "Partner 1"
                                    "{target2}" -> "Partner 2"
                                    "{all_others}" -> "All Others"
                                    "{choice_player}" -> "Choice Player"
                                    else -> segment.playerTag
                                }
                                Text(tagLabel, fontSize = 9.sp, color = Color.White, maxLines = 1)
                            }
                            DropdownMenu(expanded = expandedTag, onDismissRequest = { expandedTag = false }) {
                                listOf(
                                    Pair("Active Player", "{player}"),
                                    Pair("Partner 1", "{target1}"),
                                    Pair("Partner 2", "{target2}"),
                                    Pair("All Others", "{all_others}"),
                                    Pair("Choice Player", "{choice_player}")
                                ).forEach { (label, tag) ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = {
                                            segments[index] = segment.copy(playerTag = tag)
                                            expandedTag = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        // Action statement text input
                        TextField(
                            value = segment.statementText,
                            onValueChange = { txt ->
                                segments[index] = segment.copy(statementText = txt)
                            },
                            placeholder = { Text("e.g. must spank", fontSize = 11.sp) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = ObsidianBlack,
                                unfocusedContainerColor = ObsidianBlack,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(2f)
                                .height(42.dp)
                        )
                    }
                }
            }
            
            // Add new Segment Block / insert Tag button
            item {
                Button(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        segments.add(PromptSegment("{target1}", ""))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonGlow.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                ) {
                    Icon(Icons.Default.Add, "Add Segment", tint = GoldPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ADD BLOCK / INSERT TAG", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            // Suffix formatting & Card settings
            item {
                KinkyCard(borderColor = CrimsonGlow.copy(alpha = 0.25f)) {
                    Text("Mandatory Suffix Format Settings", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Duration Value select
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Duration Value:", fontSize = 9.sp, color = Color.White)
                            var expandedVal by remember { mutableStateOf(false) }
                            Box {
                                Button(
                                    onClick = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); expandedVal = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = ObsidianBlack),
                                    contentPadding = PaddingValues(horizontal = 6.dp),
                                    modifier = Modifier.height(36.dp).fillMaxWidth()
                                ) {
                                    Text(selectDurationValue.toString(), fontSize = 10.sp, color = Color.White)
                                }
                                DropdownMenu(expanded = expandedVal, onDismissRequest = { expandedVal = false }) {
                                    listOf(1, 2, 3, 5, 10, 15, 30, 45, 60).forEach { vl ->
                                        DropdownMenuItem(text = { Text(vl.toString()) }, onClick = { selectDurationValue = vl; expandedVal = false })
                                    }
                                }
                            }
                        }

                        // Suffix Unit select
                        Column(modifier = Modifier.weight(1.2f)) {
                            Text("Format Suffix:", fontSize = 9.sp, color = Color.White)
                            var expandedUnit by remember { mutableStateOf(false) }
                            Box {
                                Button(
                                    onClick = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); expandedUnit = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = ObsidianBlack),
                                    contentPadding = PaddingValues(horizontal = 6.dp),
                                    modifier = Modifier.height(36.dp).fillMaxWidth()
                                ) {
                                    Text(selectDurationUnit, fontSize = 10.sp, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                DropdownMenu(expanded = expandedUnit, onDismissRequest = { expandedUnit = false }) {
                                    listOf("seconds", "minutes", "turns", "times").forEach { un ->
                                        DropdownMenuItem(text = { Text(un) }, onClick = { selectDurationUnit = un; expandedUnit = false })
                                    }
                                }
                            }
                        }

                        // Category card link
                        Column(modifier = Modifier.weight(1.2f)) {
                            Text("Arcane Spell Theme:", fontSize = 9.sp, color = Color.White)
                            var expandedCat by remember { mutableStateOf(false) }
                            Box {
                                Button(
                                    onClick = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); expandedCat = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = ObsidianBlack),
                                    contentPadding = PaddingValues(horizontal = 6.dp),
                                    modifier = Modifier.height(36.dp).fillMaxWidth()
                                ) {
                                    Text(cardCategory.displayName, fontSize = 9.sp, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                DropdownMenu(expanded = expandedCat, onDismissRequest = { expandedCat = false }) {
                                    listOf(TileType.TRUTH, TileType.DARE, TileType.KINKY_LIGHT, TileType.TEASE_DENIAL, TileType.IMPACT_DOM, TileType.GROUP_BDSM).forEach { type ->
                                        DropdownMenuItem(text = { Text(type.displayName) }, onClick = { cardCategory = type; expandedCat = false })
                                    }
                                }
                            }
                        }

                        // Card Spice Level select
                        Column(modifier = Modifier.weight(1.2f)) {
                            Text("Spice Level:", fontSize = 9.sp, color = Color.White)
                            var expandedSpice by remember { mutableStateOf(false) }
                            Box {
                                Button(
                                    onClick = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); expandedSpice = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = ObsidianBlack),
                                    contentPadding = PaddingValues(horizontal = 6.dp),
                                    modifier = Modifier.height(36.dp).fillMaxWidth()
                                ) {
                                    Text(cardSpice.displayName.take(12), fontSize = 9.sp, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                DropdownMenu(expanded = expandedSpice, onDismissRequest = { expandedSpice = false }) {
                                    SpiceLevel.values().forEach { level ->
                                        DropdownMenuItem(text = { Text(level.displayName) }, onClick = { cardSpice = level; expandedSpice = false })
                                    }
                                }
                            }
                        }
                    }

                    if (selectDurationUnit != "times") {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Link Prefix Phrase (for timed formats):", fontSize = 9.sp, color = Color.White)
                        var expandedMid by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); expandedMid = true },
                                colors = ButtonDefaults.buttonColors(containerColor = ObsidianBlack),
                                modifier = Modifier.height(36.dp).fillMaxWidth()
                            ) {
                                Text(selectMidText, fontSize = 10.sp, color = Color.White)
                            }
                            DropdownMenu(expanded = expandedMid, onDismissRequest = { expandedMid = false }) {
                                listOf("for", "with", "using", "Custom...").forEach { md ->
                                    DropdownMenuItem(text = { Text(md) }, onClick = { selectMidText = md; expandedMid = false })
                                }
                            }
                        }
                        if (selectMidText == "Custom...") {
                            TextField(
                                value = customMidInput,
                                onValueChange = { customMidInput = it },
                                placeholder = { Text("e.g. during") },
                                colors = TextFieldDefaults.colors(focusedContainerColor = ObsidianBlack, unfocusedContainerColor = ObsidianBlack, focusedTextColor = Color.White),
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp).height(44.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                if (synthesizedPrompt.isNotBlank()) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    PromptDatabase.prompts.add(0, Prompt(
                        category = cardCategory,
                        spiceLevel = cardSpice,
                        template = synthesizedPrompt,
                        packName = activeSpellbook,
                        durationValue = selectDurationValue,
                        durationUnit = selectDurationUnit
                    ))
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = CrimsonGlow),
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
        ) {
            Text("SAVE STRUCTURED CARD", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }

        HorizontalDivider(color = CrimsonGlow.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 4.dp))

        Text(
            text = "Spellbook Deck Cards Manager (${PromptDatabase.prompts.count { it.packName == activeSpellbook }})", 
            fontSize = 11.sp, 
            color = GoldPrimary, 
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 2.dp)
        )

        // AUTO-SYNCD FILTERED LIST OF CARDS
        val filteredDisplayCards = PromptDatabase.prompts.filter {
            it.packName == activeSpellbook
        }

        LazyColumn(modifier = Modifier.weight(0.7f)) {
            itemsIndexed(filteredDisplayCards) { index, card ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Category: ${card.category.displayName} | Level: ${card.spiceLevel.displayName} | Tracker: ${card.durationValue} ${card.durationUnit}", fontSize = 8.sp, color = GoldPrimary)
                            Text(card.template, fontSize = 11.sp, color = Color.White)
                        }
                        IconButton(onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            PromptDatabase.prompts.remove(card)
                        }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, "Delete", tint = CrimsonGlow, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
