package leviathan143.loottweaker.common;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import crafttweaker.mc1120.commands.CTChatCommand;
import leviathan143.loottweaker.common.command.CommandLootTables;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = LootTweaker.MODID, name = LootTweaker.MODNAME, version = LootTweaker.VERSION, dependencies = LootTweaker.DEPENDENCIES)
public class LootTweaker
{
	public static final String MODNAME = "LootTweaker";
	public static final String MODID = "loottweaker";
	public static final String VERSION = "GRADLE:VERSION";
	public static final String DEPENDENCIES = "required-after:crafttweaker; before:jeresources; required:forge@[14.23.5.2779,);";
	public static final LootTweakerContext CONTEXT = new LootTweakerContext(new CTLoggingErrorHandler());
	private static final Logger LOGGER = LogManager.getLogger(MODNAME);

	@NetworkCheckHandler
	public boolean networkCheck(Map<String, String> modVersions, Side remoteSide)
	{
	    //Reject vanilla clients or servers
	    if (!modVersions.containsKey("forge"))
	        return false;
	    String remoteLTVersion = modVersions.get(MODID);
	    //Client without can connect to server with, but not vice versa
	    if (remoteLTVersion == null)
	    {
	        if (remoteSide == Side.CLIENT)
	        {
	            LOGGER.info("Accepted non-existent client LootTweaker install");
	            return true;
	        }
	        else
	        {
	            LOGGER.info("Rejected non-existent server LootTweaker install");
	            return false;
	        }
	    }
	    //Network compatibility is not guaranteed between versions
	    if (!remoteLTVersion.equals(VERSION))
	    {
	        LOGGER.info("Rejected {} LootTweaker install because its version {} differs from local version {}",
	            remoteSide.name().toLowerCase(), remoteLTVersion, VERSION);
            return false;
	    }
        return true;
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LTConfig.onLoad();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		CTChatCommand.registerCommand(new CommandLootTables());
	}

	@Mod.EventHandler
	public void serverStarted(FMLServerStartedEvent event)
	{
		DeprecationWarningManager.printDeprecationWarnings();
	}
}
