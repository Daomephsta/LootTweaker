package leviathan143.loottweaker.common.zenscript.factory;

import java.util.Map;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


@ZenRegister
@ZenClass(LootTweaker.MODID + ".vanilla.loot.Conditions")
public class LootConditionFactory
{
    private static final LootConditionFactoryImpl IMPLEMENTATION = LootTweaker.CONTEXT
        .createLootConditionFactory();

    @ZenMethod
    public static ZenLootConditionWrapper randomChance(float chance)
    {
        return IMPLEMENTATION.randomChance(chance);
    }

    @ZenMethod
    public static ZenLootConditionWrapper randomChanceWithLooting(float chance, float lootingMultiplier)
    {
        return IMPLEMENTATION.randomChanceWithLooting(chance, lootingMultiplier);
    }

    @ZenMethod
    public static ZenLootConditionWrapper killedByPlayer()
    {
        return IMPLEMENTATION.killedByPlayer();
    }

    @ZenMethod
    public static ZenLootConditionWrapper killedByNonPlayer()
    {
        return IMPLEMENTATION.killedByNonPlayer();
    }

    @ZenMethod
    public static ZenLootConditionWrapper parse(Map<String, IData> json)
    {
        return IMPLEMENTATION.parse(json);
    }

    @ZenMethod
    public static ZenLootConditionWrapper zenscript(ZenLambdaLootCondition.Delegate delegate)
    {
        return IMPLEMENTATION.zenscript(delegate);
    }
}
