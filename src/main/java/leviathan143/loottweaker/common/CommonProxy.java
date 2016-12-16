package leviathan143.loottweaker.common;

import java.io.File;

import leviathan143.loottweaker.common.commands.CommandLootTables;
import leviathan143.loottweaker.common.handlers.DropHandler;
import leviathan143.loottweaker.common.tweakers.RegisterTweakers;
import leviathan143.loottweaker.common.tweakers.loot.LootTableTweaker;
import minetweaker.MineTweakerImplementationAPI;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;

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
		RegisterTweakers.register();
		MineTweakerImplementationAPI.addMineTweakerCommand(CommandLootTables.NAME, CommandLootTables.DESCRIPTION, new CommandLootTables());
	}

	public void postInit(FMLPostInitializationEvent event)
	{
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
	{
	}
	
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		
	}
}
