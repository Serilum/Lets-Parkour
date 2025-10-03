package com.natamus.letsparkour.data;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ParkourBlockStateProperties {
	public static final IntegerProperty BLOCK_HEIGHT;

	static {
		BLOCK_HEIGHT = IntegerProperty.create("block_height", 0, 25);
	}
}
