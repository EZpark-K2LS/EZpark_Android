# Ezpark Design System

> Version 1.0 — 2026-04-18
> Pattern: VoltAgent/awesome-design-md

---

## 1. Visual Theme & Atmosphere

Ezpark is a clean, modern Korean public parking guide app. The visual language follows **functional minimalism**: maximum information density with minimum visual noise. Every element has a purpose.

**Design principles:**
- Color-coded status at a glance: **red = alert / low availability**, **green = available**, **blue = primary action**
- White-dominant surfaces with subtle gray scaffolding
- Card-based information hierarchy with soft shadow depth
- Pretendard typeface throughout — optimized for Korean + Latin legibility at small sizes

---

## 2. Color Palette & Roles

All color references in code must use `EzparkColors.*` tokens. No raw hex literals in component or screen code.

| Token name       | Hex       | Role                                     | Usage example                                      |
|------------------|-----------|------------------------------------------|----------------------------------------------------|
| `gray/10`        | `#FFFFFF` | Surface, card background                 | Card background, SearchBar fill, modal sheet       |
| `gray/20`        | `#EAEAEA` | Empty parking space, divider             | Occupied space fill, list divider                  |
| `gray/40`        | `#C2C2C2` | Disabled floor button                    | Inactive FloorSelector button                      |
| `gray/60`        | `#999999` | Secondary text, location label           | Distance label, subtitle hint                      |
| `gray/100`       | `#474747` | Primary text, active floor button        | Body text, active FloorSelector, AvailabilityChip  |
| `primary/blue`   | `#375CFF` | Primary CTA                              | "주차장 확인하기" PrimaryCtaButton                  |
| `primary/accent` | `#E4E9FF` | Info card secondary block                | MiniStatCard background                            |
| `alert/red`      | `#FF5959` | "N자리 남음" badge, alert pin             | MapPinBadge, low-availability indicator            |
| `success/light`  | `#DAFFD2` | Available parking space fill             | ParkingSpaceCell (AVAILABLE) background            |
| `success/border` | `#23D200` | Available parking space border           | ParkingSpaceCell (AVAILABLE) stroke                |
| `pin/red`        | `#E40000` | Map pin core                             | Map marker center dot                              |

**Kotlin object:** `EzparkColors` in `ui/theme/Color.kt`

---

## 3. Typography Rules

**Font family:** Pretendard (Regular 400 / Medium 500 / SemiBold 600)
**Line height:** 100% (tight, matching Figma spec) unless overridden per context.

All text styles are defined in `EzparkTypography` in `ui/theme/Typography.kt`.

| Token        | Weight   | Size  | FontWeight constant | Usage                                      |
|--------------|----------|-------|---------------------|--------------------------------------------|
| `Heading 3`  | SemiBold | 20sp  | `FontWeight.SemiBold` | Screen title, parking lot name             |
| `Subtitle 2` | SemiBold | 18sp  | `FontWeight.SemiBold` | Section headers                            |
| `Subtitle 3` | SemiBold | 16sp  | `FontWeight.SemiBold` | Card title, floor label (active)           |
| `Subtitle 4` | SemiBold | 14sp  | `FontWeight.SemiBold` | Badge label, chip label                    |
| `Subtitle 5` | SemiBold | 12sp  | `FontWeight.SemiBold` | Caption emphasis, tag                      |
| `Body 3`     | Medium   | 16sp  | `FontWeight.Medium`   | Body content, info card value              |
| `Button 2`   | Medium   | 16sp  | `FontWeight.Medium`   | Button label                               |
| `Caption 1`  | Regular  | 16sp  | `FontWeight.Normal`   | Secondary description, hint               |
| `Caption 2`  | Regular  | 14sp  | `FontWeight.Normal`   | Tertiary labels, distance text             |

---

## 4. Component Stylings

All components live in `ui/components/`. Each ships with `@Preview`.

### 1. SearchBar
**Description:** Horizontal bar with a leading search icon and hint text. Tappable; opens keyboard focus.
- **Default:** White (`gray/10`) background, card shadow, `gray/60` hint text, `Subtitle 3` style, 8dp corner radius
- **Focused:** Same background, `gray/100` text color, cursor visible
- **Disabled:** `gray/20` background, `gray/40` text

### 2. RecentSearchList
**Description:** White card below SearchBar listing recent search terms. Each row has a clock/search icon + text.
- **Default:** `gray/10` background, `gray/100` text, `Caption 1` style, divider `gray/20`
- **Item hovered/pressed:** `gray/20` row highlight

### 3. MapPinBadge
**Description:** Pill-shaped badge overlaid on map markers. Shows "N자리 남음".
- **Default:** `alert/red` background, white text, `Subtitle 4` style, `pill` radius (26dp)
- **Zero available:** Same style — alert red always; caller decides visibility

### 4. ParkingInfoCard
**Description:** Bottom-sheet card. Contains lot name (`Heading 3`), MapPinBadge, location/distance row, two MiniStatCards side by side, and PrimaryCtaButton.
- **Default:** `gray/10` background, `card` radius (8dp), card shadow (`0dp 6dp 4dp rgba(0,0,0,0.15)`)
- **Loading:** Shimmer over name + badge area

