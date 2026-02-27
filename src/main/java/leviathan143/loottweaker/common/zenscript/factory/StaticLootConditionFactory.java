package leviathan143.loottweaker.common.zenscript.factory;

import crafttweaker.annotations.ZenRegister;
import daomephsta.loot_shared.zenscript.api.ZenLootCondition;
import daomephsta.loot_shared.zenscript.api.factory.LootConditionFactory;
import daomephsta.loot_shared.zenscript.api.factory.ZenLambdaLootCondition;
import leviathan143.loottweaker.common.LootTweaker;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


@ZenRegister
@ZenClass(LootTweaker.MODID + ".Conditions")
public class StaticLootConditionFactory
{
    private static final LootConditionFactory IMPLEMENTATION = LootTweaker.CONTEXT.createLootConditionFactory();

    @ZenMethod
    public static ZenLootCondition randomChance(float chance)
    {
        return IMPLEMENTATION.randomChance(chance);
    }

    @ZenMethod
    public static ZenLootCondition randomChanceWithLooting(float chance, float lootingMultiplier)
    {
        return IMPLEMENTATION.randomChanceWithLooting(chance, lootingMultiplier);
    }

    @ZenMethod
    public static ZenLootCondition killedByPlayer()
    {
        return IMPLEMENTATION.killedByPlayer();
    }

    @ZenMethod
    public static ZenLootCondition killedByNonPlayer()
    {
        return IMPLEMENTATION.killedByNonPlayer();
    }

    @ZenMethod
    public static ZenLootCondition zenscript(ZenLambdaLootCondition.Delegate delegate)
    {
        return IMPLEMENTATION.zenscript(delegate);
    }
}
