package com.natamus.letsparkour.mixin;

import com.natamus.letsparkour.block.base.ParkourSlab;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1001)
public abstract class ServerGamePacketListenerImplMixin {
	@Shadow public ServerPlayer player;

	@ModifyVariable(
			method = "handleMovePlayer(Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket;)V",
			at = @At("STORE"),
			slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isFallFlying()Z")),
			ordinal = 0
	)
	public boolean overrideMovementCheckIfParkouring(boolean isFallFlying) {
		Level level = player.level();
		BlockPos playerPos = player.blockPosition();
		return isFallFlying || level.getBlockState(playerPos).getBlock() instanceof ParkourSlab || level.getBlockState(playerPos.below()).getBlock() instanceof ParkourSlab;
	}
}
