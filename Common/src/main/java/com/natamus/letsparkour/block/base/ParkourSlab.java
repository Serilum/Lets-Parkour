package com.natamus.letsparkour.block.base;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.natamus.letsparkour.data.ParkourBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class ParkourSlab extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<ParkourSlab> CODEC = simpleCodec(ParkourSlab::new);

    public static final EnumProperty<SlabType> TYPE;
    public static final BooleanProperty WATERLOGGED;
    public static final IntegerProperty BLOCK_HEIGHT;

    private static final int maxTotalHeight = ParkourBlockStateProperties.BLOCK_HEIGHT.getPossibleValues().size()-1;

    protected static final VoxelShape BOTTOM_AABB;
    protected static final VoxelShape TOP_AABB;

    private final Set<BlockPos> processingPositions = new HashSet<>();

    public @NotNull MapCodec<ParkourSlab> codec() {
        return CODEC;
    }

    public ParkourSlab(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, false).setValue(BLOCK_HEIGHT, 1));
    }

    protected boolean useShapeForLightOcclusion(BlockState blockState) {
        return blockState.getValue(TYPE) != SlabType.DOUBLE;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(TYPE, WATERLOGGED, BLOCK_HEIGHT);
    }

    protected @NotNull VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        SlabType slabType = blockState.getValue(TYPE);
        switch (slabType) {
			case DOUBLE -> {
				return Shapes.block();
			}
			case TOP -> {
				return TOP_AABB;
			}
			default -> {
				return BOTTOM_AABB;
			}
		}
    }

    public void tick(@NotNull BlockState blockState, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {

    }

    public @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState2, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        if (blockState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }

        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    public void onPlace(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState2, boolean bl) {
        if (!processingPositions.contains(blockPos)) {
            calculateBlockHeight(blockState, level, blockPos);
        }
    }

    public void onRemove(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState2, boolean bl) {
        if (blockState2.isAir()) {
            calculateBlockHeight(blockState, level, blockPos);
        }
    }

public void calculateBlockHeight(BlockState blockState, Level level, BlockPos blockPos) {
    if (processingPositions.contains(blockPos)) {
        return;
    }

    processingPositions.add(blockPos);
    try {
        Block thisBlock = blockState.getBlock();
        int totalHeight = 0;

        List<Pair<BlockPos, BlockState>> blockStatesToSet = new ArrayList<>();

        int y = blockPos.getY();
        for (int yo = y; yo >= level.getMinBuildHeight(); yo--) {
            BlockPos checkBlockPos = new BlockPos(blockPos.getX(), yo, blockPos.getZ());
            BlockState checkBlockState = level.getBlockState(checkBlockPos);
            if (checkBlockState.getBlock().equals(thisBlock)) {
                if (checkBlockState.getValue(TYPE) == SlabType.DOUBLE) {
                    totalHeight += 2;
                } else {
                    totalHeight += 1;
                }

                blockStatesToSet.add(Pair.of(checkBlockPos.immutable(), checkBlockState));
            } else {
                break;
            }
        }

        for (int yo = y + 1; yo < level.getMaxBuildHeight(); yo++) {
            BlockPos checkBlockPos = new BlockPos(blockPos.getX(), yo, blockPos.getZ());
            BlockState checkBlockState = level.getBlockState(checkBlockPos);
            if (checkBlockState.getBlock().equals(thisBlock)) {
                if (checkBlockState.getValue(TYPE) == SlabType.DOUBLE) {
                    totalHeight += 2;
                } else {
                    totalHeight += 1;
                }

                blockStatesToSet.add(Pair.of(checkBlockPos.immutable(), checkBlockState));
            } else {
                break;
            }
        }

       if (totalHeight > maxTotalHeight) {
           totalHeight = maxTotalHeight;
       }

        for (Pair<BlockPos, BlockState> pair : blockStatesToSet) {
            if (pair.getSecond().getValue(BLOCK_HEIGHT) != totalHeight) {
                level.setBlock(pair.getFirst(), pair.getSecond().setValue(BLOCK_HEIGHT, totalHeight), Block.UPDATE_ALL);
            }
        }
    } finally {
        processingPositions.remove(blockPos);
    }
}

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        BlockState blockState0 = blockPlaceContext.getLevel().getBlockState(blockPos);
        if (blockState0.is(this)) {
            return blockState0.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, false).setValue(BLOCK_HEIGHT, 1);
        }
        else {
            FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPos);
            BlockState blockState1 = this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(BLOCK_HEIGHT, 1);
            Direction direction = blockPlaceContext.getClickedFace();
            return direction != Direction.DOWN && (direction == Direction.UP || !(blockPlaceContext.getClickLocation().y - (double)blockPos.getY() > 0.5)) ? blockState1 : blockState1.setValue(TYPE, SlabType.TOP);
        }
    }

    protected boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        ItemStack itemStack = blockPlaceContext.getItemInHand();
        SlabType slabType = blockState.getValue(TYPE);
        if (slabType != SlabType.DOUBLE && itemStack.is(this.asItem())) {
            if (blockPlaceContext.replacingClickedOnBlock()) {
                boolean b = blockPlaceContext.getClickLocation().y - (double)blockPlaceContext.getClickedPos().getY() > 0.5;
                Direction direction = blockPlaceContext.getClickedFace();
                if (slabType == SlabType.BOTTOM) {
                    return direction == Direction.UP || b && direction.getAxis().isHorizontal();
                }
                else {
                    return direction == Direction.DOWN || !b && direction.getAxis().isHorizontal();
                }
            }
            else {
                return true;
            }
        } else {
            return false;
        }
    }

    protected @NotNull FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    public boolean placeLiquid(@NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, BlockState blockState, @NotNull FluidState fluidState) {
        return blockState.getValue(TYPE) != SlabType.DOUBLE && SimpleWaterloggedBlock.super.placeLiquid(levelAccessor, blockPos, blockState, fluidState);
    }

    public boolean canPlaceLiquid(@Nullable Player player, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, BlockState blockState, @NotNull Fluid fluid) {
        return blockState.getValue(TYPE) != SlabType.DOUBLE && SimpleWaterloggedBlock.super.canPlaceLiquid(player, blockGetter, blockPos, blockState, fluid);
    }

    protected boolean isPathfindable(@NotNull BlockState blockState, @NotNull PathComputationType pathComputationType) {
		if (Objects.requireNonNull(pathComputationType) == PathComputationType.WATER) {
			return blockState.getFluidState().is(FluidTags.WATER);
		}
		return false;
	}

    static {
        TYPE = BlockStateProperties.SLAB_TYPE;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        BLOCK_HEIGHT = ParkourBlockStateProperties.BLOCK_HEIGHT;
        BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        TOP_AABB = Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);
    }
}
