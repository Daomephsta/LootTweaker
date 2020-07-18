package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestLootConditionAccessors.isInverted;
import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;

import crafttweaker.api.data.IData;
import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.loottweaker.test.util.DataMapBuilder;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.impl.MutableLootPool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;


public class LootTableEntryAdditionTests
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addLootTableEntry()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addLootTableEntry("loottweaker:qux", "corge");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("corge")
            .hasWeight(1)
            .hasQuality(0)
            .hasNoLootConditions()
            .asLootTableEntry()
            .spawnsFromTable("loottweaker:qux");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addLootTableEntryWithWeight()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addLootTableEntry("loottweaker:qux", 2, "corge");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(0)
            .hasNoLootConditions()
            .asLootTableEntry()
            .spawnsFromTable("loottweaker:qux");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addLootTableEntryWithQuality()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addLootTableEntry("loottweaker:qux", 2, 3, "corge");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasNoLootConditions()
            .asLootTableEntry()
            .spawnsFromTable("loottweaker:qux");
    }

    /*@SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addLootTableEntryWithCondition()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addLootTableEntryHelper("loottweaker:qux", 2, 3, new ZenLootConditionWrapper[] { LootConditionFactory.killedByPlayer() }, "corge");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasMatchingCondition(condition -> condition instanceof KilledByPlayer
                && !isInverted((KilledByPlayer) condition), "KilledByPlayer()")
            .asLootTableEntry()
            .spawnsFromTable("loottweaker:qux");
    }*/

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addLootTableEntryJson()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addLootTableEntryJson("loottweaker:qux", 2, 3, new IData[] { new DataMapBuilder().putString("condition", "minecraft:killed_by_player").build() }, "corge");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("corge")
            .hasWeight(2).hasQuality(3)
            .hasMatchingCondition(condition -> condition instanceof KilledByPlayer
                && !isInverted((KilledByPlayer) condition), "KilledByPlayer()")
            .asLootTableEntry()
            .spawnsFromTable("loottweaker:qux");
    }
}
