package com.ezpark.parking.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.ezpark.parking.ui.theme.EzparkColors
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

data class MapMarker(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val title: String? = null,
    val subtitle: String? = null,
    val isHighlighted: Boolean = false,
)

@Composable
fun OsmMap(
    modifier: Modifier = Modifier,
    centerLatitude: Double,
    centerLongitude: Double,
    zoom: Double = 16.5,
    markers: List<MapMarker> = emptyList(),
    showMyLocation: Boolean = true,
    moveToMyLocationTrigger: Int = 0,
    zoomInTrigger: Int = 0,
    zoomOutTrigger: Int = 0,
    onMarkerClick: (MapMarker) -> Unit = {},
    onMapMoved: () -> Unit = {},
) {
    val context = LocalContext.current
    val mapView = rememberMapView(context)
    val latestOnMapMoved by rememberUpdatedState(onMapMoved)
    // Plain ref — avoid writing Compose state inside AndroidView.update{}
    val lastAnimatedKeyRef = remember { arrayOf("") }
    val targetKey = "$centerLatitude,$centerLongitude"

    // showMyLocation is in the key so the overlay is recreated when permission changes
    val myLocationOverlay = remember(mapView, showMyLocation) {
        if (showMyLocation) {
            MyLocationNewOverlay(GpsMyLocationProvider(context), mapView).apply {
                enableMyLocation()
            }
        } else null
    }

    DisposableEffect(mapView) {
        mapView.onResume()
        onDispose { mapView.onPause() }
    }

    // Separate effect so each overlay instance cleans itself up when it changes
    DisposableEffect(myLocationOverlay) {
        onDispose { myLocationOverlay?.disableMyLocation() }
    }

    LaunchedEffect(moveToMyLocationTrigger) {
        if (moveToMyLocationTrigger > 0) {
            myLocationOverlay?.runOnFirstFix {
                val loc = myLocationOverlay.myLocation
                if (loc != null) {
                    mapView.post { mapView.controller.animateTo(loc, zoom, 600L) }
                }
            }
            myLocationOverlay?.myLocation?.let { existing ->
                mapView.controller.animateTo(existing, zoom, 600L)
            }
        }
    }

    LaunchedEffect(zoomInTrigger) {
        if (zoomInTrigger > 0) mapView.controller.zoomIn(300L)
    }

    LaunchedEffect(zoomOutTrigger) {
        if (zoomOutTrigger > 0) mapView.controller.zoomOut(300L)
    }

    AndroidView(
        modifier = modifier,
        factory = {
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                isHorizontalMapRepetitionEnabled = false
                isVerticalMapRepetitionEnabled = false
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                controller.setZoom(zoom)
                controller.setCenter(GeoPoint(centerLatitude, centerLongitude))
                // Overlay is managed in update{} — not added here
                addMapListener(object : MapListener {
                    override fun onScroll(event: ScrollEvent?): Boolean {
                        latestOnMapMoved()
                        return false
                    }
                    override fun onZoom(event: ZoomEvent?): Boolean {
                        latestOnMapMoved()
                        return false
                    }
                })
            }
        },
        update = { view ->
            if (lastAnimatedKeyRef[0] != targetKey) {
                view.controller.animateTo(GeoPoint(centerLatitude, centerLongitude), zoom, 600L)
                lastAnimatedKeyRef[0] = targetKey
            }
            // Sync my-location overlay (handles both null→overlay and overlay→null transitions)
            view.overlays.removeAll { it is MyLocationNewOverlay }
            if (myLocationOverlay != null) {
                view.overlays.add(0, myLocationOverlay)
            }
            // Sync parking markers
            view.overlays.removeAll { it is Marker }
            markers.forEach { m ->
                val marker = Marker(view).apply {
                    position = GeoPoint(m.latitude, m.longitude)
                    title = m.title
                    subDescription = m.subtitle
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    icon = ParkingPinDrawable(view.context, m.isHighlighted).build()
                    setOnMarkerClickListener { mk, _ ->
                        onMarkerClick(m)
                        view.controller.animateTo(mk.position, 16.5, 500L)
                        true
                    }
                }
                view.overlays.add(marker)
            }
            view.invalidate()
        },
    )
}

@Composable
private fun rememberMapView(context: Context): MapView {
    return remember(context) {
        val prefs = context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        Configuration.getInstance().load(context, prefs)
        Configuration.getInstance().userAgentValue = context.packageName
        MapView(context)
    }
}

private class ParkingPinDrawable(private val context: Context, private val highlighted: Boolean) {
    fun build(): android.graphics.drawable.Drawable {
        val density = context.resources.displayMetrics.density
        val sizePx = (28 * density).toInt()
        val bitmap = android.graphics.Bitmap.createBitmap(sizePx, sizePx, android.graphics.Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        val ringPaint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
            color = if (highlighted) EzparkColors.alertRed.toArgb() else EzparkColors.primaryBlue.toArgb()
            style = android.graphics.Paint.Style.FILL
        }
        val innerPaint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            style = android.graphics.Paint.Style.FILL
        }
        val cx = sizePx / 2f
        val cy = sizePx / 2f
        canvas.drawCircle(cx, cy, sizePx * 0.45f, ringPaint)
        canvas.drawCircle(cx, cy, sizePx * 0.22f, innerPaint)
        return android.graphics.drawable.BitmapDrawable(context.resources, bitmap)
    }
}
