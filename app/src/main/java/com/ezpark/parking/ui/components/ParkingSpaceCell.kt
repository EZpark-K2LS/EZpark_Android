package com.ezpark.parking.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezpark.parking.R
import com.ezpark.parking.ui.theme.EzparkColors
import com.ezpark.parking.ui.theme.EzparkShapes
import com.ezpark.parking.ui.theme.EzparkSpacing
import com.ezpark.parking.ui.theme.EzparkTheme

enum class SpaceStatus { AVAILABLE, OCCUPIED, DISABLED }

private val CellWidth = 25.dp // spacing-scale exception: parking cell width per Figma node 19:1129
private val CellHeight = 45.dp // spacing-scale exception: parking cell height per Figma node 19:1129
private val DisabledIconSize = 10.dp // spacing-scale exception: disabled-overlay icon per Figma
private val BorderWidth = 1.dp // spacing-scale exception: hairline border per Figma

@Composable
fun ParkingSpaceCell(
    status: SpaceStatus,
    modifier: Modifier = Modifier,
) {
    val (bg, border) = when (status) {
        SpaceStatus.AVAILABLE -> EzparkColors.successLight to EzparkColors.successBorder
        SpaceStatus.OCCUPIED -> EzparkColors.gray20 to EzparkColors.gray60
        SpaceStatus.DISABLED -> EzparkColors.successLight to EzparkColors.successBorder
    }
    Box(
        modifier = modifier
            .size(width = CellWidth, height = CellHeight)
            .background(color = bg, shape = EzparkShapes.card)
            .border(width = BorderWidth, color = border, shape = EzparkShapes.card),
        contentAlignment = Alignment.Center,
    ) {
        if (status == SpaceStatus.DISABLED) {
            Icon(
                painter = painterResource(id = R.drawable.ic_disabled),
                contentDescription = "장애인 전용",
                tint = EzparkColors.gray100,
                modifier = Modifier.size(DisabledIconSize),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EzparkTheme {
        Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(EzparkSpacing.xs)) {
            ParkingSpaceCell(status = SpaceStatus.AVAILABLE)
            ParkingSpaceCell(status = SpaceStatus.OCCUPIED)
            ParkingSpaceCell(status = SpaceStatus.DISABLED)
        }
    }
}
