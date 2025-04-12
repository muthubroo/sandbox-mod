package com.muthubroo.sandboxmod;

import com.muthubroo.sandboxmod.entity.ModEntities;
import com.muthubroo.sandboxmod.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SandboxMod implements ModInitializer {
	public static final String MOD_ID = "sandbox-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerItems();
		ModEntities.registerEntities();

		LOGGER.info("Initialization complete.");
	}
}