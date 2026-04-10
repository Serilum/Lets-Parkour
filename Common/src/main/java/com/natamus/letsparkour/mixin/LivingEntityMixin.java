package com.natamus.letsparkour.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = LivingEntity.class, priority = 1001)
public abstract class LivingEntityMixin {
	/*@ModifyVariable(method = "travelInAir(Lnet/minecraft/world/phys/Vec3;)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z"))
	public float travel_f(float f, Vec3 vec3) {
		if (!ConfigHandler.enableSlipperyParkourBlock) {
			return f;
		}

		LivingEntity livingEntity = (LivingEntity)(Object)this;
		BlockState blockState = livingEntity.level().getBlockState(livingEntity.blockPosition());
		if (!(blockState.getBlock() instanceof SlipperyParkourSlab)) {
			blockState = livingEntity.level().getBlockState(livingEntity.blockPosition().below());
			if (!(blockState.getBlock() instanceof SlipperyParkourSlab)) {
				return f;
			}
		}

		return Math.min(0.9999F, (float)ConfigHandler.slipperyBlockBaseFrictionFactor + ((blockState.getValue(ParkourSlab.BLOCK_HEIGHT) - 1) * 0.025F));
	}*/
}