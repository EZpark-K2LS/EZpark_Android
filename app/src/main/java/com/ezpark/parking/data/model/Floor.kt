package com.ezpark.parking.data.model

import com.ezpark.parking.ui.components.SpaceStatus

/**
 * A parking space placed at an absolute position in the floor's SVG-style viewport.
 * Coordinates are expressed in viewport units (not px or dp) so the layout scales
 * to the rendered canvas while preserving proportions.
 */
data class ParkingSpaceShape(
    val id: String,
    val x: Float,         // top-left x in viewport units
    val y: Float,         // top-left y in viewport units
    val width: Float,
    val height: Float,
    val rotation: Float = 0f,  // degrees, pivot = top-left
    val status: SpaceStatus,
)

/**
 * A wall / structural line segment in the floor plan. Drawn as a stroke.
 */
data class Wall(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
)

/**
 * SVG-style floor plan: a declared viewport and a list of shapes/walls drawn in it.
 * ViewportWidth/Height define the aspect ratio. The renderer picks a scale that
 * fits the viewport to the canvas and applies user pan/zoom on top.
 */
data class FloorLayout(
    val viewportWidth: Float,
    val viewportHeight: Float,
    val walls: List<Wall> = emptyList(),
    val spaces: List<ParkingSpaceShape>,
)

data class Floor(
    val id: String,
    val label: String,
    val layout: FloorLayout,
) {
    val availableCount: Int get() = layout.spaces.count { it.status == SpaceStatus.AVAILABLE }
    val totalCount: Int get() = layout.spaces.size
}
