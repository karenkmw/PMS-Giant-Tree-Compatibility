package com.pms.gianttreecompat.mixin;

import com.pms.gianttreecompat.GiantGrowthBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The custom saplings do not necessarily execute vanilla SaplingBlock.advanceTree.
 * BOP's advanceTree delegates to performBonemeal, whose stage-1 branch
 * calls TreeGrower.growTree directly. Cover both entry points.
 *
 * The bridge only converts a configured, stage-1 sapling with 5+ connected
 * identical saplings; otherwise the original tree behavior is untouched.
 */
@Mixin(targets = {
    "biomesoplenty.block.SaplingBlockBOP",
    "com.mamailes.herbsandharvest.block.MHHSaplingBlock"
}, remap = false)
public abstract class CustomSaplingGrowthMixin {
    @Inject(method = "advanceTree", at = @At("HEAD"), cancellable = true, remap = false)
    private void pms$tryGiantGrowth(ServerLevel level, BlockPos pos, BlockState state,
                                    RandomSource random, CallbackInfo ci) {
        if (GiantGrowthBridge.tryGrow(level, pos, state, random)) ci.cancel();
    }
}
