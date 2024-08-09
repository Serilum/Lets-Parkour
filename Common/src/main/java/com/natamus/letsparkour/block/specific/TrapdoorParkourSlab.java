package com.natamus.letsparkour.block.specific;

import com.google.common.collect.ImmutableMap;
import com.natamus.letsparkour.block.base.ParkourSlab;
import com.natamus.letsparkour.config.ConfigHandler;
import com.natamus.letsparkour.data.ParkourBlocks;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Tilt;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public class TrapdoorParkourSlab extends ParkourSlab {
    private static final EnumProperty<Tilt> TILT;
    private static final Object2IntMap<Tilt> DELAY_UNTIL_NEXT_TILT_STATE;

    private static final Map<Tilt, VoxelShape> TRAPDOOR_SHAPES;
    private final Map<BlockState, VoxelShape> shapesCache;

    public TrapdoorParkourSlab(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH).setValue(TILT, Tilt.NONE));
        this.shapesCache = this.getShapeForEachState(TrapdoorParkourSlab::calculateShape);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(TYPE, WATERLOGGED, FACING, TILT, BLOCK_HEIGHT);
    }

    private static VoxelShape calculateShape(BlockState state) {
        return Shapes.or(TRAPDOOR_SHAPES.get(state.getValue(TILT)));
    }

    protected static boolean place(LevelAccessor level, BlockPos pos, FluidState fluidState, Direction direction) {
        BlockState blockState = ParkourBlocks.TRAPDOOR_PARKOUR_SLAB.defaultBlockState().setValue(FACING, direction);
        return level.setBlock(pos, blockState, 3);
    }

    protected void onProjectileHit(@NotNull Level level, @NotNull BlockState state, BlockHitResult hit, @NotNull Projectile projectile) {
        this.setTiltAndScheduleTick(state, level, hit.getBlockPos(), Tilt.FULL, SoundEvents.COPPER_HIT);
    }

    protected void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!level.isClientSide) {
            if (state.getValue(TILT) == Tilt.NONE && canEntityTilt(pos, entity) && !level.hasNeighborSignal(pos)) {
                this.setTiltAndScheduleTick(state, level, pos, Tilt.UNSTABLE, null);
            }
        }
    }

    public void tick(@NotNull BlockState blockState, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (serverLevel.hasNeighborSignal(blockPos)) {
            resetTilt(blockState, serverLevel, blockPos);
        } else {
            Tilt tilt = blockState.getValue(TILT);
            if (tilt == Tilt.UNSTABLE) {
                this.setTiltAndScheduleTick(blockState, serverLevel, blockPos, Tilt.PARTIAL, SoundEvents.COPPER_HIT);
            }
            else if (tilt == Tilt.PARTIAL) {
                this.setTiltAndScheduleTick(blockState, serverLevel, blockPos, Tilt.FULL, SoundEvents.COPPER_HIT);
            }
            else if (tilt == Tilt.FULL) {
                resetTilt(blockState, serverLevel, blockPos);
            }
        }
    }

    public void neighborChanged(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Block block, @NotNull BlockPos blockPos2, boolean bl) {
        if (level.hasNeighborSignal(blockPos)) {
            resetTilt(blockState, level, blockPos);
        }
    }

    private static void playTiltSound(Level level, BlockPos pos, SoundEvent sound) {
        float f = Mth.randomBetween(level.random, 0.8F, 1.2F);
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, f);
    }

    private static boolean canEntityTilt(BlockPos blockPos, Entity entity) {
        return entity.onGround() && entity.position().y > (double)((float)blockPos.getY() + 0.6875F);
    }

    private void setTiltAndScheduleTick(BlockState state, Level level, BlockPos pos, Tilt tilt, @Nullable SoundEvent sound) {
		if (!ConfigHandler.enableTrapdoorParkourBlock) {
			return;
		}

        setTilt(state, level, pos, tilt);
        if (sound != null) {
            playTiltSound(level, pos, sound);
        }

        int i = DELAY_UNTIL_NEXT_TILT_STATE.getInt(tilt);
        if (i != -1) {
            level.scheduleTick(pos, this, i);
        }
    }

    private static void resetTilt(BlockState blockState, Level level, BlockPos blockPos) {
        setTilt(blockState, level, blockPos, Tilt.NONE);
        if (blockState.getValue(TILT) != Tilt.NONE) {
            playTiltSound(level, blockPos, SoundEvents.COPPER_HIT);
        }
    }

    private static void setTilt(BlockState blockState, Level level, BlockPos blockPos, Tilt tilt) {
        Tilt tilt2 = blockState.getValue(TILT);
        level.setBlock(blockPos, blockState.setValue(TILT, tilt), 2);
        if (tilt.causesVibration() && tilt != tilt2) {
            level.gameEvent(null, GameEvent.BLOCK_CHANGE, blockPos);
        }
    }

    public @NotNull VoxelShape getCollisionShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return TRAPDOOR_SHAPES.get(blockState.getValue(TILT));
    }

    public @NotNull VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return this.shapesCache.get(blockState);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().below());
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        boolean bl = blockState.is(ParkourBlocks.TRAPDOOR_PARKOUR_SLAB);
        return this.defaultBlockState().setValue(FACING, bl ? blockState.getValue(FACING) : context.getHorizontalDirection().getOpposite());
    }

    static {
        TILT = BlockStateProperties.TILT;
		DELAY_UNTIL_NEXT_TILT_STATE = Util.make(new Object2IntArrayMap<>(), (object2IntArrayMap) -> {
            object2IntArrayMap.defaultReturnValue(-1);
            object2IntArrayMap.put(Tilt.UNSTABLE, 10);
			object2IntArrayMap.put(Tilt.PARTIAL, 10);
            object2IntArrayMap.put(Tilt.FULL, 100);
        });
        TRAPDOOR_SHAPES = ImmutableMap.of(Tilt.NONE, Block.box(0.0, 8.0, 0.0, 16.0, 15.0, 16.0), Tilt.UNSTABLE, Block.box(0.0, 8.0, 0.0, 16.0, 15.0, 16.0), Tilt.PARTIAL, Block.box(0.0, 8.0, 0.0, 16.0, 13.0, 16.0), Tilt.FULL, Shapes.empty());
    }
}
