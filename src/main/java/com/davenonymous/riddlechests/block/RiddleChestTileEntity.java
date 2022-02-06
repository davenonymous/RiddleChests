package com.davenonymous.riddlechests.block;

import com.davenonymous.libnonymous.base.BaseBlockEntity;
import com.davenonymous.libnonymous.serialization.Store;
import com.davenonymous.riddlechests.recipe.riddles.RiddleInfo;
import com.davenonymous.riddlechests.setup.Registration;
import com.davenonymous.riddlechests.util.Logz;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RiddleChestTileEntity extends BaseBlockEntity<RiddleChestTileEntity> {
    @Store(storeWithItem = true, sendInUpdatePackage = true)
    public boolean solved;

    @Store(storeWithItem = true, sendInUpdatePackage = true)
    public ResourceLocation linkedRiddle;

    private LazyOptional<ItemStackHandler> handler = LazyOptional.of(this::createHandler);

    public RiddleInfo cachedRiddle;

    public RiddleChestTileEntity(BlockPos pos, BlockState state) {
        super(Registration.RIDDLECHEST_BLOCKENTITY.get(), pos, state);
    }

    public RiddleInfo getRiddle() {
        if(cachedRiddle == null && linkedRiddle != null) {
            cachedRiddle = Registration.riddleRecipeHelper.getRecipe(this.level.getRecipeManager(), linkedRiddle);
            if(cachedRiddle == null) {
                setNewRiddle();
            }
        }

        return cachedRiddle;
    }

    private void setNewRiddle() {
        cachedRiddle = Registration.riddleRecipeHelper.getRandomRecipe(this.level.getRecipeManager(), this.level.random);
        if(cachedRiddle == null) {
            linkedRiddle = null;
            return;
        }

        linkedRiddle = cachedRiddle.getId();
        this.setChanged();
        this.notifyClients();
    }

    @Override
    protected void initialize() {
        super.initialize();

        if(linkedRiddle == null) {
            setNewRiddle();
        }
    }



    public void generateLoot(ServerPlayer player) {
        handler.ifPresent(handler -> {
            List<Integer> list = Lists.newArrayList();
            for(int i = 0; i < handler.getSlots(); ++i) {
                list.add(i);
            }

            Collections.shuffle(list, this.level.random);

            /*
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

             */

        });
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(6 * 9) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
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