### 5. MiniStatCard
**Description:** Small info tile inside ParkingInfoCard. Shows a label + large number + secondary unit text.
- **Default:** `primary/accent` (`#E4E9FF`) background, `gray/100` text, `card` radius
- **Variants:** "공회전 금지" (idling warning) / "주차 가능" (available count)

### 6. PrimaryCtaButton
**Description:** Full-width call-to-action button.
- **Default:** `primary/blue` background, white `Button 2` text, `pill` radius (26dp), 16dp vertical padding
- **Pressed:** 10% darker blue overlay
- **Disabled:** `gray/40` background, `gray/60` text

### 7. DetailHeader
**Description:** Top app bar for ParkingLotDetailScreen. Back chevron (left) + lot name title (center) + options icon (right).
- **Default:** `gray/10` background, `gray/100` title text `Subtitle 2`, icon tint `gray/100`
- **Scrolled:** Subtle bottom divider `gray/20`

### 8. FloorSelector
**Description:** Vertical stack of circular buttons. One per floor (3층/2층/1층). Active floor highlighted.
- **Active:** `gray/100` fill, white text, `CircleShape`, 44dp diameter
- **Inactive:** `gray/40` fill, white text, same size
- **Pressed:** Ripple on `gray/60`

### 9. ParkingSpaceGrid
**Description:** Grid layout of ParkingSpaceCell items representing one floor.
- **Default:** Wraps cells in a lazy grid, spacing `EzparkSpacing.xs` (4dp) between cells
- No header; floor label provided by FloorSelector context

### 10. ParkingSpaceCell
**Description:** Single parking space rectangle (25dp wide × 45dp tall).
- **AVAILABLE:** `success/light` fill + `success/border` 1dp stroke
- **OCCUPIED:** `gray/20` fill + `gray/60` 1dp stroke
- **DISABLED (장애인):** Same as AVAILABLE + `mingcute:disabled-fill` icon overlay (wheelchair symbol)

### 11. AvailabilityChip
**Description:** Pill chip at bottom of ParkingLotDetailScreen. "N대 주차가능".
- **Default:** `gray/100` background, white `Subtitle 4` text, `chip` radius (32dp), 12dp horizontal padding

---

## 5. Layout Principles

| Rule                        | Value              |
|-----------------------------|--------------------|
| Screen horizontal padding   | `24dp`             |
| Card inner padding          | `16dp`             |
| Spacing scale               | `4 / 8 / 12 / 16 / 20 / 24` |

Use `EzparkSpacing.*` constants only. Never write raw `dp` values inside layout code.

```
EzparkSpacing.xs  = 4.dp
EzparkSpacing.sm  = 8.dp
EzparkSpacing.md  = 12.dp
EzparkSpacing.lg  = 16.dp   ← card inner padding
EzparkSpacing.xl  = 20.dp
EzparkSpacing.xxl = 24.dp   ← screen horizontal padding
```

---

## 6. Depth & Elevation

| Element     | Shadow spec                          |
|-------------|--------------------------------------|
| Card        | `0dp 6dp 4dp rgba(0, 0, 0, 0.15)`   |

Compose implementation: use `Modifier.shadow(elevation = 6.dp, shape = EzparkShapes.card)`.
Do not use Material3 `CardDefaults.elevation` — use the explicit shadow modifier.

---

## 7. Shape & Radii

All shapes are defined in `EzparkShapes` in `ui/theme/Shape.kt`.

| Token           | Value              | Usage                          |
|-----------------|--------------------|--------------------------------|
| `card`          | `RoundedCornerShape(8.dp)`  | ParkingInfoCard, SearchBar, MiniStatCard |
| `pill`          | `RoundedCornerShape(26.dp)` | MapPinBadge, PrimaryCtaButton  |
| `chip`          | `RoundedCornerShape(32.dp)` | AvailabilityChip               |
| `floorButton`   | `CircleShape` (44dp diameter) | FloorSelector buttons          |

---

## 8. Do's and Don'ts

### DO
- Use `EzparkColors.*` for all color references in Composables
- Use `EzparkTypography.*` for all text styles
- Use `EzparkSpacing.*` for all padding/gap values
- Use `EzparkShapes.*` for all corner shapes
- Wrap every new Composable with `@Preview`
- Place stub data only in `data/stub/`

### DON'T
- Inline hex literals (e.g. `Color(0xFF375CFF)`) in component/screen code
- Use arbitrary `dp` values in layouts (e.g. `padding(17.dp)`)
- Create new components when one already exists in `ui/components/`
- Add network calls or real data — this codebase is stub-only

---

## 9. Agent Prompt Guide

Quick reference for coding agents authoring new files:

```
- Primary CTAs → EzparkColors.primaryBlue background, white text, EzparkShapes.pill
- Alert / low availability → EzparkColors.alertRed
- Available parking spaces → EzparkColors.successLight background + EzparkColors.successBorder stroke
- Occupied spaces → EzparkColors.gray20 background + EzparkColors.gray60 stroke
- Primary text → EzparkColors.gray100
- Secondary text → EzparkColors.gray60
- Card background → EzparkColors.gray10
- Screen padding → EzparkSpacing.xxl (24.dp) horizontal
- Card inner padding → EzparkSpacing.lg (16.dp)
- Heading → EzparkTypography.heading3
- Button label → EzparkTypography.button2
- Always import from ui.theme — never use Color(0x...) in component/screen files
```
