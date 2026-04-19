package com.ezpark.parking.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezpark.parking.ui.theme.EzparkSpacing
import com.ezpark.parking.ui.theme.EzparkTheme

@Composable
fun ParkingSpaceGrid(
    spaces: List<List<SpaceStatus>>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(EzparkSpacing.xs),
    ) {
        spaces.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(EzparkSpacing.xs),
            ) {
                row.forEach { status ->
                    ParkingSpaceCell(status = status)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EzparkTheme {
        ParkingSpaceGrid(
            spaces = listOf(
                listOf(SpaceStatus.AVAILABLE, SpaceStatus.OCCUPIED, SpaceStatus.AVAILABLE, SpaceStatus.DISABLED),
                listOf(SpaceStatus.OCCUPIED, SpaceStatus.AVAILABLE, SpaceStatus.OCCUPIED, SpaceStatus.AVAILABLE),
                listOf(SpaceStatus.AVAILABLE, SpaceStatus.AVAILABLE, SpaceStatus.OCCUPIED, SpaceStatus.OCCUPIED),
            ),
        )
    }
}
