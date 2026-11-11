# PMS Giant Tree Compatibility — v0.1.0 (experimental source)

Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21.

This is a **new, untested Gradle build** of the previously experimental compatibility bridge. It changes the mod ID and package from `nmlgianttreecompat` to `pmsgianttreecompat`, and compiles against the published Giant Tree 1.0.1 JAR from Curse Maven rather than temporary Java stub classes.

## Scope

- Intercepts `advanceTree` in Biomes O' Plenty `SaplingBlockBOP` and Herbs & Harvest `MHHSaplingBlock`.
- Only attempts Giant Tree activation if a configured sapling has STAGE=1 and more than four connected saplings.
- Reuses Giant Tree's own `TreeCoreBlockEntity` and existing gianttree.json configuration.
- Does not change the original Giant Tree mod or any config files.

## Build

GitHub Actions automatically builds with Java 21, Gradle 8.10.2, NeoForge ModDevGradle, installs and updates ClamAV, scans the built JAR, then uploads the artifact only if the build and scan succeed.

Build manually with `gradle build` (Gradle 8.10.2 and JDK 21 required). Requires network access to resolve NeoForge and Curse Maven dependencies.

## Important test notes

1. Remove the old `nml-gianttree-compat-0.1.0-experimental.jar` or renamed copy before installing this new build; don't run both.
2. Install alongside Giant Tree 1.0.1, Biomes O' Plenty, and Herbs & Harvest. The normal mods are NOT bundled.
3. Test on the temporary test server and make a world backup first. This is not yet runtime-verified.
4. Test BOP Jacaranda and eight Herbs & Harvest fruit saplings already configured in gianttree.json.
5. A successful compile or antivirus scan does not establish Minecraft runtime compatibility. Inspect server logs if startup fails.

The mod is currently intended for server-side growth compatibility; the mod metadata uses BOTH to permit installation in matching client/server modpacks.
