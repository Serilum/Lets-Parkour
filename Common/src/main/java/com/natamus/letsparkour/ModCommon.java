package com.natamus.letsparkour;

import com.natamus.collective.functions.CreativeModeTabFunctions;
import com.natamus.collective.services.Services;
import com.natamus.letsparkour.block.base.ParkourSlab;
import com.natamus.letsparkour.block.specific.*;
import com.natamus.letsparkour.config.ConfigHandler;
import com.natamus.letsparkour.data.ParkourBlocks;
import com.natamus.letsparkour.util.Reference;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModCommon {

	public static void init() {
		ConfigHandler.initConfig();
		load();
	}

	private static void load() {

	}

	public static void registerAssets(Object modEventBusObject) {
		ResourceKey<CreativeModeTab> functionalBlocksResourceKey = CreativeModeTabFunctions.getCreativeModeTabResourceKey("functional_blocks");

		Services.REGISTERBLOCK.registerBlockWithItem(modEventBusObject, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "base_parkour_slab"), () -> new ParkourSlab(BlockBehaviour.Properties.of().strength(1.2F, 6.0F)), functionalBlocksResourceKey);
		Services.REGISTERBLOCK.registerBlockWithItem(modEventBusObject, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "soft_parkour_slab"), () -> new SoftParkourSlab(BlockBehaviour.Properties.of().strength(1.2F, 6.0F)), functionalBlocksResourceKey);

		Services.REGISTERBLOCK.registerBlockWithItem(modEventBusObject, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "slow_parkour_slab"), () -> new SlowParkourSlab(BlockBehaviour.Properties.of().strength(1.2F, 6.0F)), functionalBlocksResourceKey);
		Services.REGISTERBLOCK.registerBlockWithItem(modEventBusObject, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "fast_parkour_slab"), () -> new FastParkourSlab(BlockBehaviour.Properties.of().strength(1.2F, 6.0F)), functionalBlocksResourceKey);

		Services.REGISTERBLOCK.registerBlockWithItem(modEventBusObject, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "jelly_parkour_slab"), () -> new JellyParkourSlab(BlockBehaviour.Properties.of().strength(1.2F, 6.0F).forceSolidOn().noCollission().isViewBlocking((a, b, c) -> { return false; })), functionalBlocksResourceKey);
		Services.REGISTERBLOCK.registerBlockWithItem(modEventBusObject, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "jump_parkour_slab"), () -> new JumpParkourSlab(BlockBehaviour.Properties.of().strength(1.2F, 6.0F)), functionalBlocksResourceKey);
		Services.REGISTERBLOCK.registerBlockWithItem(modEventBusObject, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "slippery_parkour_slab"), () -> new SlipperyParkourSlab(BlockBehaviour.Properties.of().strength(1.2F, 6.0F)), functionalBlocksResourceKey);

		Services.REGISTERBLOCK.registerBlockWithItem(modEventBusObject, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "illusion_parkour_slab"), () -> new IllusionParkourSlab(BlockBehaviour.Properties.of().strength(1.2F, 6.0F).noCollission()), functionalBlocksResourceKey);

		Services.REGISTERBLOCK.registerBlockWithItem(modEventBusObject, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "trapdoor_parkour_slab"), () -> new TrapdoorParkourSlab(BlockBehaviour.Properties.of().strength(1.2F, 6.0F)), functionalBlocksResourceKey, true);
	}

	public static void setAssets() {
		ParkourBlocks.BASE_PARKOUR_SLAB = (ParkourSlab)Services.REGISTERBLOCK.getRegisteredBlockWithItem(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "base_parkour_slab"));
		ParkourBlocks.SOFT_PARKOUR_SLAB = (SoftParkourSlab)Services.REGISTERBLOCK.getRegisteredBlockWithItem(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "soft_parkour_slab"));

		ParkourBlocks.SLOW_PARKOUR_SLAB = (SlowParkourSlab)Services.REGISTERBLOCK.getRegisteredBlockWithItem(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "slow_parkour_slab"));
		ParkourBlocks.FAST_PARKOUR_SLAB = (FastParkourSlab)Services.REGISTERBLOCK.getRegisteredBlockWithItem(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "fast_parkour_slab"));

		ParkourBlocks.JELLY_PARKOUR_SLAB = (JellyParkourSlab)Services.REGISTERBLOCK.getRegisteredBlockWithItem(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "jelly_parkour_slab"));
		ParkourBlocks.JUMP_PARKOUR_SLAB = (JumpParkourSlab)Services.REGISTERBLOCK.getRegisteredBlockWithItem(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "jump_parkour_slab"));
		ParkourBlocks.SLIPPERY_PARKOUR_SLAB = (SlipperyParkourSlab)Services.REGISTERBLOCK.getRegisteredBlockWithItem(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "slippery_parkour_slab"));

		ParkourBlocks.ILLUSION_PARKOUR_SLAB = (IllusionParkourSlab)Services.REGISTERBLOCK.getRegisteredBlockWithItem(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "illusion_parkour_slab"));
		ParkourBlocks.TRAPDOOR_PARKOUR_SLAB = (TrapdoorParkourSlab)Services.REGISTERBLOCK.getRegisteredBlockWithItem(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "trapdoor_parkour_slab"));
	}
}