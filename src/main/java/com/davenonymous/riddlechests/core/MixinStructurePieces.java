/*
package com.davenonymous.riddlechests.core;

import com.davenonymous.riddlechests.event.GenerateChestEvent;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(StructurePiece.class)
public abstract class MixinStructurePieces {

    @Inject(method = "generateChest(" +
            "Lnet/minecraft/world/IWorld;" +
            "Lnet/minecraft/util/math/MutableBoundingBox;" +
            "Ljava/util/Random;" +
            "Lnet/minecraft/util/math/BlockPos;" +
            "Lnet/minecraft/util/ResourceLocation;" +
            "Lnet/minecraft/block/BlockState;" +
            ")Z", at = @At("RETURN"), require = 2)
    public void onGenerateChest(IWorld worldIn, MutableBoundingBox boundsIn, Random rand, BlockPos posIn, ResourceLocation resourceLocationIn, BlockState p_191080_6_, CallbackInfoReturnable<Boolean> ci) {
    //protected void onGenerateChest(IWorld worldIn, BlockPos posIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new GenerateChestEvent(worldIn, posIn));
    }
}
*/