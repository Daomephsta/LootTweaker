package leviathan143.loottweaker.common.mutable_loot.entry;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import leviathan143.loottweaker.common.accessors.LootEntryAccessors;
import leviathan143.loottweaker.common.lib.LootConditions;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;


public class GenericMutableLootEmpty implements MutableLootEntry
{
    private final LootEntry entry;

    GenericMutableLootEmpty(LootEntry entry)
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
        return LootEntryAccessors.getWeight(entry);
    }

    @Override
    public void setWeight(int weight)
    {
        LootEntryAccessors.setWeight(entry, weight);
    }

    @Override
    public int getQuality()
    {
        return LootEntryAccessors.getQuality(entry);
    }

    @Override
    public void setQuality(int quality)
    {
        LootEntryAccessors.setQuality(entry, quality);
    }

    @Override
    public List<LootCondition> getConditions()
    {
        return Arrays.asList(LootEntryAccessors.getConditions(entry));
    }

    @Override
    public void setConditions(List<LootCondition> conditions)
    {
        LootEntryAccessors.setConditions(entry, conditions.toArray(LootConditions.NONE));
    }

    @Override
    public void addCondition(LootCondition condition)
    {
        LootEntryAccessors.setConditions(entry,
            ArrayUtils.add(LootEntryAccessors.getConditions(entry), condition));
    }

    @Override
    public void clearConditions()
    {
        LootEntryAccessors.setConditions(entry, new LootCondition[0]);
    }

    @Override
    public String getName()
    {
        return entry.getEntryName();
    }

    @Override
    public void setName(String name)
    {
        LootEntryAccessors.setName(entry, name);
    }
}
