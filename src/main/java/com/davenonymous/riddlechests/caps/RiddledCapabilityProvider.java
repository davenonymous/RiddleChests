package com.davenonymous.riddlechests.caps;

import com.davenonymous.riddlechests.setup.RegistryEvents;
import net.minecraft.core.Direction;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ShortTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RiddledCapabilityProvider implements ICapabilitySerializable<IntTag> {
	IRiddleChunk storage;

	public RiddledCapabilityProvider() {
		storage = new RiddleChunk();
	}

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if(cap == CapabilityRiddleChunk.RIDDLE_CHUNK_CAPABILITY) {
			return (LazyOptional<T>) LazyOptional.of(() -> storage);
		}
		return LazyOptional.empty();
	}

	@Override
	public IntTag serializeNBT() {
		return IntTag.valueOf(storage.isRiddled() ? 1 : 0);
	}

	@Override
	public void deserializeNBT(IntTag nbt) {
		if(nbt.getAsInt() == 1) {
			storage.setRiddled();
		}
	}
}