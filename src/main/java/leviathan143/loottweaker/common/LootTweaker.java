package leviathan143.loottweaker.common;

import daomephsta.loot_shared.DaomephstaLootShared;
import daomephsta.loot_shared.utility.zenscript.ZenClasses;
import daomephsta.loot_shared.utility.zenscript.ZenSymbols;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.ZenLootTableTweakManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import stanhebben.zenscript.symbols.SymbolPackage;


@Mod(modid = LootTweaker.MODID, name = LootTweaker.MODNAME, version = LootTweaker.VERSION, dependencies = LootTweaker.DEPENDENCIES)
public class LootTweaker
{
    public static final String MODNAME = "LootTweaker";
    public static final String MODID = "loottweaker";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDENCIES = "required-after:crafttweaker@[4.1.20,); required-after:daomephsta_loot_shared; before:jeresources; required:forge@[14.23.5.2779,);";
    public static final String ZEN_PACKAGE = MODID;
    public static final LootTweakerContext CONTEXT = new LootTweakerContext(new CTLoggingErrorHandler());

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LootTweakerNetworkChecker.install();
        LTConfig.onLoad();
        registerDLSAliases("LootCondition", "CustomLootCondition", "LootFunction", "CustomLootFunction");
        registerLegacyAliases("Conditions", "Functions", "LootCondition", "LootFunction", "LootPool", "LootTable");
    }

    private void registerDLSAliases(String... aliased)
    {
        for (String simpleName : aliased)
            ZenClasses.registerAlias(ZEN_PACKAGE + "." + simpleName, DaomephstaLootShared.ZEN_PACKAGE + "." + simpleName);
    }

    private void registerLegacyAliases(String... aliased)
    {
    	SymbolPackage legacyPackage = ZenSymbols.createPackage("loottweaker.vanilla.loot");
        for (String simpleName : aliased)
            ZenClasses.registerAlias(legacyPackage.getName() + "." + simpleName, ZEN_PACKAGE + "." + simpleName);
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
}
