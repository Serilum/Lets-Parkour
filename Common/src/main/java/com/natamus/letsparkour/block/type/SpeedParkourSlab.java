package com.natamus.letsparkour.block.type;

import com.natamus.letsparkour.block.base.ParkourSlab;

public class SpeedParkourSlab extends ParkourSlab {
    public SpeedParkourSlab(Properties properties) {
        super(properties);
    }

    public float getSpeedFactor() {
        return this.speedFactor;
    }
}