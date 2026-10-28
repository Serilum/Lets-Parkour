package com.serilum.letsparkour.block.type;

import com.serilum.letsparkour.block.base.ParkourSlab;

public class SpeedParkourSlab extends ParkourSlab {
	public SpeedParkourSlab(Properties properties) {
		super(properties);
	}

	public float getSpeedFactor() {
		return this.speedFactor;
	}
}