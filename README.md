# PMS Giant Tree Compatibility — BOP growth fix (0.1.3)

Targeted experimental update based on tested server-starting v0.1.1.

- Adds `BOPBonemealMixin` to intercept `SaplingBlockBOP.performBonemeal`, the method that actually invokes tree generation in the supplied Biomes O’ Plenty JAR.
- Keeps existing `advanceTree` interception unchanged for BOP and Herbs & Harvest.
- Keeps the 5+ horizontally connected, stage-1 sapling condition; a 3×3 grid meets the quantity requirement.
- No changes to Giant Tree JSON, fruit-tree logic, or wood mappings.
- Packaging remains compatible with GitHub Actions. Do not replace `.github`.

This fixes an uncovered BOP growth path; success must still be established by an in-game test. It is not a comprehensive fix for all possible BOP growth failures.
