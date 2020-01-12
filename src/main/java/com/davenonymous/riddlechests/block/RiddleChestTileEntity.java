package com.davenonymous.riddlechests.block;

import com.davenonymous.libnonymous.base.BaseTileEntity;
import com.davenonymous.libnonymous.serialization.Store;
import com.davenonymous.riddlechests.riddles.RiddleInfo;
import com.davenonymous.riddlechests.setup.ModObjects;
import com.davenonymous.riddlechests.util.Logz;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RiddleChestTileEntity extends BaseTileEntity {
    @Store(storeWithItem = true, sendInUpdatePackage = true)
    public boolean solved;

    @Store(storeWithItem = true, sendInUpdatePackage = true)
    public ResourceLocation linkedRiddle;

    private LazyOptional<ItemStackHandler> handler = LazyOptional.of(this::createHandler);

    public RiddleInfo cachedRiddle;

    public RiddleChestTileEntity() {
        super(ModObjects.RIDDLECHEST_TILE);
    }

    public RiddleInfo getRiddle() {
        if(cachedRiddle == null && linkedRiddle != null) {
            cachedRiddle = ModObjects.riddleRecipeHelper.getRecipe(this.world.getRecipeManager(), linkedRiddle);
            if(cachedRiddle == null) {
                setNewRiddle();
            }
        }

        return cachedRiddle;
    }

    private void setNewRiddle() {
        cachedRiddle = ModObjects.riddleRecipeHelper.getRandomRecipe(this.world.getRecipeManager(), this.world.rand);
        linkedRiddle = cachedRiddle.getId();
        this.markDirty();
        this.notifyClients();
    }

    @Override
    protected void initialize() {
        super.initialize();

        if(linkedRiddle == null) {
            setNewRiddle();
        }
    }



    public void generateLoot(ServerPlayerEntity player) {
        handler.ifPresent(handler -> {
            List<Integer> list = Lists.newArrayList();
            for(int i = 0; i < handler.getSlots(); ++i) {
                list.add(i);
            }

            Collections.shuffle(list, this.world.rand);

            Set<ResourceLocation> lootTableIds = ModObjects.lootTableMappingRecipeHelper.getLootTablesForCategory(cachedRiddle.category, this.world.getRecipeManager());
            for(ResourceLocation lootTableId : lootTableIds) {
                LootTable lootTable = this.world.getServer().getLootTableManager().getLootTableFromLocation(lootTableId);
                if(lootTable == null) {
                    Logz.warn("While generating riddle chest loot: Loottable '{}' does not exist. Check the loottable mapping of '{}'.", lootTableId, cachedRiddle.category);
                    continue;
                }

                LootContext.Builder builder = new LootContext.Builder((ServerWorld)this.world)
                        .withParameter(LootParameters.POSITION, new BlockPos(this.pos))
                        .withSeed(this.world.rand.nextLong());

                if (player != null) {
                    builder.withLuck(player.getLuck()).withParameter(LootParameters.THIS_ENTITY, player);
                }
                LootContext lootContext = builder.build(LootParameterSets.CHEST);

                lootTable.generate(lootContext, itemStack -> {
                    if(list.size() <= 0) {
                        return;
                    }

                    Integer slotToFill = list.remove(0);
                    handler.insertItem(slotToFill, itemStack, false);
                });
            }

        });
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void read(CompoundNBT tag) {
        CompoundNBT invTag = tag.getCompound("inv");
        handler.ifPresent(h -> h.deserializeNBT(invTag));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        handler.ifPresent(h -> {
            CompoundNBT compound = h.serializeNBT();
            tag.put("inv", compound);
        });
        return super.write(tag);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(6 * 9) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
        };
    }

    public NonNullList<ItemStack> getContentStacks() {
        NonNullList<ItemStack> result = NonNullList.create();
        handler.ifPresent(h -> {
            for(int slot = 0; slot < h.getSlots(); slot++) {
                ItemStack content = h.getStackInSlot(slot);
                if(content.isEmpty()) {
                    continue;
                }

                result.add(content.copy());
            }
        });
        return result;
    }
}
