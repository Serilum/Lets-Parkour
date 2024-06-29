package com.natamus.letsparkour.mixin;

import com.natamus.letsparkour.block.base.ParkourSlab;
import com.natamus.letsparkour.block.specific.FastParkourSlab;
import com.natamus.letsparkour.block.specific.JumpParkourSlab;
import com.natamus.letsparkour.block.specific.SlowParkourSlab;
import com.natamus.letsparkour.block.type.SpeedParkourSlab;
import com.natamus.letsparkour.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, priority = 1001)
public abstract class EntityMixin {
    @Shadow private Level level;
    @Shadow private BlockPos blockPosition;

	@Inject(method = "getBlockJumpFactor()F", at = @At(value = "HEAD"), cancellable = true)
	public void getBlockJumpFactor(CallbackInfoReturnable<Float> cir) {
		if (!ConfigHandler.enableJumpParkourBlock) {
			return;
		}

		BlockState blockState = this.level.getBlockState(this.blockPosition);
		if (!(blockState.getBlock() instanceof JumpParkourSlab)) {
			blockState = this.level.getBlockState(this.blockPosition.below());
			if (!(blockState.getBlock() instanceof JumpParkourSlab)) {
				return;
			}
		}

		cir.setReturnValue((float)ConfigHandler.jumpBlockBaseJumpFactor * (1 + ((blockState.getValue(ParkourSlab.BLOCK_HEIGHT) - 1) * 0.25F)));
	}

	@Inject(method = "getBlockSpeedFactor()F", at = @At(value = "HEAD"), cancellable = true)
	public void getBlockSpeedFactor(CallbackInfoReturnable<Float> cir) {
		BlockState blockState = this.level.getBlockState(this.blockPosition);
		if (!(blockState.getBlock() instanceof SpeedParkourSlab)) {
			blockState = this.level.getBlockState(this.blockPosition.below());
			if (!(blockState.getBlock() instanceof SpeedParkourSlab)) {
				return;
			}
		}

		Entity entity = (Entity)(Object)this;
		if (entity.getDeltaMovement().y != 0) {
			return;
		}

		Block block = blockState.getBlock();
		if (ConfigHandler.enableFastParkourBlock && block instanceof FastParkourSlab) {
			cir.setReturnValue((float) ConfigHandler.fastBlockBaseSpeedFactor * (1 + ((blockState.getValue(ParkourSlab.BLOCK_HEIGHT) - 1) * 0.25F)));
		}
		else if (ConfigHandler.enableSlowParkourBlock && block instanceof SlowParkourSlab) {
			cir.setReturnValue((float) ConfigHandler.slowBlockBaseSpeedFactor / (1 + ((blockState.getValue(ParkourSlab.BLOCK_HEIGHT) - 1) * 1.0F)));
		}
	}
}
