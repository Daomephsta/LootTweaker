package leviathan143.loottweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;

import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootEntryAccessors extends AbstractAccessors
{
    private static final MethodHandle LootEntry$weightGetter,
                                      LootEntry$qualityGetter,
                                      LootEntry$conditionsGetter;
    static
    {
        try
        {
            LootEntry$weightGetter = createFieldGetter(LootEntry.class, "field_186364_c", "weight");
            LootEntry$qualityGetter = createFieldGetter(LootEntry.class, "field_186365_d", "quality");
            LootEntry$conditionsGetter = createFieldGetter(LootEntry.class, "field_186366_e", "conditions");
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
