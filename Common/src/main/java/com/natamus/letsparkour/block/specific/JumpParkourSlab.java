package com.natamus.letsparkour.block.specific;

import com.natamus.letsparkour.block.base.ParkourSlab;

public class JumpParkourSlab extends ParkourSlab {
    public JumpParkourSlab(Properties properties) {
        super(properties);
    }

    public float getJumpFactor() {
        return this.jumpFactor;
    }
}
