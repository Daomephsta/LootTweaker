package leviathan143.loottweaker.common;

import crafttweaker.mc1120.commands.CTChatCommand;
import leviathan143.loottweaker.common.commands.CommandLootTables;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

public abstract class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		LTConfig.onLoad();
	}

	public void init(FMLInitializationEvent event)
	{
		CTChatCommand.registerCommand(new CommandLootTables());
	}

	public void postInit(FMLPostInitializationEvent event)
	{
		
	}

	public abstract World getWorld();

	public void serverStarted(FMLServerStartedEvent event)
	{
		DeprecationWarningManager.printDeprecationWarnings();
	}

	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{

	}
}
