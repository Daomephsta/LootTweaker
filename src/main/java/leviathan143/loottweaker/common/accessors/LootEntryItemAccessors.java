package leviathan143.loottweaker.common.accessors;

import java.lang.invoke.MethodHandle;

import leviathan143.loottweaker.common.lib.LootFunctions;
import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.functions.LootFunction;


public class LootEntryItemAccessors extends AbstractAccessors
{
    private static final MethodHandle LootEntryItem$itemGetter, LootEntryItem$functionsGetter;
    static
    {
        try
        {
            LootEntryItem$itemGetter = createFieldGetter(LootEntryItem.class, "field_186368_a", "item");
            LootEntryItem$functionsGetter = createFieldGetter(LootEntryItem.class, "field_186369_b", "functions");
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
        LootFunction[] functions = getFunctionsUnsafe(entry);
        return functions != null ? functions : LootFunctions.NONE;
    }

    public static LootFunction[] getFunctionsUnsafe(LootEntryItem entry)
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
