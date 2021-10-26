package io.github.daomephsta.loottweaker.test.zenscript;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.LootTableTweakManager;
import leviathan143.loottweaker.common.zenscript.ZenLootTableTweakManager;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Checks that all classes and methods are registered with and callable by Zenscript.
 * Functionality is tested elsewhere using the backing Java methods directly.
 * @author Daomephsta
 *
 */
public class ZenscriptTests
{
    @RegisterExtension
    public static final CraftTweakerLoggerRedirect redirect = new CraftTweakerLoggerRedirect(LogManager.getFormatterLogger("Saddle.LootTweaker"));

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void jsonTests()
    {
        ScriptRunner.run("scripts/json-tests.zs");
        loadTweakedTables();
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void itemEntryAddition()
    {
        ScriptRunner.run("scripts/item-entry-addition.zs");
        loadTweakedTables();
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void lootTableEntryAddition()
    {
        ScriptRunner.run("scripts/loot-table-entry-addition.zs");
        loadTweakedTables();
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void emptyEntryAddition()
    {
        ScriptRunner.run("scripts/empty-entry-addition.zs");
        loadTweakedTables();
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void entryNaming()
    {
        ScriptRunner.run("scripts/entry-naming.zs");
        loadTweakedTables();
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void miscPoolTests()
    {
        ScriptRunner.run("scripts/misc-pool-tests.zs");
        loadTweakedTables();
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void lootConditionFactory()
    {
        ScriptRunner.run("scripts/loot-condition-factory.zs");
        loadTweakedTables();
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void lootFunctionFactory()
    {
        ScriptRunner.run("scripts/loot-function-factory.zs");
        loadTweakedTables();
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void createTable()
    {
        ScriptRunner.run("scripts/create-table.zs");
        loadTweakedTables();
    }

    private void loadTweakedTables()
    {
        LootTableTweakManager tweakManager = ObfuscationReflectionHelper.getPrivateValue(ZenLootTableTweakManager.class, null, "TWEAK_MANAGER");
        Map<ResourceLocation, ZenLootTableWrapper> tweakedTables = ObfuscationReflectionHelper.getPrivateValue(LootTableTweakManager.class, tweakManager, "tweakedTables");
        for (ResourceLocation tweakedTable : tweakedTables.keySet())
            tweakManager.tweakTable(tweakedTable, TestUtils.loadTable(tweakedTable));
    }
}
