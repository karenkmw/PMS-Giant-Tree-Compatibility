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

/** Targets the actual BOP tree-generation entry point on stage-1 bonemeal. */
@Mixin(targets = "biomesoplenty.block.SaplingBlockBOP", remap = false)
public abstract class BOPBonemealMixin {
    @Inject(method = "performBonemeal", at = @At("HEAD"), cancellable = true, remap = false)
    private void pms$tryGiantGrowthOnBonemeal(ServerLevel level, RandomSource random,
                                               BlockPos pos, BlockState state, CallbackInfo ci) {
        if (GiantGrowthBridge.tryGrow(level, pos, state, random)) ci.cancel();
    }
}
