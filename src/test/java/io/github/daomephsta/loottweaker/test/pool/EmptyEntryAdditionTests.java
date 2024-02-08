package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestLootConditionAccessors.isInverted;
import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;

import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.LootTableTweakManager;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootPoolWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;


public class EmptyEntryAdditionTests
{
    private final LootTweakerContext context = TestUtils.context();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntry()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addEmptyEntry(2, "corge");

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar")).extractEntry("corge").hasWeight(2).hasNoLootConditions().isEmptyEntry();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntryWithQuality()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addEmptyEntry(2, 3, "corge");

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar")).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasNoLootConditions()
            .isEmptyEntry();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntryWithCondition()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addEmptyEntry(2, 3, new ZenLootConditionWrapper[] { LootConditionFactory.killedByPlayer() },
            "corge");

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar")).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasMatchingCondition(
                condition -> condition instanceof KilledByPlayer && !isInverted((KilledByPlayer) condition),
                "KilledByPlayer()")
            .isEmptyEntry();
    }
}