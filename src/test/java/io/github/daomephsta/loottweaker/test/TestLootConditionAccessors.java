package io.github.daomephsta.loottweaker.test;

import java.lang.invoke.MethodHandle;

import leviathan143.loottweaker.common.accessors.AbstractAccessors;
import net.minecraft.world.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.storage.loot.properties.EntityOnFire;
import net.minecraft.world.storage.loot.properties.EntityProperty;


public class TestLootConditionAccessors extends AbstractAccessors
{
    private static final MethodHandle KilledByPlayer$inverseGetter, RandomChance$chanceGetter,
        RandomChanceWithLooting$chanceGetter, RandomChanceWithLooting$lootingMultiplierGetter,
        EntityHasProperty$targetGetter, EntityHasProperty$propertiesGetter, EntityOnFire$onFireGetter;
    static
    {
        try
        {
            KilledByPlayer$inverseGetter = field(KilledByPlayer.class, "inverse").getter();
            RandomChance$chanceGetter = field(RandomChance.class, "chance").getter();
            RandomChanceWithLooting$chanceGetter = field(RandomChanceWithLooting.class, "chance").getter();
            RandomChanceWithLooting$lootingMultiplierGetter = field(RandomChanceWithLooting.class, "lootingMultiplier").getter();
            EntityHasProperty$targetGetter = field(EntityHasProperty.class, "target").getter();
            EntityHasProperty$propertiesGetter = field(EntityHasProperty.class, "properties").getter();
            EntityOnFire$onFireGetter = field(EntityOnFire.class, "onFire").getter();
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
            throw new RuntimeException(
                "Could not invoke RandomChanceWithLooting lootingMultiplier getter method handle", e);
        }
    }

    public static EntityTarget getTarget(EntityHasProperty entityHasProperty)
    {
        try
        {
            return (EntityTarget) EntityHasProperty$targetGetter.invokeExact(entityHasProperty);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke EntityHasProperty target getter method handle", e);
        }
    }

    public static EntityProperty[] getProperties(EntityHasProperty entityHasProperty)
    {
        try
        {
            return (EntityProperty[]) EntityHasProperty$propertiesGetter.invokeExact(entityHasProperty);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke EntityHasProperty properties getter method handle", e);
        }
    }

    public static boolean isOnFire(EntityOnFire entityOnFire)
    {
        try
        {
            return (boolean) EntityOnFire$onFireGetter.invokeExact(entityOnFire);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke EntityOnFire onFire getter method handle", e);
        }
    }
}
