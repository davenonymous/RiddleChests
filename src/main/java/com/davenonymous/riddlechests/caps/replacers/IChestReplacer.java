package com.davenonymous.riddlechests.caps.replacers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

public interface IChestReplacer {

	boolean replaceChest(BlockPos pos, BlockEntity entity, LevelChunk chunk, ServerLevel level);
}