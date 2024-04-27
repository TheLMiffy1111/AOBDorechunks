package com.rcx.aobdorechunks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER, dependencies = Reference.DEPENDENCIES)
public class AOBDOC {
	
	//@SidedProxy(serverSide="com.rcx.aobdorechunks.proxy.CommonProxy",clientSide="com.rcx.aobdorechunks.proxy.ClientProxy")
	//public static CommonProxy proxy;

	@Instance(Reference.MOD_ID)
	public static AOBDOC instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new OreSwapper());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
}
