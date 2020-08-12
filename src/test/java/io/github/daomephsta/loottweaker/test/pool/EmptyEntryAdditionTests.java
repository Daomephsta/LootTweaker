package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestLootConditionAccessors.isInverted;
import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;

import crafttweaker.api.data.IData;
import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.loottweaker.test.util.DataMapBuilder;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.api.entry.LootConditionRepresentation;
import leviathan143.loottweaker.common.zenscript.api.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.impl.MutableLootPool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;


public class EmptyEntryAdditionTests
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntry()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addEmptyEntry("corge");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("corge")
            .hasWeight(1)
            .hasQuality(0)
            .hasNoLootConditions()
            .isEmptyEntry();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntryWithWeight()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addEmptyEntry(2, "corge");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(0)
            .hasNoLootConditions()
            .isEmptyEntry();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntryWithQuality()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addEmptyEntry(2, 3, "corge");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasNoLootConditions()
            .isEmptyEntry();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntryHelper()
    {
        LootTweakerContext context = TestUtils.createContext();
        LootConditionFactory conditionFactory = context.lootSystem().getLootConditionFactory();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addEmptyEntryHelper(2, 3, new LootConditionRepresentation[] { conditionFactory.killedByPlayer() }, "corge");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasMatchingCondition(condition -> condition instanceof KilledByPlayer
                && !isInverted((KilledByPlayer) condition), "KilledByPlayer()")
            .isEmptyEntry();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntryJson()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addEmptyEntryJson(2, 3, new IData[] { new DataMapBuilder().putString("condition", "minecraft:killed_by_player").build() }, "corge");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasMatchingCondition(condition -> condition instanceof KilledByPlayer
                && !isInverted((KilledByPlayer) condition), "KilledByPlayer()")
            .isEmptyEntry();
    }
}
