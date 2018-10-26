package leviathan143.loottweaker.common;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CTChatCommand;
import leviathan143.loottweaker.common.commands.CommandLootTables;
import leviathan143.loottweaker.common.zenscript.ModConditionHelper;
import leviathan143.loottweaker.common.zenscript.ModFunctionHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.*;

public abstract class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		//Register global symbols
		CraftTweakerAPI.registerGlobalSymbol("modConditions", CraftTweakerAPI.getJavaStaticFieldSymbol(ModConditionHelper.class, "INSTANCE"));
		CraftTweakerAPI.registerGlobalSymbol("modFunctions", CraftTweakerAPI.getJavaStaticFieldSymbol(ModFunctionHelper.class, "INSTANCE"));
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
