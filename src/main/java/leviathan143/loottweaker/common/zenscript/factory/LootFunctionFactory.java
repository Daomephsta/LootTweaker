package leviathan143.loottweaker.common.zenscript.factory;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootFunctionWrapper;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".vanilla.loot.Functions")
public class LootFunctionFactory
{
    private static final LootFunctionFactoryImpl IMPLEMENTATION = LootTweaker.CONTEXT.createLootFunctionFactory();

    public static ZenLootFunctionWrapper enchantRandomly(String[] enchantIDList)
    {
        return IMPLEMENTATION.enchantRandomly(enchantIDList);
    }

    @ZenMethod
    public static ZenLootFunctionWrapper enchantWithLevels(int min, int max, boolean isTreasure)
    {
        return IMPLEMENTATION.enchantWithLevels(min, max, isTreasure);
    }

    @ZenMethod
    public static ZenLootFunctionWrapper lootingEnchantBonus(int min, int max, int limit)
    {
        return IMPLEMENTATION.lootingEnchantBonus(min, max, limit);
    }

    @ZenMethod
    public static ZenLootFunctionWrapper setCount(int min, int max)
    {
        return IMPLEMENTATION.setCount(min, max);
    }

    @ZenMethod
    public static ZenLootFunctionWrapper setDamage(float min, float max)
    {
        return IMPLEMENTATION.setDamage(min, max);
    }

    @ZenMethod
    public static ZenLootFunctionWrapper setMetadata(int min, int max)
    {
        return IMPLEMENTATION.setMetadata(min, max);
    }

    @ZenMethod
    public static ZenLootFunctionWrapper setNBT(IData nbtData)
    {
        return IMPLEMENTATION.setNBT(nbtData);
    }

    @ZenMethod
    public static ZenLootFunctionWrapper smelt()
    {
        return IMPLEMENTATION.smelt();
    }

    @ZenMethod
    public static ZenLootFunctionWrapper parse(IData json)
    {
        return IMPLEMENTATION.parse(json);
    }
}
