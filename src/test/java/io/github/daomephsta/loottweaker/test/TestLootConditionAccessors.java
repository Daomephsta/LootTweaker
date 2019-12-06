package io.github.daomephsta.loottweaker.test;

import java.lang.invoke.MethodHandle;

import leviathan143.loottweaker.common.darkmagic.AbstractAccessors;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;

public class TestLootConditionAccessors extends AbstractAccessors
{
    private static final MethodHandle KilledByPlayer$inverseGetter,
                                      RandomChance$chanceGetter,
                                      RandomChanceWithLooting$chanceGetter,
                                      RandomChanceWithLooting$lootingMultiplierGetter;
    static
    {
        try
        {
            KilledByPlayer$inverseGetter = createFieldGetter(KilledByPlayer.class, "inverse");
            RandomChance$chanceGetter = createFieldGetter(RandomChance.class, "chance");
            RandomChanceWithLooting$chanceGetter = createFieldGetter(RandomChanceWithLooting.class, "chance");
            RandomChanceWithLooting$lootingMultiplierGetter = createFieldGetter(RandomChanceWithLooting.class, "lootingMultiplier");
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to initialize test loot condition accessor method handles", t);
        }
    }

    public static boolean isInverted(KilledByPlayer killedByPlayer)
    {
        try
        {
            return (boolean) KilledByPlayer$inverseGetter.invokeExact(killedByPlayer);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke KilledByPlayer inverse getter method handle", e);
        }
    }

    public static float getChance(RandomChance randomChance)
    {
        try
        {
            return (float) RandomChance$chanceGetter.invokeExact(randomChance);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke RandomChance chance getter method handle", e);
        }
    }

    public static float getChance(RandomChanceWithLooting randomChance)
    {
        try
        {
            return (float) RandomChanceWithLooting$chanceGetter.invokeExact(randomChance);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke RandomChanceWithLooting chance getter method handle", e);
        }
    }

    public static float getLootingMultiplier(RandomChanceWithLooting randomChance)
    {
        try
        {
            return (float) RandomChanceWithLooting$lootingMultiplierGetter.invokeExact(randomChance);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke RandomChanceWithLooting lootingMultiplier getter method handle", e);
        }
    }
}
