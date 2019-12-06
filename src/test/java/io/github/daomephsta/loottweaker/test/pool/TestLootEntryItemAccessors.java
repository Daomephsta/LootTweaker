package io.github.daomephsta.loottweaker.test.pool;

import java.lang.invoke.MethodHandle;

import leviathan143.loottweaker.common.darkmagic.AbstractAccessors;
import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class TestLootEntryItemAccessors extends AbstractAccessors
{
    private static final MethodHandle LootEntryItem$itemGetter,
                                      LootEntryItem$functionsGetter;
    static
    {
        try
        {
            LootEntryItem$itemGetter = createFieldGetter(LootEntryItem.class, "item");
            LootEntryItem$functionsGetter = createFieldGetter(LootEntryItem.class, "functions");
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to initialize test LootEntryItem accessor method handles", t);
        }
    }

    public static Item getItem(LootEntryItem entry)
    {
        try
        {
            return (Item) LootEntryItem$itemGetter.invokeExact(entry);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntryItem item getter method handle", e);
        }
    }

    public static LootFunction[] getFunctions(LootEntryItem entry)
    {
        try
        {
            return (LootFunction[]) LootEntryItem$functionsGetter.invokeExact(entry);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntryItem functions getter method handle", e);
        }
    }
}
