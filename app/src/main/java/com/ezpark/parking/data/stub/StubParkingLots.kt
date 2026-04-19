package com.ezpark.parking.data.stub

import com.ezpark.parking.data.model.Floor
import com.ezpark.parking.data.model.FloorLayout
import com.ezpark.parking.data.model.ParkingLot
import com.ezpark.parking.data.model.ParkingSpaceShape
import com.ezpark.parking.data.model.Wall
import com.ezpark.parking.ui.components.SpaceStatus

object StubParkingLots {

    // Canonical parking space dimensions inside the SVG viewport
    private const val SPACE_W = 26f
    private const val SPACE_H = 48f

    // --- Layout helpers -------------------------------------------------------

    /**
     * Build a row of spaces laid side-by-side horizontally along a given y,
     * starting at the given x. Status is a per-index function.
     */
    private fun horizontalRow(
        idPrefix: String,
        startX: Float,
        y: Float,
        count: Int,
        gap: Float = 2f,
        rotation: Float = 0f,
        statusFor: (Int) -> SpaceStatus,
    ): List<ParkingSpaceShape> = List(count) { i ->
        ParkingSpaceShape(
            id = "$idPrefix-$i",
            x = startX + i * (SPACE_W + gap),
            y = y,
            width = SPACE_W,
            height = SPACE_H,
            rotation = rotation,
            status = statusFor(i),
        )
    }

    /**
     * Build a column of spaces stacked vertically at a given x, starting at the given y.
     * Cells are rotated 90° so their long edge is vertical relative to the driveway.
     */
    private fun verticalColumn(
        idPrefix: String,
        x: Float,
        startY: Float,
        count: Int,
        gap: Float = 2f,
        statusFor: (Int) -> SpaceStatus,
    ): List<ParkingSpaceShape> = List(count) { i ->
        ParkingSpaceShape(
            id = "$idPrefix-$i",
            x = x,
            y = startY + i * (SPACE_W + gap),
            width = SPACE_H,       // swapped because rotated conceptually
            height = SPACE_W,
            rotation = 0f,
            status = statusFor(i),
        )
    }

    /**
     * A typical parking floor: outer rectangular wall, two horizontal rows of spaces
     * (top and bottom facing a central driveway), plus two flanking vertical columns
     * on the sides. Caller supplies a status function so each floor feels distinct.
     */
    private fun defaultFloorLayout(
        availableTopIndices: Set<Int>,
        availableBottomIndices: Set<Int>,
        availableLeftIndices: Set<Int> = emptySet(),
        availableRightIndices: Set<Int> = emptySet(),
        disabledIndices: Map<String, Set<Int>> = emptyMap(),
    ): FloorLayout {
        val vw = 560f
        val vh = 380f

        val top = horizontalRow(
            idPrefix = "top", startX = 70f, y = 30f, count = 14,
            statusFor = { i ->
                when {
                    i in (disabledIndices["top"] ?: emptySet()) -> SpaceStatus.DISABLED
                    i in availableTopIndices -> SpaceStatus.AVAILABLE
                    else -> SpaceStatus.OCCUPIED
                }
            },
        )
        val bottom = horizontalRow(
            idPrefix = "bottom", startX = 70f, y = 302f, count = 14,
            statusFor = { i ->
                when {
                    i in (disabledIndices["bottom"] ?: emptySet()) -> SpaceStatus.DISABLED
                    i in availableBottomIndices -> SpaceStatus.AVAILABLE
                    else -> SpaceStatus.OCCUPIED
                }
            },
        )
        val left = verticalColumn(
            idPrefix = "left", x = 14f, startY = 110f, count = 4,
            statusFor = { i ->
                if (i in availableLeftIndices) SpaceStatus.AVAILABLE else SpaceStatus.OCCUPIED
            },
        )
        val right = verticalColumn(
            idPrefix = "right", x = 498f, startY = 110f, count = 4,
            statusFor = { i ->
                if (i in availableRightIndices) SpaceStatus.AVAILABLE else SpaceStatus.OCCUPIED
            },
        )

        // Outer walls (rectangle)
        val walls = listOf(
            Wall(0f, 0f, vw, 0f),
            Wall(vw, 0f, vw, vh),
            Wall(vw, vh, 0f, vh),
            Wall(0f, vh, 0f, 0f),
            // central driveway indicators (two dashed-feel inner lines)
            Wall(70f, 90f, vw - 70f, 90f),
            Wall(70f, vh - 90f, vw - 70f, vh - 90f),
        )

        return FloorLayout(
            viewportWidth = vw,
            viewportHeight = vh,
            walls = walls,
            spaces = top + bottom + left + right,
        )
    }

    // --- Lots -----------------------------------------------------------------

