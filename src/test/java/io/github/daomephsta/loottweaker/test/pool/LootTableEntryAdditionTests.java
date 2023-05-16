package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;
import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.loottweaker.test.mixin.condition.TestKilledByPlayerAccessors;
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


public class LootTableEntryAdditionTests
{
    private final LootTweakerContext context = TestUtils.context();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addLootTableEntry()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addLootTableEntry("loottweaker_test:qux", 2, "corge");

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar")).extractEntry("corge")
            .hasWeight(2)
            .hasNoLootConditions()
            .asLootTableEntry()
            .spawnsFromTable("loottweaker_test:qux");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addLootTableEntryWithQuality()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addLootTableEntry("loottweaker_test:qux", 2, 3, "corge");

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar")).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasNoLootConditions()
            .asLootTableEntry()
            .spawnsFromTable("loottweaker_test:qux");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addLootTableEntryWithCondition()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addLootTableEntry("loottweaker_test:qux", 2, 3,
            new ZenLootConditionWrapper[] { LootConditionFactory.killedByPlayer() }, "corge");

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar")).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasMatchingCondition(
                condition -> condition instanceof KilledByPlayer && !((TestKilledByPlayerAccessors) condition).isInverse(),
                "KilledByPlayer()")
            .asLootTableEntry()
            .spawnsFromTable("loottweaker_test:qux");
    }
}