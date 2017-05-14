package leviathan143.loottweaker.common;

import org.apache.logging.log4j.Logger;

import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;


@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)
public class LootTweakerMain 
{	
	public class Constants
	{
		public static final String MODNAME = "LootTweaker";
		public static final  String  MODID = "loottweaker";
		public static final  String  VERSION = "0.0.6.2";
		public static final String DEPENDENCIES = "required-after:MineTweaker3; before:jeresources";
		public static final  String  CLIENT_PROXY_PATH = "leviathan143.loottweaker.client.ClientProxy";
		public static final  String  SERVER_PROXY_PATH = "leviathan143.loottweaker.server.ServerProxy";
	}

	public static Logger logger;
	
	@SidedProxy(serverSide=Constants.SERVER_PROXY_PATH, clientSide=Constants.CLIENT_PROXY_PATH)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
	
	@Mod.EventHandler
	public void serverStarted(FMLServerStartedEvent event)
	{
		proxy.serverStarted(event);
	}
	
	@Mod.EventHandler
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		proxy.serverAboutToStart(event);
	}
}
