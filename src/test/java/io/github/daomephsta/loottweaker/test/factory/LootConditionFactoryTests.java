package io.github.daomephsta.loottweaker.test.factory;

import static com.google.common.base.Predicates.not;
import static io.github.daomephsta.loottweaker.test.TestLootConditionAccessors.getChance;
import static io.github.daomephsta.loottweaker.test.TestLootConditionAccessors.getLootingMultiplier;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Condition;

import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.DataString;
import crafttweaker.api.data.IData;
import io.github.daomephsta.loottweaker.test.TestLootConditionAccessors;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;

public class LootConditionFactoryTests
{
    private static final Condition<ZenLootConditionWrapper> VALID_CONDITION = new Condition<>(ZenLootConditionWrapper::isValid, "valid condition");
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void randomChance()
    {
        assertThat(LootConditionFactory.randomChance(0.21F))
            .is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(RandomChance.class))
            .satisfies(new Condition<>(rc -> getChance(rc) == 0.21F, "RandomChance(0.21)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void randomChanceWithLooting()
    {
        assertThat(LootConditionFactory.randomChanceWithLooting(0.35F, 1.2F))
            .is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(RandomChanceWithLooting.class))
            .satisfies(new Condition<>(rcwl -> getChance(rcwl) == 0.35F && getLootingMultiplier(rcwl) == 1.2F, 
                "RandomChanceWithLooting(0.35, 1.2)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void killedByPlayer()
    {
        assertThat(LootConditionFactory.killedByPlayer())
            .is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(KilledByPlayer.class))
            .satisfies(new Condition<>(not(TestLootConditionAccessors::isInverted), "KilledByPlayer()"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void killedByNonPlayer()
    {
        assertThat(LootConditionFactory.killedByNonPlayer())
            .is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(KilledByPlayer.class))
            .satisfies(new Condition<>(TestLootConditionAccessors::isInverted, "KilledByNonPlayer()"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parse()
    {
        Map<String, IData> data = new HashMap<>();
        data.put("condition", new DataString("minecraft:killed_by_player"));
        IData json = new DataMap(data , false);
        assertThat(LootConditionFactory.parse(json))
            .is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(KilledByPlayer.class))
            .satisfies(new Condition<>(not(TestLootConditionAccessors::isInverted), "KilledByPlayer()"));
    }
}
