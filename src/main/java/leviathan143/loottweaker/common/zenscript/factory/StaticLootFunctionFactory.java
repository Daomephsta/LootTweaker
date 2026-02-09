package leviathan143.loottweaker.common.zenscript.factory;

import java.util.Map;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import daomephsta.loot_shared.zenscript.api.ZenLootFunction;
import daomephsta.loot_shared.zenscript.api.factory.LootFunctionFactory;
import daomephsta.loot_shared.zenscript.api.factory.ZenLambdaLootFunction;
import daomephsta.loot_shared.zenscript.impl.JsonMapConversions;
import leviathan143.loottweaker.common.DeprecationWarningManager;
import leviathan143.loottweaker.common.LootTweaker;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


@ZenRegister
@ZenClass(LootTweaker.MODID + ".Functions")
public class StaticLootFunctionFactory
{
	private static final LootFunctionFactory IMPLEMENTATION = LootTweaker.CONTEXT.createLootFunctionFactory();
	private static final JsonMapConversions.Impl JSON_MAP_CONVERSTIONS = new JsonMapConversions.Impl(LootTweaker.CONTEXT.getErrorHandler());

    @ZenMethod
    public static ZenLootFunction enchantRandomly(String[] enchantIDList)
    {
        return IMPLEMENTATION.enchantRandomly(enchantIDList);
    }

    @ZenMethod
    public static ZenLootFunction enchantWithLevels(int min, int max, boolean isTreasure)
    {
        return IMPLEMENTATION.enchantWithLevels(min, max, isTreasure);
    }

    @ZenMethod
    public static ZenLootFunction lootingEnchantBonus(int min, int max, int limit)
    {
        return IMPLEMENTATION.lootingEnchantBonus(min, max, limit);
    }

    @ZenMethod
    public static ZenLootFunction setCount(int min, int max)
    {
        return IMPLEMENTATION.setCount(min, max);
    }

    @ZenMethod
    public static ZenLootFunction setDamage(float min, float max)
    {
        return IMPLEMENTATION.setDamage(min, max);
    }

    @ZenMethod
    public static ZenLootFunction setMetadata(int min, int max)
    {
        return IMPLEMENTATION.setMetadata(min, max);
    }

    @ZenMethod
    public static ZenLootFunction setNBT(IData nbtData)
    {
        return IMPLEMENTATION.setNBT(nbtData);
    }

    @ZenMethod
    public static ZenLootFunction smelt()
    {
        return IMPLEMENTATION.smelt();
    }

    @ZenMethod
    public static ZenLootFunction parse(Map<String, IData> json)
    {
        DeprecationWarningManager.addWarning();
        return JSON_MAP_CONVERSTIONS.asLootFunction(json);
    }

    @ZenMethod
    public static ZenLootFunction zenscript(ZenLambdaLootFunction.Delegate delegate)
    {
        return IMPLEMENTATION.zenscript(delegate);
    }
}
