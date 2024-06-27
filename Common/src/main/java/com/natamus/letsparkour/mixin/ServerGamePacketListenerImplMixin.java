package com.natamus.letsparkour.mixin;

import com.natamus.letsparkour.block.base.ParkourSlab;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1001)
public abstract class ServerGamePacketListenerImplMixin {
	@Shadow public ServerPlayer player;

	@Inject(method = "handleMovePlayer(Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;isSingleplayerOwner()Z"), cancellable = true)
	public void handleMovePlayer(ServerboundMovePlayerPacket $$0, CallbackInfo ci) {
		Level level = player.level();
		BlockPos playerPos = player.blockPosition();
		if (level.getBlockState(playerPos).getBlock() instanceof ParkourSlab || level.getBlockState(playerPos.below()).getBlock() instanceof ParkourSlab) {
			ci.cancel();
		}
	}
}
