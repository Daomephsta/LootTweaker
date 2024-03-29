package leviathan143.loottweaker.common;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Unique;

import crafttweaker.mc1120.commands.CTChatCommand;
import crafttweaker.zenscript.GlobalRegistry;
import leviathan143.loottweaker.common.command.CommandLootTables;
import leviathan143.loottweaker.common.duck.LootLoadingContext;
import leviathan143.loottweaker.common.mixin.LootEntryAccessors;
import leviathan143.loottweaker.common.mixin.LootPoolAccessors;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.ZenLootTableTweakManager;
import leviathan143.loottweaker.common.zenscript.factory.ZenLambdaLootCondition;
import leviathan143.loottweaker.common.zenscript.factory.ZenLambdaLootFunction;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
    public static final String DEPENDENCIES = "required-after:crafttweaker@[4.1.20,); before:jeresources; required:forge@[14.23.5.2779,);";
    public static final LootTweakerContext CONTEXT = new LootTweakerContext(new CTLoggingErrorHandler());

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LootTweakerNetworkChecker.install();
        LTConfig.onLoad();
        LootFunctionManager.registerFunction(ZenLambdaLootFunction.SERIALISER);
        LootConditionManager.registerCondition(ZenLambdaLootCondition.SERIALISER);
        registerAliases("Conditions", "Functions", "LootCondition", "LootFunction", "LootPool", "LootTable");
    }

    private void registerAliases(String... aliased)
    {
        SymbolPackage loottweaker = (SymbolPackage) GlobalRegistry.getRoot().get(LootTweaker.MODID);
        SymbolPackage loottweakerVanilla = new SymbolPackage("loottweaker.vanilla");
        loottweaker.put("vanilla", loottweakerVanilla, GlobalRegistry.getErrors());
        SymbolPackage loottweakerVanillaLoot = new SymbolPackage("loottweaker.vanilla.loot");
        loottweakerVanilla.put("loot", loottweakerVanillaLoot, GlobalRegistry.getErrors());

        for (String simpleName : aliased)
            loottweakerVanillaLoot.put(simpleName, loottweaker.get(simpleName), GlobalRegistry.getErrors());
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
    @Unique
    public
    static final Logger LOOT_TWEAKER_SANITY_LOGGER = LogManager.getLogger(LootTweaker.MODID + ".sanity_checks");

    public static void extracted(LootPool pool, LootEntry[] entries, String name)
    {
        Set<String> usedNames = new HashSet<>();
        if (name.startsWith("custom#"))
        {
            name = "loottweaker_fixed_pool_" + LootLoadingContext.get().getPoolDiscriminator();
            LOOT_TWEAKER_SANITY_LOGGER.error(
                "Pool with custom flag found in non-custom table '{}'. Renamed to '{}'.\n" +
                "Report this to the loot adder.", LootLoadingContext.get().tableId, name);
            ((LootPoolAccessors) pool).setName(name);
        }
        for (LootEntry entry : entries)
        {
            if (!usedNames.add(entry.getEntryName()))
            {
                String error = String.format("Duplicate entry name '%s' in pool '%s' of table '{}'",
                    entry.getEntryName(), name, LootLoadingContext.get().tableId);
                if (!(boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
                {
                    String newName = entry.getEntryName() + LootLoadingContext.get().getEntryDiscriminator();
                    LOOT_TWEAKER_SANITY_LOGGER.error(
                        "{}. Duplicate added as '{}'.\n" +
                        "Report this to the loot adder.", error, newName);
                    ((LootEntryAccessors) entry).setName(newName);
                }
                else
                    throw new IllegalArgumentException(error);
            }
            else if (entry.getEntryName().startsWith("custom#"))
            {
                String newName = "loottweaker_fixed_entry_" + LootLoadingContext.get().getEntryDiscriminator();
                LOOT_TWEAKER_SANITY_LOGGER.error(
                    "Entry with custom flag found in non-custom table '{}'. Renamed to '{}'.\n" +
                    "Report this to the loot adder.", LootLoadingContext.get().tableId, newName);
                ((LootEntryAccessors) entry).setName(newName);
            }
        }
    }

    public static TextComponentTranslation translation(String keySuffix, Object... args)
    {
        return new TextComponentTranslation(MODID + keySuffix, args);
    }
}
