package com.natamus.letsparkour.block.specific;

import com.natamus.letsparkour.block.base.ParkourSlab;
import com.natamus.letsparkour.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class JellyParkourSlab extends ParkourSlab {
    public JellyParkourSlab(Properties properties) {
        super(properties);
    }

    public @NotNull VoxelShape getVisualShape(@NotNull BlockState $$0, @NotNull BlockGetter $$1, @NotNull BlockPos $$2, @NotNull CollisionContext $$3) {
        return Shapes.empty();
    }

    public float getShadeBrightness(@NotNull BlockState $$0, @NotNull BlockGetter $$1, @NotNull BlockPos $$2) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(@NotNull BlockState $$0, @NotNull BlockGetter $$1, @NotNull BlockPos $$2) {
        return true;
    }

    protected void entityInside(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Entity entity) {
		if (!ConfigHandler.enableJellyParkourBlock) {
			return;
		}

        float stuckSpeed = 1.0F / (blockState.getValue(ParkourSlab.BLOCK_HEIGHT) * 1.2F);
        entity.makeStuckInBlock(blockState, new Vec3(stuckSpeed, stuckSpeed, stuckSpeed));
    }
}