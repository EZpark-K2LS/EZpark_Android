# Ezpark Android — Coding Agent Rules

This file instructs coding agents (executor/ralph/team) on how to build features in this codebase. Read it before authoring any new file under `app/`.

---

## Rules

1. **Always import from `ui.theme`** — use `EzparkColors`, `EzparkTypography`, `EzparkShapes`, and `EzparkSpacing` for every color, text style, shape, and spacing value. Never write raw `Color(0x...)`, inline hex strings, or arbitrary `dp` literals inside component or screen code.

2. **Reuse before creating** — always check `ui/components/` for an existing Composable before building a new one. If a component exists for the UI element you need, use it. Composable duplication is a defect.

3. **Spacing scale only** — use the spacing scale (4 / 8 / 12 / 16 / 20 / 24) via `EzparkSpacing.xs/sm/md/lg/xl/xxl`. Do not introduce arbitrary `dp` values inside layout padding, margin, or gap calls.

4. **New Composables ship with `@Preview`** — every new `@Composable` function in `ui/components/` or `ui/screens/` must include at least one `@Preview`-annotated sibling function in the same file with representative stub data.

5. **Stub data lives only in `data/stub/`** — hardcoded sample data (parking lots, floors, spaces) belongs exclusively in `data/stub/StubParkingLots.kt` and related files. Component and screen files must not contain hardcoded content strings or numeric business values.

6. **Install git hooks after clone** — run `git config core.hooksPath .githooks` once immediately after cloning the repository so pre-commit and other hooks are active.
