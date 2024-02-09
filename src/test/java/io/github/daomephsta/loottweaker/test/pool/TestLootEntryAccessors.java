package io.github.daomephsta.loottweaker.test.pool;

import java.lang.invoke.MethodHandle;

import leviathan143.loottweaker.common.accessors.AbstractAccessors;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;


public class TestLootEntryAccessors extends AbstractAccessors
{
    private static final MethodHandle LootEntry$weightGetter, LootEntry$qualityGetter, LootEntry$conditionsGetter;
    static
    {
        try
        {
            LootEntry$weightGetter = field(LootEntry.class, "weight").getter();
            LootEntry$qualityGetter = field(LootEntry.class, "quality").getter();
            LootEntry$conditionsGetter = field(LootEntry.class, "conditions").getter();
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to initialize test LootEntry accessor method handles", t);
        }
    }

    public static int getWeight(LootEntry entry)
    {
        try
        {
            return (int) LootEntry$weightGetter.invokeExact(entry);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntry weight getter method handle", e);
        }
    }

    public static int getQuality(LootEntry entry)
    {
        try
        {
            return (int) LootEntry$qualityGetter.invokeExact(entry);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntry quality getter method handle", e);
        }
    }

    public static LootCondition[] getConditions(LootEntry entry)
    {
        try
        {
            return (LootCondition[]) LootEntry$conditionsGetter.invokeExact(entry);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntry conditions getter method handle", e);
        }
    }
}
