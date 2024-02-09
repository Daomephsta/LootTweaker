package io.github.daomephsta.loottweaker.test.pool;

import java.lang.invoke.MethodHandle;

import leviathan143.loottweaker.common.accessors.AbstractAccessors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryTable;


public class TestLootEntryTableAccessors extends AbstractAccessors
{
    private static final MethodHandle LootEntryTable$tableGetter;
    static
    {
        try
        {
            LootEntryTable$tableGetter = createFieldGetter(LootEntryTable.class, "table");
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to initialize test LootEntryTable accessor method handles", t);
        }
    }

    public static ResourceLocation getTable(LootEntryTable entry)
    {
        try
        {
            return (ResourceLocation) LootEntryTable$tableGetter.invokeExact(entry);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntryTable table getter method handle", e);
        }
    }
}
