package io.github.daomephsta.loottweaker.test.factory;
import static io.github.daomephsta.loottweaker.test.TestLootFunctionAccessors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Condition;

import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.DataString;
import crafttweaker.api.data.IData;
import io.github.daomephsta.loottweaker.test.TestLootFunctionAccessors;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.factory.LootFunctionFactory;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootFunctionWrapper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;
import net.minecraft.world.storage.loot.functions.EnchantWithLevels;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;
import net.minecraft.world.storage.loot.functions.Smelt;

public class LootFunctionFactoryTests
{
    private static final Condition<ZenLootFunctionWrapper> VALID_FUNCTION = new Condition<>(ZenLootFunctionWrapper::isValid, "valid function");
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void enchantRandomly()
    {
        assertThat(LootFunctionFactory.enchantRandomly(new String[] {"minecraft:thorns"}))
            .is(VALID_FUNCTION)
            .extracting(ZenLootFunctionWrapper::unwrap)
            .asInstanceOf(type(EnchantRandomly.class))
            .satisfies(new Condition<>(enchantRandomly -> 
            {
                List<Enchantment> enchantments = getEnchantments(enchantRandomly);
                return enchantments.size() == 1 
                    && enchantments.get(0).getRegistryName().equals(new ResourceLocation("minecraft:thorns"));
            }, "LootingEnchantBonus([1, 3])"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void enchantWithLevels()
    {
        assertThat(LootFunctionFactory.enchantWithLevels(11, 26, false))
            .is(VALID_FUNCTION)
            .extracting(ZenLootFunctionWrapper::unwrap)
            .asInstanceOf(type(EnchantWithLevels.class))
            .satisfies(new Condition<>(enchantWithLevels -> 
            {
                RandomValueRange levelRange = getLevelRange(enchantWithLevels);
                return levelRange.getMin() == 11.0F && levelRange.getMax() == 26.0F 
                    && !isTreasure(enchantWithLevels);
            }, "EnchantWithLevels([11, 26], isTreasure: false)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void lootingEnchantBonus()
    {
        assertThat(LootFunctionFactory.lootingEnchantBonus(1, 2, 3))
            .is(VALID_FUNCTION)
            .extracting(ZenLootFunctionWrapper::unwrap)
            .asInstanceOf(type(LootingEnchantBonus.class))
            .satisfies(new Condition<>(lootingEnchantBonus -> 
            {
                RandomValueRange bonusRange = getBonusRange(lootingEnchantBonus);
                return bonusRange.getMin() == 1.0F && bonusRange.getMax() == 2.0F 
                    && getLimit(lootingEnchantBonus) == 3;
            }, "LootingEnchantBonus([1, 2], limit: 3)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setCount()
    {
        assertThat(LootFunctionFactory.setCount(1, 3))
            .is(VALID_FUNCTION)
            .extracting(ZenLootFunctionWrapper::unwrap)
            .asInstanceOf(type(SetCount.class))
            .satisfies(new Condition<>(setCount -> 
            {
                RandomValueRange countRange = getCountRange(setCount);
                return countRange.getMin() == 1.0F && countRange.getMax() == 3.0F;
            }, "SetCount([1, 3])"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setDamage()
    {
        assertThat(LootFunctionFactory.setDamage(0.2F, 0.8F))
            .is(VALID_FUNCTION)
            .extracting(ZenLootFunctionWrapper::unwrap)
            .asInstanceOf(type(SetDamage.class))
            .satisfies(new Condition<>(setDamage -> 
            {
                RandomValueRange damageRange = getDamageRange(setDamage);
                return damageRange.getMin() == 0.2F && damageRange.getMax() == 0.8F;
            }, "SetDamage([0.2, 0.8])"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setMetadata()
    {
        assertThat(LootFunctionFactory.setMetadata(23, 45))
            .is(VALID_FUNCTION)
            .extracting(ZenLootFunctionWrapper::unwrap)
            .asInstanceOf(type(SetMetadata.class))
            .satisfies(new Condition<>(setMetadata -> 
            {
                RandomValueRange metaRange = getMetaRange(setMetadata);
                return metaRange.getMin() == 23.0F && metaRange.getMax() == 45.0F;
            }, "SetMetadata([23, 45])"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setNBT()
    {
        Map<String, IData> data = new HashMap<>();
        data.put("foo", new DataString("bar"));
        IData nbtData = new DataMap(data , false);
        
        NBTTagCompound expectedTag = new NBTTagCompound();
        expectedTag.setString("foo", "bar");
        
        assertThat(LootFunctionFactory.setNBT(nbtData ))
            .is(VALID_FUNCTION)
            .extracting(ZenLootFunctionWrapper::unwrap)
            .asInstanceOf(type(SetNBT.class))
            .satisfies(new Condition<>(setNbt -> TestLootFunctionAccessors.getTag(setNbt).equals(expectedTag), 
                "SetNBT(%s)", expectedTag));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void smelt()
    {
        assertThat(LootFunctionFactory.smelt())
            .is(VALID_FUNCTION)
            .extracting(ZenLootFunctionWrapper::unwrap)
            .isInstanceOf(Smelt.class);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parse()
    {
        Map<String, IData> data = new HashMap<>();
        data.put("function", new DataString("minecraft:furnace_smelt"));
        IData json = new DataMap(data , false);
        assertThat(LootFunctionFactory.parse(json))
            .is(VALID_FUNCTION)
            .extracting(ZenLootFunctionWrapper::unwrap)
            .isInstanceOf(Smelt.class);
    }
}
