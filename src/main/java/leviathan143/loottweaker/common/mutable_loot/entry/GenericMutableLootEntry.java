package leviathan143.loottweaker.common.mutable_loot.entry;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import leviathan143.loottweaker.common.lib.LootConditions;
import leviathan143.loottweaker.common.mixin.LootEntryAccessors;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;


public class GenericMutableLootEntry implements MutableLootEntry
{
    private final LootEntry entry;

    GenericMutableLootEntry(LootEntry entry)
    {
        this.entry = entry;
    }

    @Override
    public MutableLootEntry deepClone()
    {
        throw new UnsupportedOperationException(
            String.format("Could not deep copy entry '%s' as it is a non-vanilla loot entry type (%s)", getName(),
                entry.getClass().getName()));
    }

    @Override
    public LootEntry toImmutable()
    {
        return entry;
    }

    @Override
    public int getWeight()
    {
        return ((LootEntryAccessors) entry).getWeight();
    }

    @Override
    public void setWeight(int weight)
    {
        ((LootEntryAccessors) entry).setWeight(weight);
    }

    @Override
    public int getQuality()
    {
        return ((LootEntryAccessors) entry).getQuality();
    }

    @Override
    public void setQuality(int quality)
    {
        ((LootEntryAccessors) entry).setQuality(quality);
    }

    @Override
    public List<LootCondition> getConditions()
    {
        return Arrays.asList(LootConditions.get(entry));
    }

    @Override
    public void setConditions(List<LootCondition> conditions)
    {
        ((LootEntryAccessors) entry).setConditions(conditions.toArray(LootConditions.NONE));
    }

    @Override
    public void addCondition(LootCondition condition)
    {
        ((LootEntryAccessors) entry).setConditions(
            ArrayUtils.add(LootConditions.get(entry), condition));
    }

    @Override
    public void clearConditions()
    {
        ((LootEntryAccessors) entry).setConditions(new LootCondition[0]);
    }

    @Override
    public String getName()
    {
        return entry.getEntryName();
    }

    @Override
    public void setName(String name)
    {
        ((LootEntryAccessors) entry).setName(name);
    }
}
