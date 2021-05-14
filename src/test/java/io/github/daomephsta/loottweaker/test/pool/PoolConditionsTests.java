package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestLootConditionAccessors.isInverted;
import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;

import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.api.entry.LootConditionRepresentation;
import leviathan143.loottweaker.common.zenscript.api.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.impl.MutableLootPool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;

public class PoolConditionsTests
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addConditions()
    {
        LootTweakerContext context = TestUtils.createContext();
        LootConditionFactory conditions = context.lootSystem().getLootConditionFactory();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool barOriginal = foo.getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addConditions(new LootConditionRepresentation[] {conditions.killedByPlayer()});

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).hasMatchingCondition(condition ->
            condition instanceof KilledByPlayer && !isInverted((KilledByPlayer) condition),
        "KilledByPlayer()");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeAllConditions()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation barId = new ResourceLocation("loottweaker", "bar");
        LootTable bar = loadTable(barId);
        LootPool bazOriginal = bar.getPool("baz");
        assertThat(bazOriginal).hasLootConditions();
        MutableLootPool mutableBaz = new MutableLootPool(bazOriginal, barId, context);
        mutableBaz.removeAllConditions();

        LootPool bazNew = mutableBaz.toImmutable();
        assertThat(bazNew).hasNoLootConditions();
    }
}
