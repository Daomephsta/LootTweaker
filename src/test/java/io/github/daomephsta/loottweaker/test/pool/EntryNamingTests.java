package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestUtils.iitemstack;
import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;

import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.LootTableTweakManager;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootPoolWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;

public class EntryNamingTests
{
    private final LootTweakerContext context = TestUtils.context();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void identicalItems()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addItemEntry(iitemstack(Items.DYE, 2), 5, null);
        barTweaks.addItemEntry(iitemstack(Items.DYE, 1), 2, null);

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar"))
            .hasEntry("loottweaker#1")
            .hasEntry("loottweaker#2");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void customNamedItemEntry()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addItemEntry(iitemstack(Items.DYE, 2), 5, "garple");
        barTweaks.addItemEntry(iitemstack(Items.DYE, 1), 2, null);

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar"))
            .hasEntry("loottweaker#1")
            .hasEntry("garple");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void identicalTableReferences()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addLootTableEntry("loottweaker:bar", 5, null);
        barTweaks.addLootTableEntry("loottweaker:bar", 2, null);

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar"))
            .hasEntry("loottweaker#1")
            .hasEntry("loottweaker#2");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void customNamedTableReference()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addLootTableEntry("loottweaker:bar", 5, "garple");
        barTweaks.addLootTableEntry("loottweaker:bar", 2, null);

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar"))
            .hasEntry("loottweaker#1")
            .hasEntry("garple");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void multipleEmpties()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addEmptyEntry(5, null);
        barTweaks.addEmptyEntry(2, null);

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar"))
            .hasEntry("loottweaker#1")
            .hasEntry("loottweaker#2");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void customNamedEmpty()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addEmptyEntry(5, "garple");
        barTweaks.addEmptyEntry(2, null);

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar"))
            .hasEntry("loottweaker#1")
            .hasEntry("garple");
    }
}
