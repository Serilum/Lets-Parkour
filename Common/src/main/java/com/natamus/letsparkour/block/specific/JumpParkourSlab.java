package com.natamus.letsparkour.block.specific;

import com.natamus.letsparkour.block.base.ParkourSlab;
import com.natamus.letsparkour.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class JumpParkourSlab extends ParkourSlab {
    public JumpParkourSlab(Properties properties) {
        super(properties);
    }

    public float getJumpFactor() {
        return this.jumpFactor;
    }

    public void fallOn(@NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
		if (!ConfigHandler.enableJumpParkourBlock) {
			return;
		}

        entity.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall());
    }
}
