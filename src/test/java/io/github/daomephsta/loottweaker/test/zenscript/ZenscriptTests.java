package io.github.daomephsta.loottweaker.test.zenscript;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.github.daomephsta.loottweaker.test.ThrowingErrorHandler;
import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.CTLoggingErrorHandler;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.impl.ZenScriptPlugin;
import net.minecraft.util.ResourceLocation;

/**
 * Checks that all classes and methods are registered with and callable by
 * Zenscript. Functionality is tested elsewhere using the backing Java methods
 * directly.
 *
 * @author Daomephsta
 *
 */
public class ZenscriptTests
{
    @RegisterExtension
    public static final CraftTweakerLoggerRedirect redirect = new CraftTweakerLoggerRedirect(LogManager.getFormatterLogger("Saddle.LootTweaker"));
    @RegisterExtension
    public static final LootTweakerApiRefresh apiRefresh = new LootTweakerApiRefresh(() -> new LootTweakerContext(new CTLoggingErrorHandler()));
    public static final Iterable<ResourceLocation> TEST_LOOT_TABLES = Stream.of("loottweaker:foo", "loottweaker:bar").map(ResourceLocation::new).collect(Collectors.toSet());

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void tableMethods()
    {
        ScriptRunner.run("scripts/loot-table-methods.zs");
        loadTestTables();
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void miscPoolMethods()
    {
        ScriptRunner.run("scripts/misc-loot-pool-methods.zs");
        loadTestTables();
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void emptyEntryAddition()
    {
        ScriptRunner.run("scripts/empty-entry-addition.zs");
        loadTestTables();
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void lootTableEntryAddition()
    {
        ScriptRunner.run("scripts/loot-table-entry-addition.zs");
        loadTestTables();
    }

    private void loadTestTables()
    {
        for (ResourceLocation testTable : TEST_LOOT_TABLES)
            LootTweakerApiRefresh.getTestApi().processLootTable(testTable, TestUtils.loadTable(testTable));
    }
}
