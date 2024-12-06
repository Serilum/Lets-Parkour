package com.natamus.letsparkour.config;

import com.natamus.collective.config.DuskConfig;
import com.natamus.letsparkour.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry public static boolean enableSlowParkourBlock = true;
	@Entry public static boolean enableFastParkourBlock = true;
	@Entry public static boolean enableJumpParkourBlock = true;
	@Entry public static boolean enableSoftParkourBlock = true;
	@Entry public static boolean enableJellyParkourBlock = true;
	@Entry public static boolean enableSlipperyParkourBlock = true;
	@Entry public static boolean enableTrapdoorParkourBlock = true;

	@Entry public static double slowBlockBaseSpeedFactor = 0.5;
	@Entry public static double fastBlockBaseSpeedFactor = 1.5;
	@Entry public static double jumpBlockBaseJumpFactor = 2.0;
	@Entry public static double slipperyBlockBaseFrictionFactor = 0.95;

	public static void initConfig() {
		configMetaData.put("enableSlowParkourBlock", Arrays.asList(
			"Whether the slow parkour block functionality should be enabled."
		));
		configMetaData.put("enableFastParkourBlock", Arrays.asList(
			"Whether the fast parkour block functionality should be enabled."
		));
		configMetaData.put("enableJumpParkourBlock", Arrays.asList(
			"Whether the jump parkour block functionality should be enabled."
		));
		configMetaData.put("enableSoftParkourBlock", Arrays.asList(
			"Whether the soft parkour block functionality should be enabled."
		));
		configMetaData.put("enableJellyParkourBlock", Arrays.asList(
			"Whether the jelly parkour block functionality should be enabled."
		));
		configMetaData.put("enableSlipperyParkourBlock", Arrays.asList(
			"Whether the slippery parkour block functionality should be enabled."
		));
		configMetaData.put("enableTrapdoorParkourBlock", Arrays.asList(
			"Whether the trapdoor block functionality should be enabled."
		));
		configMetaData.put("slowBlockBaseSpeedFactor", Arrays.asList(
			"The base speed factor of the slow parkour block."
		));
		configMetaData.put("fastBlockBaseSpeedFactor", Arrays.asList(
			"The base speed factor of the fast parkour block."
		));
		configMetaData.put("jumpBlockBaseJumpFactor", Arrays.asList(
			"The base jump factor of the jump parkour block."
		));
		configMetaData.put("slipperyBlockBaseFrictionFactor", Arrays.asList(
			"The base friction factor of the slippery parkour block."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}