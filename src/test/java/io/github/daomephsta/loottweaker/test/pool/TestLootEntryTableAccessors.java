package io.github.daomephsta.loottweaker.test.pool;

import java.lang.invoke.MethodHandle;

import leviathan143.loottweaker.common.darkmagic.AbstractAccessors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryTable;

public class TestLootEntryTableAccessors extends AbstractAccessors
{
    private static final MethodHandle LootEntryTable$itemGetter;
    static
    {
        try
        {
            LootEntryTable$itemGetter = createFieldGetter(LootEntryTable.class, "table");
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("Failed to initialize test LootEntryTable accessor method handles", e);
        }
    }
    
    public static ResourceLocation getTable(LootEntryTable entry)
    {
        try
        {
            return (ResourceLocation) LootEntryTable$itemGetter.invokeExact(entry);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntryTable table getter method handle", e);
        }
    }
}
