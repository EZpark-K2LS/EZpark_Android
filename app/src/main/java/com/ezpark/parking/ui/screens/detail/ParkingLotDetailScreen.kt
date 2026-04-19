package com.ezpark.parking.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ezpark.parking.data.stub.StubParkingLots
import com.ezpark.parking.ui.components.AvailabilityChip
import com.ezpark.parking.ui.components.DetailHeader
import com.ezpark.parking.ui.components.FloorOption
import com.ezpark.parking.ui.components.FloorSelector
import com.ezpark.parking.ui.components.ParkingLotCanvas
import com.ezpark.parking.ui.theme.EzparkColors
import com.ezpark.parking.ui.theme.EzparkSpacing
import com.ezpark.parking.ui.theme.EzparkTheme

@Composable
fun ParkingLotDetailScreen(
    lotId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lot = remember(lotId) { StubParkingLots.byId(lotId) ?: StubParkingLots.shinseong2 }
    val selectedFloorId = remember { mutableStateOf(lot.floors.last().id) }
    val selectedFloor = lot.floors.first { it.id == selectedFloorId.value }
    val availabilityText = "${selectedFloor.availableCount}대 주차가능"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(EzparkColors.gray10)
            .statusBarsPadding(),
    ) {
        DetailHeader(
            title = lot.name,
            onBack = onBack,
            onOptions = {},
        )

        Box(modifier = Modifier.fillMaxSize()) {
            ParkingLotCanvas(
                layout = selectedFloor.layout,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = EzparkSpacing.lg, vertical = EzparkSpacing.md),
            )

            FloorSelector(
                floors = lot.floors.map { FloorOption(it.id, it.label) },
                selectedId = selectedFloorId.value,
                onSelect = { selectedFloorId.value = it },
                selectedBadge = { AvailabilityChip(text = availabilityText) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(EzparkSpacing.xxl),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailPreview() {
    EzparkTheme {
        ParkingLotDetailScreen(lotId = "shinseong-2", onBack = {})
    }
}
