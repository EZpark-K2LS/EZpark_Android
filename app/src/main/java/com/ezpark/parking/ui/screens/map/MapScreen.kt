package com.ezpark.parking.ui.screens.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.ezpark.parking.R
import com.ezpark.parking.data.model.ParkingLot
import com.ezpark.parking.data.stub.StubParkingLots
import com.ezpark.parking.ui.components.MapMarker
import com.ezpark.parking.ui.components.OsmMap
import com.ezpark.parking.ui.components.ParkingInfoCard
import com.ezpark.parking.ui.components.RecentSearchList
import com.ezpark.parking.ui.components.SearchBar
import com.ezpark.parking.ui.components.StatData
import com.ezpark.parking.ui.theme.EzparkColors
import com.ezpark.parking.ui.theme.EzparkShapes
import com.ezpark.parking.ui.theme.EzparkSpacing
import com.ezpark.parking.ui.theme.EzparkTheme
import com.ezpark.parking.ui.theme.EzparkTypography

private val RecentSearches = listOf("신성동 제2 주차장", "신성동 제1 주차장")
private val FabSize = 48.dp // spacing-scale exception: floating action button size
private val FabSpacing = 220.dp // spacing-scale exception: bottom offset so stacked controls clear the ParkingInfoCard

@Composable
fun MapScreen(
    onNavigateToDetail: (lotId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    var searchFocused by remember { mutableStateOf(false) }
    var selectedLotId by remember { mutableStateOf(StubParkingLots.shinseong2.id) }
    var moveToCurrentTrigger by remember { mutableStateOf(0) }
    var zoomInTrigger by remember { mutableStateOf(0) }
    var zoomOutTrigger by remember { mutableStateOf(0) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val featuredLot: ParkingLot = remember(selectedLotId) {
        StubParkingLots.byId(selectedLotId) ?: StubParkingLots.shinseong2
    }

    val results by remember(query) {
        derivedStateOf { StubParkingLots.search(query) }
    }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { perms ->
        hasLocationPermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (hasLocationPermission) moveToCurrentTrigger++
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            )
        }
    }

    fun dismissSearch() {
        searchFocused = false
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    val showOverlay = searchFocused

    val mapMarkers: List<MapMarker> = remember(selectedLotId) {
        StubParkingLots.all.map { lot ->
            MapMarker(
                id = lot.id,
                latitude = lot.latitude,
                longitude = lot.longitude,
                title = lot.name,
                subtitle = "${lot.availableSpaces}자리 가능 · ${lot.distanceMeters}m",
                isHighlighted = lot.id == selectedLotId,
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        OsmMap(
            modifier = Modifier.fillMaxSize(),
            centerLatitude = featuredLot.latitude,
            centerLongitude = featuredLot.longitude,
            zoom = 13.5,
            markers = mapMarkers,
            showMyLocation = hasLocationPermission,
            moveToMyLocationTrigger = moveToCurrentTrigger,
            zoomInTrigger = zoomInTrigger,
            zoomOutTrigger = zoomOutTrigger,
            onMarkerClick = { marker ->
                selectedLotId = marker.id
                dismissSearch()
            },
            onMapMoved = {
                if (searchFocused) dismissSearch()
            },
        )

        if (showOverlay) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(EzparkColors.gray10),
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(horizontal = EzparkSpacing.xxl, vertical = EzparkSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(EzparkSpacing.xs),
        ) {
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                onFocusChanged = { searchFocused = it },
                onSubmit = {
                    val firstHit = results.firstOrNull()
                    if (firstHit != null) {
                        selectedLotId = firstHit.id
                        query = firstHit.name
                    }
                    dismissSearch()
                },
            )
            if (showOverlay) {
                if (query.isBlank()) {
                    RecentSearchList(
                        items = RecentSearches,
                        onItemClick = { name ->
                            query = name
                            val match = StubParkingLots.all.firstOrNull { it.name.contains(name) }
                            if (match != null) selectedLotId = match.id
                            dismissSearch()
                        },
                    )
                } else {
                    if (results.isEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = EzparkShapes.card,
                            color = EzparkColors.gray10,
                            shadowElevation = 6.dp, // spacing-scale exception: card shadow elevation per Figma
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(EzparkSpacing.lg),
                                contentAlignment = Alignment.CenterStart,
                            ) {
                                Text(
                                    text = "검색 결과가 없습니다",
                                    style = EzparkTypography.caption2,
                                    color = EzparkColors.gray60,
                                )
                            }
                        }
                    } else {
                        RecentSearchList(
                            items = results.map { "${it.name}  ·  ${it.distanceMeters}m  ·  ${it.availableSpaces}자리" },
                            onItemClick = { displayLine ->
                                val matched = results.firstOrNull { displayLine.startsWith(it.name) }
                                if (matched != null) {
                                    selectedLotId = matched.id
                                    query = matched.name
                                    dismissSearch()
                                }
                            },
                        )
                    }
                }
            }
        }

        if (!showOverlay) {
            // Stacked map controls: zoom in, zoom out, my-location
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(end = EzparkSpacing.xxl, bottom = FabSpacing.plus(EzparkSpacing.xxl)),
                verticalArrangement = Arrangement.spacedBy(EzparkSpacing.sm),
            ) {
                MapControlButton(label = "+", onClick = { zoomInTrigger++ })
                MapControlButton(label = "−", onClick = { zoomOutTrigger++ })
                MapControlButton(
                    iconRes = R.drawable.ic_marker_plain,
                    contentDescription = "내 위치",
                    iconTint = EzparkColors.primaryBlue,
                    onClick = {
                        if (hasLocationPermission) {
                            moveToCurrentTrigger++
                        } else {
                            locationPermissionLauncher.launch(
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                            )
                        }
                    },
                )
            }

            ParkingInfoCard(
                title = featuredLot.name,
                badgeText = "${featuredLot.availableSpaces}자리 남음",
                distance = "${featuredLot.distanceMeters}m 남음",
                leftStat = StatData("공회전 중인 차량", "${featuredLot.availableSpaces}", "(대)"),
                rightStat = StatData("주차 가능", "${featuredLot.availableSpaces}", "/ ${featuredLot.totalSpaces}"),
                ctaText = "주차장 확인하기",
                onCta = { onNavigateToDetail(featuredLot.id) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(EzparkSpacing.xxl),
            )
        }
    }
}

@Composable
private fun MapControlButton(
    onClick: () -> Unit,
    label: String? = null,
    iconRes: Int? = null,
    contentDescription: String? = null,
    iconTint: androidx.compose.ui.graphics.Color = EzparkColors.gray100,
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = EzparkColors.gray10,
        shadowElevation = 6.dp, // spacing-scale exception: card shadow elevation per Figma
        modifier = Modifier.size(FabSize),
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(EzparkColors.gray10, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            when {
                label != null -> Text(
                    text = label,
                    style = EzparkTypography.subtitle2,
                    color = EzparkColors.gray100,
                )
                iconRes != null -> Icon(
                    painter = painterResource(iconRes),
                    contentDescription = contentDescription,
                    tint = iconTint,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MapScreenPreview() {
    EzparkTheme {
        MapScreen(onNavigateToDetail = {})
    }
}
