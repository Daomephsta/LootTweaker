package leviathan143.loottweaker.common;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CTChatCommand;
import leviathan143.loottweaker.common.commands.CommandLootTables;
import leviathan143.loottweaker.common.zenscript.ModConditionHelper;
import leviathan143.loottweaker.common.zenscript.ModFunctionHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		//Register global symbols
		CraftTweakerAPI.registerGlobalSymbol("modConditions", CraftTweakerAPI.getJavaStaticFieldSymbol(ModConditionHelper.class, "INSTANCE"));
		CraftTweakerAPI.registerGlobalSymbol("modFunctions", CraftTweakerAPI.getJavaStaticFieldSymbol(ModFunctionHelper.class, "INSTANCE"));
	}

	public void init(FMLInitializationEvent event)
	{
		CTChatCommand.registerCommand(new CommandLootTables());
	}

	public void postInit(FMLPostInitializationEvent event)
	{
		DeprecationWarningManager.printDeprecationWarnings();
	}

	public World getWorld()
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
