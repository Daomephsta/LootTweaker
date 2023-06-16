package leviathan143.loottweaker.common.mutable_loot.entry;

import java.util.List;

import com.google.common.collect.Lists;

import leviathan143.loottweaker.common.darkmagic.LootEntryAccessors;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;


public abstract class AbstractMutableLootEntry implements MutableLootEntry
{
    private String name;
    private int weight, quality;
    private List<LootCondition> conditions;

    protected AbstractMutableLootEntry(LootEntry entry)
    {
        this.name = entry.getEntryName();
        this.weight = LootEntryAccessors.getWeight(entry);
        this.quality = LootEntryAccessors.getQuality(entry);
        this.conditions = Lists.newArrayList(LootEntryAccessors.getConditions(entry));
    }

    protected AbstractMutableLootEntry(String name, int weight, int quality, LootCondition[] conditions)
    {
        this(name, weight, quality, Lists.newArrayList(conditions));
    }

    protected AbstractMutableLootEntry(String name, int weight, int quality, List<LootCondition> conditions)
    {
        this.name = name;
        this.weight = weight;
        this.quality = quality;
        this.conditions = conditions;
    }

    @Override
    public abstract MutableLootEntry deepClone();

    @Override
    public abstract LootEntry toImmutable();

    @Override
    public int getWeight()
    {
        return weight;
    }

    @Override
    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    @Override
    public int getQuality()
    {
        return quality;
    }

    @Override
    public void setQuality(int quality)
    {
        this.quality = quality;
    }

    @Override
    public List<LootCondition> getConditions()
    {
        return conditions;
    }

    @Override
    public void setConditions(List<LootCondition> conditions)
    {
        this.conditions = conditions;
    }

    @Override
    public void addCondition(LootCondition condition)
    {
        conditions.add(condition);
    }

    @Override
    public void clearConditions()
    {
        conditions.clear();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }
}
