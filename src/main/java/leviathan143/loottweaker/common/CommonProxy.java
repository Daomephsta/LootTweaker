package leviathan143.loottweaker.common;

import crafttweaker.mc1120.commands.CTChatCommand;
import leviathan143.loottweaker.common.commands.CommandLootTables;
import leviathan143.loottweaker.common.tweakers.LootTableTweaker;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;

public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(LootTableTweaker.class);
		LTConfig.onLoad();
	}

	public void init(FMLInitializationEvent event)
	{
		CTChatCommand.registerCommand(new CommandLootTables());
	}

	public void postInit(FMLPostInitializationEvent event)
	{
		
	}

	public World getWorld()
	{
		return null;
	}

	public void serverStarted(FMLServerStartedEvent event)
	{
		DeprecationWarningManager.printDeprecationWarnings();
	}

	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{

	}
}
