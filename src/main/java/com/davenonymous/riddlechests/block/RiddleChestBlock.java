package com.davenonymous.riddlechests.block;

import com.davenonymous.libnonymous.base.BaseNamedContainerProvider;
import com.davenonymous.libnonymous.base.WaterloggedBaseBlock;
import com.davenonymous.riddlechests.gui.OpenRiddleChestContainer;
import com.davenonymous.riddlechests.network.Networking;
import com.davenonymous.riddlechests.recipe.riddles.RiddleInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class RiddleChestBlock extends WaterloggedBaseBlock {
    private final VoxelShape shapeX = VoxelShapes.create(0.15f, 0.005f, 0.065f, 0.85f, 0.7f, 0.935f);
    private final VoxelShape shapeZ = VoxelShapes.create(0.065f, 0.005f, 0.15f, 0.935f, 0.7f, 0.85f);

    public RiddleChestBlock() {
        super(Properties.create(Material.WOOD, MaterialColor.BROWN)
            .hardnessAndResistance(15.0f, 1200.0f)
            .sound(SoundType.WOOD)
            .harvestTool(ToolType.AXE)
            .harvestLevel(2));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
        if(facing == Direction.NORTH || facing == Direction.SOUTH) {
            return shapeZ;
        }
        return shapeX;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new RiddleChestTileEntity();
    }

    public RiddleChestTileEntity getOwnTile(IBlockReader world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if(!(te instanceof RiddleChestTileEntity)) {
            return null;
        }

        return (RiddleChestTileEntity)te;
    }



    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(player.isCrouching()) {
            return ActionResultType.FAIL;
        }

        if(worldIn.isRemote || !(player instanceof ServerPlayerEntity)) {
            return ActionResultType.SUCCESS;
        }

        if(handIn != Hand.MAIN_HAND) {
            return ActionResultType.FAIL;
        }

        RiddleChestTileEntity chestTile = getOwnTile(worldIn, pos);
        if(chestTile == null) {
            return ActionResultType.FAIL;
        }

        if(chestTile.solved) {
            // Open container gui
            worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            NetworkHooks.openGui((ServerPlayerEntity)player, new BaseNamedContainerProvider(
                    new StringTextComponent("Riddle Chest"),
                    (id, inv, playerEntity) -> new OpenRiddleChestContainer(id, inv, worldIn, pos)
            ), pos);
            return ActionResultType.SUCCESS;
        }

        RiddleInfo riddle = chestTile.getRiddle();
        if(riddle == null) {
            return ActionResultType.FAIL;
        }

        Networking.sendOpenRiddleChestPacketToClient((ServerPlayerEntity)player, pos);
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if(placer == null) {
            return;
        }

        world.setBlockState(pos, state.with(BlockStateProperties.HORIZONTAL_FACING, getFacingFromEntity(pos, placer, true)), 2);
        if(stack.hasTag()) {
            RiddleChestTileEntity chestTile = getOwnTile(world, pos);
            if(chestTile != null) {
                ResourceLocation existingRiddle = ResourceLocation.tryCreate(stack.getTag().getString("riddle"));
                chestTile.linkedRiddle = existingRiddle;
                chestTile.solved = stack.getTag().getBoolean("solved");
                chestTile.markDirty();
                chestTile.notifyClients();
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        RiddleChestTileEntity chestTile = getOwnTile(worldIn, pos);
        if(chestTile == null) {
            super.onReplaced(state, worldIn, pos, newState, isMoving);
            return;
        }

        if(chestTile.solved) {
            InventoryHelper.dropItems(worldIn, pos, chestTile.getContentStacks());
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
