package com.davenonymous.riddlechests.caps;

import com.davenonymous.libnonymous.registration.AnnotatedInstanceUtil;
import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.caps.replacers.ChestReplacer;
import com.davenonymous.riddlechests.caps.replacers.IChestReplacer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapabilityRiddleChunk {
	public static final ResourceLocation RIDDLE_CHUNK = new ResourceLocation(RiddleChests.MODID, "riddle_chunk");

	public static final Capability<IRiddleChunk> RIDDLE_CHUNK_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

	@SubscribeEvent
	public static void registerCapability(RegisterCapabilitiesEvent event) {
		event.register(IRiddleChunk.class);
	}

	private static final LazyOptional<List<IChestReplacer>> processors = LazyOptional.of(() -> AnnotatedInstanceUtil.getInstances(ChestReplacer.class, IChestReplacer.class));

	public static void riddleChunk(LevelChunk chunk, ServerLevel level) {
		Optional<List<IChestReplacer>> optionalProcessors = processors.resolve();
		if(optionalProcessors.isEmpty()) {
			return;
		}

		for(var entry : chunk.getBlockEntities().entrySet()) {
			var pos = entry.getKey();
			var entity = entry.getValue();

			for(var processor : optionalProcessors.get()) {
				if(processor.replaceChest(pos, entity, chunk, level)) {
					break;
				}
			}
		}
	}
}