package io.github.daomephsta.loottweaker.test;

import static com.google.common.base.Predicates.not;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import java.util.Map;

import org.assertj.core.api.Condition;

import com.google.common.collect.ImmutableMap;

import io.github.daomephsta.loottweaker.test.TestErrorHandler.LootTweakerException;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.AnyDictConversions;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootFunctionWrapper;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.functions.Smelt;

public class AnyDictConversionsTests
{
    private final Condition<ZenLootConditionWrapper> VALID_CONDITION = 
        new Condition<>(ZenLootConditionWrapper::isValid, "valid condition");
    private final Condition<ZenLootFunctionWrapper> VALID_FUNCTION = 
        new Condition<>(ZenLootFunctionWrapper::isValid, "valid function");
    private final AnyDictConversions.Impl anyDictConversions = 
        new AnyDictConversions.Impl(TestUtils.context());
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseCondition()
    {
        Map<String, Object> json = ImmutableMap.of("condition", "minecraft:killed_by_player");
        assertThat(anyDictConversions.asLootCondition(json))
            .is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(KilledByPlayer.class))
            .satisfies(new Condition<>(not(TestLootConditionAccessors::isInverted), "KilledByPlayer()"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseMalformedCondition()
    {
        Map<String, Object> json = ImmutableMap.of("condition", "garBaGe");
        assertThatThrownBy(() -> anyDictConversions.asLootCondition(json))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("Unknown condition 'minecraft:garbage'");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseFunction()
    {
        Map<String, Object> json = ImmutableMap.of("function", "minecraft:furnace_smelt");
        assertThat(anyDictConversions.asLootFunction(json))
            .is(VALID_FUNCTION)
            .extracting(ZenLootFunctionWrapper::unwrap)
            .isInstanceOf(Smelt.class);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseMalformedFunction()
    {
        Map<String, Object> json = ImmutableMap.of("function", "garBaGe");
        assertThatThrownBy(() -> anyDictConversions.asLootFunction(json))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("Unknown function 'minecraft:garbage'");
    }
}