    val shinseong2 = ParkingLot(
        id = "shinseong-2",
        name = "신성동 제2 공영 주차장",
        address = "대전 유성구 신성동",
        distanceMeters = 30,
        totalSpaces = 150,
        availableSpaces = 3,
        latitude = 36.3915,
        longitude = 127.3610,
        floors = listOf(
            Floor(
                id = "3", label = "3층",
                layout = defaultFloorLayout(
                    availableTopIndices = setOf(1, 4, 9),
                    availableBottomIndices = setOf(2, 6, 11, 13),
                    availableLeftIndices = setOf(1),
                    availableRightIndices = setOf(3),
                ),
            ),
            Floor(
                id = "2", label = "2층",
                layout = defaultFloorLayout(
                    availableTopIndices = setOf(0, 5),
                    availableBottomIndices = setOf(3, 8, 12),
                    availableLeftIndices = setOf(2),
                    availableRightIndices = emptySet(),
                    disabledIndices = mapOf("top" to setOf(7), "bottom" to setOf(0, 13)),
                ),
            ),
            Floor(
                id = "1", label = "1층",
                layout = defaultFloorLayout(
                    availableTopIndices = setOf(10),
                    availableBottomIndices = setOf(5),
                    availableLeftIndices = emptySet(),
                    availableRightIndices = emptySet(),
                    disabledIndices = mapOf("top" to setOf(0)),
                ),
            ),
        ),
    )

    val shinseong1 = ParkingLot(
        id = "shinseong-1",
        name = "신성동 제1 공영 주차장",
        address = "대전 유성구 신성동",
        distanceMeters = 120,
        totalSpaces = 80,
        availableSpaces = 12,
        latitude = 36.3905,
        longitude = 127.3590,
        floors = listOf(
            Floor(
                id = "1", label = "1층",
                layout = defaultFloorLayout(
                    availableTopIndices = (0..13 step 2).toSet(),
                    availableBottomIndices = (1..13 step 2).toSet(),
                    availableLeftIndices = setOf(0, 2),
                    availableRightIndices = setOf(1, 3),
                ),
            ),
        ),
    )

    val noeunStation = ParkingLot(
        id = "noeun-station",
        name = "노은역 공영 주차장",
        address = "대전 유성구 노은동",
        distanceMeters = 850,
        totalSpaces = 220,
        availableSpaces = 47,
        latitude = 36.3812,
        longitude = 127.3270,
        floors = listOf(
            Floor(
                id = "3", label = "3층",
                layout = defaultFloorLayout(
                    availableTopIndices = (0..13).filter { it % 2 == 0 }.toSet(),
                    availableBottomIndices = (0..13).filter { it % 3 == 0 }.toSet(),
                    availableLeftIndices = setOf(0, 3),
                    availableRightIndices = setOf(1, 2),
                ),
            ),
            Floor(
                id = "2", label = "2층",
                layout = defaultFloorLayout(
                    availableTopIndices = setOf(1, 5, 9),
                    availableBottomIndices = setOf(4, 7, 11),
                    disabledIndices = mapOf("top" to setOf(0), "bottom" to setOf(13)),
                ),
            ),
            Floor(
                id = "1", label = "1층",
                layout = defaultFloorLayout(
                    availableTopIndices = setOf(7),
                    availableBottomIndices = setOf(4, 12),
                ),
            ),
        ),
    )

    val eoeunPark = ParkingLot(
        id = "eoeun-park",
        name = "어은동 근린공원 주차장",
        address = "대전 유성구 어은동",
        distanceMeters = 540,
        totalSpaces = 60,
        availableSpaces = 8,
        latitude = 36.3700,
        longitude = 127.3590,
        floors = listOf(
            Floor(
                id = "1", label = "1층",
                layout = defaultFloorLayout(
                    availableTopIndices = setOf(2, 8),
                    availableBottomIndices = setOf(4, 10, 13),
                    availableLeftIndices = setOf(1),
                    availableRightIndices = setOf(2),
                ),
            ),
        ),
    )

    val expoCenter = ParkingLot(
        id = "expo-center",
        name = "엑스포시민광장 주차장",
        address = "대전 유성구 도룡동",
        distanceMeters = 1320,
        totalSpaces = 410,
        availableSpaces = 96,
        latitude = 36.3744,
        longitude = 127.3892,
        floors = listOf(
            Floor(
                id = "B1", label = "B1",
                layout = defaultFloorLayout(
                    availableTopIndices = (0..13).filter { it % 3 != 0 }.toSet(),
                    availableBottomIndices = (0..13).filter { it % 2 == 1 }.toSet(),
                    availableLeftIndices = setOf(0, 1, 2),
                    availableRightIndices = setOf(1, 3),
                    disabledIndices = mapOf("top" to setOf(0, 13)),
                ),
            ),
            Floor(
                id = "1", label = "1층",
                layout = defaultFloorLayout(
                    availableTopIndices = setOf(1, 4, 8, 12),
                    availableBottomIndices = setOf(2, 9),
                ),
            ),
        ),
    )

    val all: List<ParkingLot> = listOf(shinseong2, shinseong1, noeunStation, eoeunPark, expoCenter)

    fun byId(id: String): ParkingLot? = all.firstOrNull { it.id == id }

    fun search(query: String): List<ParkingLot> {
        val q = query.trim()
        if (q.isEmpty()) return emptyList()
        val tokens = q.split(Regex("\\s+")).filter { it.isNotBlank() }
        return all.filter { lot ->
            val haystack = "${lot.name} ${lot.address}"
            tokens.all { haystack.contains(it, ignoreCase = true) }
        }
    }
}
