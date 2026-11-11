package com.pms.gianttreecompat;

import com.seasky.gianttree.Block.ModBlocks;
import com.seasky.gianttree.BlockEntity.TreeCoreBlockEntity;
import com.seasky.gianttree.Config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/** Reproduces Giant Tree's group activation for saplings with custom advanceTree overrides.
 *  It DOES NOT change vanilla saplings, growth probabilities, or tree textures.
 */
public final class GiantGrowthBridge {
    private GiantGrowthBridge() {}

    public static boolean tryGrow(ServerLevel level, BlockPos origin, BlockState state, RandomSource random) {
        if (!state.hasProperty(SaplingBlock.STAGE) || state.getValue(SaplingBlock.STAGE) != 1) return false;
        Block sapling = state.getBlock();
        String saplingId = BuiltInRegistries.BLOCK.getKey(sapling).toString();
        Config config = Config.getInstance();
        // Only intervene for explicitly configured saplings; never accidentally turn an unknown tree into oak.
        if (config == null || !config.hasSapling(saplingId)) return false;
        int maximum = config.getMaxSaplingCount();
        if (maximum < 5) return false;

        Set<BlockPos> group = findConnected(level, origin, sapling, maximum);
        if (group.size() <= 4) return false;

        // Create Giant Tree's own tree core. The normal Giant Tree block entity then handles
        // growth, saving, and rendering using the user's existing gianttree.json.
        // Do not destroy the saplings if something unexpected prevents core initialization.
        BlockState coreState = ModBlocks.TREE_CORE.get().defaultBlockState();
        if (!level.setBlock(origin, coreState, 3)) return false;
        if (!(level.getBlockEntity(origin) instanceof TreeCoreBlockEntity core)) {
            level.setBlock(origin, state, 3);
            return false;
        }
        for (BlockPos pos : group) {
            if (!pos.equals(origin)) level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
        core.setSaplingCount(group.size());
        core.setRandomSeed(random.nextLong());
        core.setSaplingId(saplingId);
        core.rebuildGrowther();
        return true;
    }

    private static Set<BlockPos> findConnected(ServerLevel level, BlockPos origin, Block sapling, int limit) {
        Set<BlockPos> found = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(origin);
        found.add(origin);
        while (!queue.isEmpty() && found.size() < limit) {
            BlockPos pos = queue.remove();
            for (BlockPos next : new BlockPos[]{pos.east(), pos.west(), pos.north(), pos.south()}) {
                if (found.size() >= limit) break;
                if (found.contains(next)) continue;
                if (level.getBlockState(next).is(sapling)) {
                    found.add(next);
                    queue.add(next);
                }
            }
        }
        return found;
    }
}
