package com.pms.gianttreecompat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/** Hook only the overridden methods, not vanilla SaplingBlock. */
@Mixin(targets = {
    "biomesoplenty.block.SaplingBlockBOP",
    "com.mamailes.herbsandharvest.block.MHHSaplingBlock"
}, remap = false)
public abstract class CustomSaplingGrowthMixin {
    @Inject(method = "advanceTree", at = @At("HEAD"), cancellable = true, remap = false)
    private void pms$tryGiantGrowth(ServerLevel level, BlockPos pos, BlockState state,
                                    RandomSource random, CallbackInfo ci) {
        if (GiantGrowthBridge.tryGrow(level, pos, state, random)) {
            ci.cancel();
        }
    }
}
