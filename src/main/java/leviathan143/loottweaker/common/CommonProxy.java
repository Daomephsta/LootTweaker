package leviathan143.loottweaker.common;

import java.io.File;

import leviathan143.loottweaker.common.commands.CommandLootTables;
import leviathan143.loottweaker.common.handlers.DropHandler;
import leviathan143.loottweaker.common.loot.block.BlockLootHandler;
import leviathan143.loottweaker.common.loot.block.BlockLootTableManager;
import leviathan143.loottweaker.common.tweakers.RegisterTweakers;
import leviathan143.loottweaker.common.tweakers.loot.LootTableTweaker;
import minetweaker.MineTweakerImplementationAPI;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
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
		RegisterTweakers.register();
		MineTweakerImplementationAPI.addMineTweakerCommand(CommandLootTables.NAME, CommandLootTables.DESCRIPTION, new CommandLootTables());
	}

	public void postInit(FMLPostInitializationEvent event)
	{
		int numTables = 0;
		for(ResourceLocation registryName : Block.REGISTRY.getKeys())
		{
			ResourceLocation blockLootTable = LootUtils.getBlockLootTableFromRegistryName(registryName);
			LootUtils.writeTableToJSON(blockLootTable, new LootTable(LootUtils.NO_POOLS), BlockLootTableManager.getBlockLootTableFilePath(blockLootTable), false);
			BlockLootHandler.registerTable(blockLootTable);
			numTables++;
		}
		BlockLootHandler.initBlockLootTableManager();
		System.out.println(numTables + " block loot tables registered");
		for(ResourceLocation blockLootTable : BlockLootHandler.getBlockLootTables())
		{
			BlockLootHandler.getBlockLootTableManager().getLootTableFromLocation(blockLootTable);
		}
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
