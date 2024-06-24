package com.natamus.letsparkour.block.specific;

import com.natamus.letsparkour.block.base.ParkourSlab;
import com.natamus.letsparkour.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SoftParkourSlab extends ParkourSlab {
    public SoftParkourSlab(Properties properties) {
        super(properties);
    }

    public void fallOn(Level level, @NotNull BlockState state, @NotNull BlockPos pos, Entity entity, float fallDistance) {
		if (!ConfigHandler.enableSoftParkourBlock) {
			return;
		}

        entity.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall());
    }
}