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
import leviathan143.loottweaker.common.zenscript.JsonMapConversions;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootFunctionWrapper;
import net.minecraft.world.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.Smelt;
import net.minecraft.world.storage.loot.properties.EntityOnFire;
import net.minecraft.world.storage.loot.properties.EntityProperty;

public class JsonMapConversionTests
{
    private final Condition<ZenLootConditionWrapper> VALID_CONDITION =
        new Condition<>(ZenLootConditionWrapper::isValid, "valid condition");
    private final Condition<ZenLootFunctionWrapper> VALID_FUNCTION =
        new Condition<>(ZenLootFunctionWrapper::isValid, "valid function");
    private final JsonMapConversions.Impl jsonMapConversions = new JsonMapConversions.Impl(TestUtils.context());

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseSimpleCondition()
    {
        Map<String, Object> json = ImmutableMap.of("condition", "minecraft:killed_by_player");
        assertThat(jsonMapConversions.asLootCondition(json))
            .is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(KilledByPlayer.class))
            .satisfies(new Condition<>(not(TestLootConditionAccessors::isInverted), "KilledByPlayer()"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseNestingCondition()
    {
        Map<String, Object> json = ImmutableMap.of(
            "condition", "minecraft:entity_properties",
            "entity", "this",
            "properties", ImmutableMap.of("on_fire", true));
        assertThat(jsonMapConversions.asLootCondition(json))
            .is(VALID_CONDITION)
            .extracting(ZenLootConditionWrapper::unwrap)
            .asInstanceOf(type(EntityHasProperty.class))
            .satisfies(new Condition<>(entityHasProperty ->
            {
                if (TestLootConditionAccessors.getTarget(entityHasProperty) != EntityTarget.THIS)
                    return false;
                EntityProperty[] properties = TestLootConditionAccessors.getProperties(entityHasProperty);
                if (properties.length != 1)
                    return false;
                if (properties[0] instanceof EntityOnFire)
                    return TestLootConditionAccessors.isOnFire((EntityOnFire) properties[0]);
                return false;
            }, "EntityHasProperty(on_fire = true)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseMalformedCondition()
    {
        Map<String, Object> json = ImmutableMap.of("condition", "garBaGe");
        assertThatThrownBy(() -> jsonMapConversions.asLootCondition(json))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("Unknown condition 'minecraft:garbage'");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseSimpleFunction()
    {
        Map<String, Object> json = ImmutableMap.of("function", "minecraft:furnace_smelt");
        assertThat(jsonMapConversions.asLootFunction(json))
            .is(VALID_FUNCTION)
            .extracting(ZenLootFunctionWrapper::unwrap)
            .isInstanceOf(Smelt.class);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseNestingFunction()
    {
        Map<String, Object> json = ImmutableMap.of(
            "function", "minecraft:set_count",
            "count", ImmutableMap.of("min", 0, "max", 2));
        assertThat(jsonMapConversions.asLootFunction(json))
            .is(VALID_FUNCTION)
            .extracting(ZenLootFunctionWrapper::unwrap)
            .asInstanceOf(type(SetCount.class))
            .satisfies(new Condition<>(setCount ->
            {
                RandomValueRange countRange = TestLootFunctionAccessors.getCountRange(setCount);
                return countRange.getMin() == 0F && countRange.getMax() == 2F;
            }, "SetCount(min = 0, max = 2)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseMalformedFunction()
    {
        Map<String, Object> json = ImmutableMap.of("function", "garBaGe");
        assertThatThrownBy(() -> jsonMapConversions.asLootFunction(json))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("Unknown function 'minecraft:garbage'");
    }
}
