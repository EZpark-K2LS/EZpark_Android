package com.ezpark.parking.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezpark.parking.ui.theme.EzparkColors
import com.ezpark.parking.ui.theme.EzparkShapes
import com.ezpark.parking.ui.theme.EzparkSpacing
import com.ezpark.parking.ui.theme.EzparkTheme
import com.ezpark.parking.ui.theme.EzparkTypography

data class FloorOption(val id: String, val label: String)

private val FloorButtonSize = 52.dp // spacing-scale exception: 52dp circle button per Figma spec

@Composable
fun FloorSelector(
    floors: List<FloorOption>,
    selectedId: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedBadge: (@Composable (FloorOption) -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(EzparkSpacing.sm),
        horizontalAlignment = Alignment.End,
    ) {
        floors.forEach { floor ->
            val isSelected = floor.id == selectedId
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(EzparkSpacing.md),
            ) {
                if (isSelected && selectedBadge != null) {
                    selectedBadge(floor)
                }
                Box(
                    modifier = Modifier
                        .size(FloorButtonSize)
                        .clip(EzparkShapes.floorButton)
                        .background(
                            color = if (isSelected) EzparkColors.gray100 else EzparkColors.gray40,
                            shape = EzparkShapes.floorButton,
                        )
                        .clickable { onSelect(floor.id) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = floor.label,
                        style = EzparkTypography.subtitle4,
                        color = EzparkColors.gray10,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EzparkTheme {
        FloorSelector(
            floors = listOf(
                FloorOption("b1", "B1"),
                FloorOption("b2", "B2"),
                FloorOption("b3", "B3"),
            ),
            selectedId = "b1",
            onSelect = {},
        )
    }
}
