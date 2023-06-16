package leviathan143.loottweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import leviathan143.loottweaker.common.lib.LootConditions;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;


public class LootEntryAccessors extends AbstractAccessors
{
    private static final MethodHandle LootEntry$weightGetter, LootEntry$weightSetter, LootEntry$qualityGetter,
        LootEntry$qualitySetter, LootEntry$conditionsGetter, LootEntry$conditionsSetter, LootEntry$nameSetter,
        LootEntry$serialize;
    static
    {
        try
        {
            LootEntry$weightGetter = createFieldGetter(LootEntry.class, "field_186364_c", "weight");
            LootEntry$weightSetter = createFieldSetter(LootEntry.class, "field_186364_c", "weight");
            LootEntry$qualityGetter = createFieldGetter(LootEntry.class, "field_186365_d", "quality");
            LootEntry$qualitySetter = createFieldSetter(LootEntry.class, "field_186365_d", "quality");
            LootEntry$conditionsGetter = createFieldGetter(LootEntry.class, "field_186366_e", "conditions");
            LootEntry$conditionsSetter = createFieldSetter(LootEntry.class, "field_186366_e", "conditions");
            LootEntry$nameSetter = createFieldSetter(LootEntry.class, "entryName");
            LootEntry$serialize = createMethodInvoker(LootEntry.class, "func_186362_a ", "serialize",
                JsonObject.class, JsonSerializationContext.class);
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

    public static void setWeight(LootEntry entry, int weight)
    {
        try
        {
            LootEntry$weightSetter.invokeExact(entry, weight);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntry weight setter method handle", e);
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

    public static void setQuality(LootEntry entry, int quality)
    {
        try
        {
            LootEntry$qualitySetter.invokeExact(entry, quality);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntry quality setter method handle", e);
        }
    }

    public static LootCondition[] getConditions(LootEntry entry)
    {
        LootCondition[] conditions = getConditionsUnsafe(entry);
        return conditions != null ? conditions : LootConditions.NONE;
    }

    public static LootCondition[] getConditionsUnsafe(LootEntry entry)
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

    public static void setConditions(LootEntry entry, LootCondition[] conditions)
    {
        try
        {
            LootEntry$conditionsSetter.invokeExact(entry, conditions);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntry conditions setter method handle", e);
        }
    }

    public static void setName(LootEntry entry, String name)
    {
        try
        {
            LootEntry$nameSetter.invokeExact(entry, name);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntry name setter method handle", e);
        }
    }

    public static void serialise(LootEntry entry, JsonObject json, JsonSerializationContext context)
    {
        try
        {
            LootEntry$serialize.invokeExact(entry, json, context);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntry.serialize method handle", e);
        }
    }
}
