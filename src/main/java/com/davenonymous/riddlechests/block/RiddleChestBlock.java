package com.davenonymous.riddlechests.block;

import com.davenonymous.libnonymous.base.BaseBlock;
import com.davenonymous.libnonymous.base.BaseLanguageProvider;
import com.davenonymous.riddlechests.gui.OpenRiddleChestContainer;
import com.davenonymous.riddlechests.gui.RiddleChestContainer;
import com.davenonymous.riddlechests.network.Networking;
import com.davenonymous.riddlechests.recipe.riddles.RiddleInfo;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;


public class RiddleChestBlock extends BaseBlock {
    private final VoxelShape shapeX = Shapes.box(0.15f, 0.005f, 0.065f, 0.85f, 0.7f, 0.935f);
    private final VoxelShape shapeZ = Shapes.box(0.065f, 0.005f, 0.15f, 0.935f, 0.7f, 0.85f);

    public RiddleChestBlock() {
        super(Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN)
            .strength(15.0f, 1200.0f)
            .sound(SoundType.WOOD)
            .requiresCorrectToolForDrops());
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = pState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        if(facing == Direction.NORTH || facing == Direction.SOUTH) {
            return shapeZ;
        }
        return shapeX;
    }


    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult pHit) {
        if(player.isCrouching()) {
            return InteractionResult.FAIL;
        }

        if(worldIn.isClientSide() || !(player instanceof ServerPlayer)) {
            return InteractionResult.SUCCESS;
        }

        if(handIn != InteractionHand.MAIN_HAND) {
            return InteractionResult.FAIL;
        }

        var tile = worldIn.getBlockEntity(pos);
        if(tile instanceof RiddleChestTileEntity chestTile) {
            if(chestTile.solved) {
                // Open container gui
                worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5F, worldIn.random.nextFloat() * 0.1F + 0.9F);
                MenuProvider menuProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return new TranslatableComponent(BaseLanguageProvider.getContainerLanguageKey(Registration.OPEN_RIDDLECHEST_CONTAINER.get()));
                    }

                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                        return new OpenRiddleChestContainer(pContainerId, pos, pInventory, pPlayer);
                    }
                };

                NetworkHooks.openGui((ServerPlayer)player, menuProvider, pos);
                return InteractionResult.SUCCESS;
            }

            RiddleInfo riddle = chestTile.getRiddle();
            if(riddle == null) {
                return InteractionResult.FAIL;
            }



            MenuProvider menuProvider = new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return new TranslatableComponent(BaseLanguageProvider.getContainerLanguageKey(Registration.RIDDLECHEST_CONTAINER.get()));
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                    return new RiddleChestContainer(pContainerId, pos, pInventory, pPlayer);
                }
            };

            NetworkHooks.openGui((ServerPlayer)player, menuProvider, pos);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }



    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);

        if(placer == null) {
            return;
        }

        world.setBlock(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, getFacingFromEntity(pos, placer, true)), 2);
        if(stack.hasTag() && world.getBlockEntity(pos) instanceof RiddleChestTileEntity chestTile) {
            ResourceLocation existingRiddle = ResourceLocation.tryParse(stack.getTag().getString("riddle"));
            chestTile.linkedRiddle = existingRiddle;
            chestTile.solved = stack.getTag().getBoolean("solved");
            chestTile.setChanged();
            chestTile.notifyClients();
        }
    }


    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(worldIn.getBlockEntity(pos) instanceof RiddleChestTileEntity chestTile) {
            if(chestTile.solved) {
                // TODO: InventoryHelper.dropItems(worldIn, pos, chestTile.getContentStacks());
            }

            return;
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RiddleChestTileEntity(pPos, pState);
    }
}