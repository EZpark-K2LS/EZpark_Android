# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test

- Build debug APK: `./gradlew :app:assembleDebug`
- Compile only (fast): `./gradlew :app:compileDebugKotlin`
- Instrumented UI tests: `./gradlew :app:connectedDebugAndroidTest` (requires a running emulator/device)
- Run a single instrumented test: `./gradlew :app:connectedDebugAndroidTest --tests "com.ezpark.parking.SmokeTest"`
- Android SDK location must be set in `local.properties` (template at `local.properties.example`). JDK 21 is required (configured via `kotlin { jvmToolchain(21) }`).
- On Windows bash shells use forward slashes and `./gradlew`; on `cmd` use `gradlew.bat`.

## Repo Hooks

After cloning, run once: `git config core.hooksPath .githooks`. The `pre-commit` hook rejects any staged `.kt` file under `app/src/main/java/com/ezpark/parking/ui/(components|screens)/**` that contains:
- Hex color literals (`Color(0x...)` or `#RRGGBB`)
- Arbitrary `dp` literals outside the canonical `4 / 8 / 12 / 16 / 20 / 24` scale

Bypass an intentional exception by appending `// spacing-scale exception: <reason>` to the line (matches the hook's allow-list grep), not by using `--no-verify`.

## Architecture

Single-module Jetpack Compose app (`app/`) with namespace `com.ezpark.parking`. Entry point: `MainActivity` → `EzparkNavGraph` (`ui/navigation/NavGraph.kt`). Two destinations wired by `Routes`:
- `map` → `MapScreen` (OSM map via `osmdroid`, search bar with recent history, bottom `ParkingInfoCard`)
- `detail/{lotId}` → `ParkingLotDetailScreen` (floor grid + `FloorSelector` + `AvailabilityChip`)

Data is **stub-only**: no network layer. All sample parking lots/floors/spaces live in `data/stub/StubParkingLots.kt` and are the single source for screens. Do not add real data fetching.

The design system is load-bearing. Screens and components must pull every color, text style, shape, and spacing value from `ui/theme/` tokens — `EzparkColors`, `EzparkTypography`, `EzparkShapes`, `EzparkSpacing`. Raw literals are rejected by the pre-commit hook (see above). `docs/DESIGN.md` is the canonical token reference (colors, typography, shadow spec, component variants); `docs/AGENTS.md` is the short rule list for authoring new UI code.

### Conventions enforced beyond the hook

- Every new `@Composable` in `ui/components/` or `ui/screens/` ships with at least one `@Preview` sibling using representative stub data.
- Reuse existing components under `ui/components/` before creating new ones.
- Card shadow is always `Modifier.shadow(elevation = 6.dp, shape = EzparkShapes.card)` — not `CardDefaults.elevation`. `6.dp` here is an allowed exception (annotate with the spacing-scale comment).

## Useful References

- `docs/DESIGN.md` — design tokens, component stylings, do/don't list
- `docs/AGENTS.md` — concise authoring rules for this codebase
- `gradle/libs.versions.toml` — centralized dependency/version catalog
