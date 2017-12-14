package leviathan143.loottweaker.common;

import java.io.File;

import crafttweaker.mc1120.commands.CTChatCommand;
import leviathan143.loottweaker.common.commands.CommandLootTables;
import leviathan143.loottweaker.common.handlers.DropHandler;
import leviathan143.loottweaker.common.tweakers.loot.LootTableTweaker;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		registerRenders();
		MinecraftForge.EVENT_BUS.register(LootTableTweaker.class);
		MinecraftForge.EVENT_BUS.register(DropHandler.class);
	}

	public void init(FMLInitializationEvent event)
	{
		CTChatCommand.registerCommand(new CommandLootTables());
	}

	public void postInit(FMLPostInitializationEvent event)
	{
		DeprecationWarningManager.printDeprecationWarnings();
	}

	public void registerRenders()
	{

	}

	public World getWorld()
	{
		return null;
	}

	public File getMCFolder()
	{
		return null;
	}

	public void serverStarted(FMLServerStartedEvent event)
	{}

	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{

	}
}
