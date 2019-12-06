package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestUtils.iitemstack;
import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;

import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootPoolWrapper;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;

public class EntryNamingTests
{
    private final LootTweakerContext context = TestUtils.context();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void identicalItems()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        ZenLootPoolWrapper barTweaks = context.wrapPool("bar", fooId);
        barTweaks.addItemEntry(iitemstack(Items.DYE, 2), 5, null);
        barTweaks.addItemEntry(iitemstack(Items.DYE, 1), 2, null);
        barTweaks.tweak(foo);

        assertThat(foo.getPool("bar"))
            .hasEntry("loottweaker#1")
            .hasEntry("loottweaker#2");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void customNamedItemEntry()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        ZenLootPoolWrapper barTweaks = context.wrapPool("bar", fooId);
        barTweaks.addItemEntry(iitemstack(Items.DYE, 2), 5, "garple");
        barTweaks.addItemEntry(iitemstack(Items.DYE, 1), 2, null);
        barTweaks.tweak(foo);

        assertThat(foo.getPool("bar"))
            .hasEntry("loottweaker#1")
            .hasEntry("garple");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void identicalTableReferences()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        ZenLootPoolWrapper barTweaks = context.wrapPool("bar", fooId);
        barTweaks.addLootTableEntry("loottweaker:bar", 5, null);
        barTweaks.addLootTableEntry("loottweaker:bar", 2, null);
        barTweaks.tweak(foo);

        assertThat(foo.getPool("bar"))
            .hasEntry("loottweaker#1")
            .hasEntry("loottweaker#2");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void customNamedTableReference()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        ZenLootPoolWrapper barTweaks = context.wrapPool("bar", fooId);
        barTweaks.addLootTableEntry("loottweaker:bar", 5, "garple");
        barTweaks.addLootTableEntry("loottweaker:bar", 2, null);
        barTweaks.tweak(foo);

        assertThat(foo.getPool("bar"))
            .hasEntry("loottweaker#1")
            .hasEntry("garple");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void multipleEmpties()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        ZenLootPoolWrapper barTweaks = context.wrapPool("bar", fooId);
        barTweaks.addEmptyEntry(5, null);
        barTweaks.addEmptyEntry(2, null);
        barTweaks.tweak(foo);

        assertThat(foo.getPool("bar"))
            .hasEntry("loottweaker#1")
            .hasEntry("loottweaker#2");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void customNamedEmpty()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        ZenLootPoolWrapper barTweaks = context.wrapPool("bar", fooId);
        barTweaks.addEmptyEntry(5, "garple");
        barTweaks.addEmptyEntry(2, null);
        barTweaks.tweak(foo);

        assertThat(foo.getPool("bar"))
            .hasEntry("loottweaker#1")
            .hasEntry("garple");
    }
}
