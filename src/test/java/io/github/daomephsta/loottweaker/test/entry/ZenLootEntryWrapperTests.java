package io.github.daomephsta.loottweaker.test.entry;

import static io.github.daomephsta.loottweaker.test.TestLootConditionAccessors.isInverted;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;

import crafttweaker.api.data.IData;
import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.loottweaker.test.util.DataMapBuilder;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.mutable_loot.entry.MutableLootEntry;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootEntryWrapper;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class ZenLootEntryWrapperTests
{
    private static final LootCondition[] NO_CONDITIONS = new LootCondition[0];
    private static final LootFunction[] NO_FUNCTIONS = new LootFunction[0];
    private final LootTweakerContext context = TestUtils.context();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setWeight()
    {
        LootEntry quxOriginal = new LootEntryItem(Items.COAL, 1, 0, NO_FUNCTIONS, NO_CONDITIONS, "qux");
        ZenLootEntryWrapper quxTweaks = context.wrapEntry(new ResourceLocation("loottweaker:bar"), "baz", "qux");
        quxTweaks.setWeight(10);
        LootEntry quxNew = applyTweaks(quxTweaks, quxOriginal);
        assertThat(quxOriginal).hasWeight(1);
        assertThat(quxNew).hasWeight(10);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setQuality()
    {
        LootEntry quxOriginal = new LootEntryItem(Items.COAL, 1, 0, NO_FUNCTIONS, NO_CONDITIONS, "qux");
        ZenLootEntryWrapper quxTweaks = context.wrapEntry(new ResourceLocation("loottweaker:bar"), "baz", "qux");
        quxTweaks.setQuality(10);
        LootEntry quxNew = applyTweaks(quxTweaks, quxOriginal);
        assertThat(quxOriginal).hasQuality(0);
        assertThat(quxNew).hasQuality(10);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setConditionsHelper()
    {
        LootEntry quxOriginal = new LootEntryItem(Items.COAL, 1, 0, NO_FUNCTIONS, NO_CONDITIONS, "qux");
        ZenLootEntryWrapper quxTweaks = context.wrapEntry(new ResourceLocation("loottweaker:bar"), "baz", "qux");
        quxTweaks.setConditionsHelper(new ZenLootConditionWrapper[] {LootConditionFactory.killedByPlayer()});
        LootEntry quxNew = applyTweaks(quxTweaks, quxOriginal);
        assertThat(quxOriginal).hasNoLootConditions();
        assertThat(quxNew).hasMatchingCondition(
            condition -> condition instanceof KilledByPlayer && !isInverted((KilledByPlayer) condition),
            "KilledByPlayer()");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setConditionsJson()
    {
        LootEntry quxOriginal = new LootEntryItem(Items.COAL, 1, 0, NO_FUNCTIONS, NO_CONDITIONS, "qux");
        ZenLootEntryWrapper quxTweaks = context.wrapEntry(new ResourceLocation("loottweaker:bar"), "baz", "qux");
        quxTweaks.setConditionsJson(new IData[] {
            new DataMapBuilder().putString("condition", "minecraft:killed_by_player").build()});
        LootEntry quxNew = applyTweaks(quxTweaks, quxOriginal);
        assertThat(quxOriginal).hasNoLootConditions();
        assertThat(quxNew).hasMatchingCondition(
            condition -> condition instanceof KilledByPlayer && !isInverted((KilledByPlayer) condition),
            "KilledByPlayer()");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addConditionsHelper()
    {
        LootEntry quxOriginal = new LootEntryItem(Items.COAL, 1, 0, NO_FUNCTIONS, NO_CONDITIONS, "qux");
        ZenLootEntryWrapper quxTweaks = context.wrapEntry(new ResourceLocation("loottweaker:bar"), "baz", "qux");
        quxTweaks.addConditionsHelper(new ZenLootConditionWrapper[] {LootConditionFactory.killedByPlayer()});
        LootEntry quxNew = applyTweaks(quxTweaks, quxOriginal);
        assertThat(quxOriginal).hasNoLootConditions();
        assertThat(quxNew).hasMatchingCondition(
            condition -> condition instanceof KilledByPlayer && !isInverted((KilledByPlayer) condition),
            "KilledByPlayer()");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addConditionsJson()
    {
        LootEntry quxOriginal = new LootEntryItem(Items.COAL, 1, 0, NO_FUNCTIONS, NO_CONDITIONS, "qux");
        ZenLootEntryWrapper quxTweaks = context.wrapEntry(new ResourceLocation("loottweaker:bar"), "baz", "qux");
        quxTweaks.addConditionsJson(new IData[] {
            new DataMapBuilder().putString("condition", "minecraft:killed_by_player").build()});
        LootEntry quxNew = applyTweaks(quxTweaks, quxOriginal);
        assertThat(quxOriginal).hasNoLootConditions();
        assertThat(quxNew).hasMatchingCondition(
            condition -> condition instanceof KilledByPlayer && !isInverted((KilledByPlayer) condition),
            "KilledByPlayer()");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void clearConditions()
    {
        LootEntry quxOriginal = new LootEntryItem(Items.COAL, 1, 0, NO_FUNCTIONS,
            new LootCondition[] {new KilledByPlayer(false)}, "qux");
        ZenLootEntryWrapper quxTweaks = context.wrapEntry(new ResourceLocation("loottweaker:bar"), "baz", "qux");
        quxTweaks.clearConditions();
        LootEntry quxNew = applyTweaks(quxTweaks, quxOriginal);
        assertThat(quxOriginal).hasMatchingCondition(
            condition -> condition instanceof KilledByPlayer && !isInverted((KilledByPlayer) condition),
            "KilledByPlayer()");
        assertThat(quxNew).hasNoLootConditions();
    }

    private LootEntry applyTweaks(ZenLootEntryWrapper tweaks, LootEntry entry)
    {
        MutableLootEntry mutableEntry = MutableLootEntry.from(entry);
        tweaks.tweak(mutableEntry);
        return mutableEntry.toImmutable();
    }
}
