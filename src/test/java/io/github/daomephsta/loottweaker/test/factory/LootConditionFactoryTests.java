package io.github.daomephsta.loottweaker.test.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import java.util.Map;

import org.assertj.core.api.Condition;

import com.google.common.collect.ImmutableMap;

import io.github.daomephsta.loottweaker.test.TestErrorHandler.LootTweakerException;
import io.github.daomephsta.loottweaker.test.mixin.condition.TestKilledByPlayerAccessors;
import io.github.daomephsta.loottweaker.test.mixin.condition.TestRandomChanceAccessors;
import io.github.daomephsta.loottweaker.test.mixin.condition.TestRandomChanceWithLootingAccessors;
import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.factory.LootConditionFactoryImpl;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;


public class LootConditionFactoryTests
{
    private final Condition<ZenLootConditionWrapper> VALID_CONDITION = new Condition<>(
        ZenLootConditionWrapper::isValid, "valid condition");
    private final LootConditionFactoryImpl factory = TestUtils.context().createLootConditionFactory();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void randomChance()
    {
        assertThat(factory.randomChance(0.21F)).is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(RandomChance.class))
            .satisfies(new Condition<>(rc -> ((TestRandomChanceAccessors) rc).getChance() == 0.21F, "RandomChance(0.21)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void randomChanceWithLooting()
    {
        assertThat(factory.randomChanceWithLooting(0.35F, 1.2F)).is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(RandomChanceWithLooting.class))
            .satisfies(new Condition<>(rcwl -> ((TestRandomChanceWithLootingAccessors) rcwl).getChance() == 0.35F &&
                ((TestRandomChanceWithLootingAccessors) rcwl).getLootingMultiplier() == 1.2F,
                "RandomChanceWithLooting(0.35, 1.2)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void killedByPlayer()
    {
        assertThat(factory.killedByPlayer()).is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(KilledByPlayer.class))
            .satisfies(new Condition<>(condition -> condition instanceof KilledByPlayer && !((TestKilledByPlayerAccessors) condition).isInverse(), "KilledByPlayer()"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void killedByNonPlayer()
    {
        assertThat(factory.killedByNonPlayer()).is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(KilledByPlayer.class))
            .satisfies(new Condition<>(condition -> condition instanceof KilledByPlayer && ((TestKilledByPlayerAccessors) condition).isInverse(), "KilledByNonPlayer()"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parse()
    {
        Map<String, Object> json = ImmutableMap.of("condition", "minecraft:killed_by_player");
        assertThat(factory.parse(json)).is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(KilledByPlayer.class))
            .satisfies(new Condition<>(condition -> condition instanceof KilledByPlayer && !((TestKilledByPlayerAccessors) condition).isInverse(), "KilledByPlayer()"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseMalformed()
    {
        Map<String, Object> json = ImmutableMap.of("condition", "garBaGe");
        assertThatThrownBy(() -> factory.parse(json)).isInstanceOf(LootTweakerException.class)
            .hasMessage("Unknown condition 'minecraft:garbage'");
    }
}
