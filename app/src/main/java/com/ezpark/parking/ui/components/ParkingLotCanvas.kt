package com.ezpark.parking.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezpark.parking.R
import com.ezpark.parking.data.model.FloorLayout
import com.ezpark.parking.data.model.ParkingSpaceShape
import com.ezpark.parking.data.stub.StubParkingLots
import com.ezpark.parking.ui.theme.EzparkColors
import com.ezpark.parking.ui.theme.EzparkShapes
import com.ezpark.parking.ui.theme.EzparkSpacing
import com.ezpark.parking.ui.theme.EzparkTheme
import com.ezpark.parking.ui.theme.EzparkTypography
import kotlin.math.max
import kotlin.math.min

private const val MIN_ZOOM = 0.5f
private const val MAX_ZOOM = 4f

/**
 * Renders a parking floor plan from absolute SVG-like coordinates and supports
 * pinch-to-zoom + two-finger / single-finger pan via [detectTransformGestures].
 *
 * Tap on a space to fire [onSpaceTap] with the space id (useful for future
 * "reserve this spot" flows).
 */
@Composable
fun ParkingLotCanvas(
    layout: FloorLayout,
    modifier: Modifier = Modifier,
    onSpaceTap: (String) -> Unit = {},
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val disabledPainter = rememberVectorPainter(
        image = ImageVector.vectorResource(id = R.drawable.ic_disabled),
    )

    Box(
        modifier = modifier
            .background(EzparkColors.gray10, RoundedCornerShape(12.dp))
            .clipToBounds(),
    ) {
        // Hint label (shows briefly - a tiny accessory on top-left)
        Text(
            text = "${(scale * 100).toInt()}%",
            style = EzparkTypography.caption2,
            color = EzparkColors.gray60,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(EzparkSpacing.md),
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        val newScale = (scale * zoom).coerceIn(MIN_ZOOM, MAX_ZOOM)
                        // Scale the pan so dragging feels natural regardless of current zoom
                        offset += pan
                        scale = newScale
                    }
                }
                .pointerInput(layout) {
                    detectTapGestures { tap ->
                        // Convert tap to viewport coords
                        val canvasW = size.width.toFloat()
                        val canvasH = size.height.toFloat()
                        val vpScale = min(canvasW / layout.viewportWidth, canvasH / layout.viewportHeight)
                        val dx = (canvasW - layout.viewportWidth * vpScale) / 2f
                        val dy = (canvasH - layout.viewportHeight * vpScale) / 2f
                        // Reverse the graphicsLayer transform (scale/translate) to get tap in pre-transform coords
                        val preX = (tap.x - offset.x - canvasW / 2f) / scale + canvasW / 2f
                        val preY = (tap.y - offset.y - canvasH / 2f) / scale + canvasH / 2f
                        // Convert to viewport coords
                        val vpX = (preX - dx) / vpScale
                        val vpY = (preY - dy) / vpScale
                        layout.spaces.firstOrNull { hit(it, vpX, vpY) }?.let { onSpaceTap(it.id) }
                    }
                },
        ) {
            val canvasW = size.width
            val canvasH = size.height
            val vpScale = min(canvasW / layout.viewportWidth, canvasH / layout.viewportHeight)
            val dx = (canvasW - layout.viewportWidth * vpScale) / 2f
            val dy = (canvasH - layout.viewportHeight * vpScale) / 2f

            // Walls
            layout.walls.forEach { w ->
                drawLine(
                    color = EzparkColors.gray60,
                    start = Offset(dx + w.x1 * vpScale, dy + w.y1 * vpScale),
                    end = Offset(dx + w.x2 * vpScale, dy + w.y2 * vpScale),
                    strokeWidth = max(2f, 2f * vpScale / 8f),
                )
            }

            // Spaces
            layout.spaces.forEach { space ->
                val topLeft = Offset(dx + space.x * vpScale, dy + space.y * vpScale)
                val sz = Size(space.width * vpScale, space.height * vpScale)
                val (bg, border) = when (space.status) {
                    SpaceStatus.AVAILABLE -> EzparkColors.successLight to EzparkColors.successBorder
                    SpaceStatus.DISABLED -> EzparkColors.successLight to EzparkColors.successBorder
                    SpaceStatus.OCCUPIED -> EzparkColors.gray20 to EzparkColors.gray60
                }
                translate(topLeft.x, topLeft.y) {
                    rotate(space.rotation, pivot = Offset.Zero) {
                        drawRoundRect(
                            color = bg,
                            topLeft = Offset.Zero,
                            size = sz,
                            cornerRadius = CornerRadius(4f * vpScale, 4f * vpScale),
                        )
                        drawRoundRect(
                            color = border,
                            topLeft = Offset.Zero,
                            size = sz,
                            cornerRadius = CornerRadius(4f * vpScale, 4f * vpScale),
                            style = Stroke(width = max(1.5f, 1.5f * vpScale / 4f)),
                        )
                        if (space.status == SpaceStatus.DISABLED) {
                            val iconSize = min(sz.width, sz.height) * 0.45f
                            translate(
                                left = (sz.width - iconSize) / 2f,
                                top = (sz.height - iconSize) / 2f,
                            ) {
                                with(disabledPainter) {
                                    draw(size = Size(iconSize, iconSize))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun hit(space: ParkingSpaceShape, vpX: Float, vpY: Float): Boolean {
    // Axis-aligned hit test; rotation not accounted for (good enough for walls
    // that are orthogonal in the stub; cells are axis-aligned in current layouts).
    return vpX >= space.x && vpX <= space.x + space.width &&
        vpY >= space.y && vpY <= space.y + space.height
}

@Preview(showBackground = true)
@Composable
private fun PreviewCanvas() {
    EzparkTheme {
        Box(
            modifier = Modifier
                .background(EzparkColors.gray10)
                .padding(EzparkSpacing.lg)
                .background(EzparkColors.gray20, EzparkShapes.card),
        ) {
            ParkingLotCanvas(
                layout = StubParkingLots.shinseong2.floors.first().layout,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
