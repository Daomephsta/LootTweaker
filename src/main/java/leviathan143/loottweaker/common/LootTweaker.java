package leviathan143.loottweaker.common;

import crafttweaker.mc1120.commands.CTChatCommand;
import leviathan143.loottweaker.common.command.CommandLootTables;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.ZenLootTableTweakManager;
import leviathan143.loottweaker.common.zenscript.factory.ZenLambdaLootCondition;
import leviathan143.loottweaker.common.zenscript.factory.ZenLambdaLootFunction;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;


@Mod(modid = LootTweaker.MODID, name = LootTweaker.MODNAME, version = LootTweaker.VERSION, dependencies = LootTweaker.DEPENDENCIES)
public class LootTweaker
{
    public static final String MODNAME = "LootTweaker";
    public static final String MODID = "loottweaker";
    public static final String VERSION = "GRADLE:VERSION";
    public static final String DEPENDENCIES = "required-after:crafttweaker@[4.1.20,); before:jeresources; required:forge@[14.23.5.2779,);";
    public static final LootTweakerContext CONTEXT = new LootTweakerContext(new CTLoggingErrorHandler());

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LootTweakerNetworkChecker.install();
        LTConfig.onLoad();
        LootFunctionManager.registerFunction(ZenLambdaLootFunction.SERIALISER);
        LootConditionManager.registerCondition(ZenLambdaLootCondition.SERIALISER);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        CTChatCommand.registerCommand(new CommandLootTables());
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        ZenLootTableTweakManager.onServerStarting(event);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event)
    {
        DeprecationWarningManager.printDeprecationWarnings();
    }

    public static TextComponentTranslation translation(String keySuffix, Object... args)
    {
        return new TextComponentTranslation(MODID + keySuffix, args);
    }
}
