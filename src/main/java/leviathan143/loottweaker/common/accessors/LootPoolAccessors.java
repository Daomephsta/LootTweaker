package leviathan143.loottweaker.common.accessors;

import java.lang.invoke.MethodHandle;
import java.util.List;

import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootCondition;


public class LootPoolAccessors extends AbstractAccessors
{
    private static final MethodHandle lootPool$poolConditionsGetter, LootPool$lootEntriesGetter;

    static
    {
        try
        {
            lootPool$poolConditionsGetter = field(LootPool.class, "field_186454_b").getter();
            LootPool$lootEntriesGetter = field(LootPool.class, "field_186453_a").getter();
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to initialize LootPool accessor method handles", t);
        }
    }

    public static List<LootCondition> getConditions(LootPool pool)
    {
        try
        {
            return (List<LootCondition>) lootPool$poolConditionsGetter.invokeExact(pool);
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Could not invoke loot table pool conditions getter method handle", t);
        }
    }

    public static List<LootEntry> getEntries(LootPool lootPool)
    {
        try
        {
            return (List<LootEntry>) LootPool$lootEntriesGetter.invokeExact(lootPool);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootPool lootEntries getter method handle", e);
        }
    }
}
