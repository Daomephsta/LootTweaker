package leviathan143.loottweaker.common.mutable_loot.entry;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import leviathan143.loottweaker.common.darkmagic.LootEntryAccessors;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.DeepClone;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public abstract class MutableLootEntry<Self extends MutableLootEntry<Self, Immutable>, Immutable extends LootEntry> implements DeepClone<Self>
{
    private final String name;
    private int weight, quality;
    private List<LootCondition> conditions;

    protected MutableLootEntry(LootEntry entry)
    {
        this.name = entry.getEntryName();
        this.weight = LootEntryAccessors.getWeight(entry);
        this.quality = LootEntryAccessors.getQuality(entry);
        this.conditions = Lists.newArrayList(LootEntryAccessors.getConditions(entry));
    }

    protected MutableLootEntry(String name, int weight, int quality, LootCondition[] conditions)
    {
        this(name, weight, quality, Lists.newArrayList(conditions));
    }

    protected MutableLootEntry(String name, int weight, int quality, List<LootCondition> conditions)
    {
        this.name = name;
        this.weight = weight;
        this.quality = quality;
        this.conditions = conditions;
    }

    public static MutableLootEntry<?, ?> from(LootEntry entry)
    {
        if (entry instanceof LootEntryItem)
            return new MutableLootEntryItem((LootEntryItem) entry);
        else if (entry instanceof LootEntryTable)
            return new MutableLootEntryTable((LootEntryTable) entry);
        else if (entry instanceof LootEntryEmpty)
            return new MutableLootEntryEmpty((LootEntryEmpty) entry);
        else
            throw new IllegalArgumentException("Unknown loot entry type " + entry.getClass().getName());
    }

    protected List<LootCondition> deepCloneConditions()
    {
        List<LootCondition> clone = new ArrayList<>(conditions.size());
        for (int i = 0; i < conditions.size(); i++)
            clone.add(deepCloneCondition(conditions.get(i)));
        return clone;
    }

    private LootCondition deepCloneCondition(LootCondition lootCondition)
    {
        Gson lootTableGson = LootTableManagerAccessors.getGsonInstance();
        JsonElement json = lootTableGson.toJsonTree(lootCondition);
        return lootTableGson.fromJson(json, LootCondition.class);
    }

    public abstract Immutable toImmutable();

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    public int getQuality()
    {
        return quality;
    }

    public void setQuality(int quality)
    {
        this.quality = quality;
    }

    public List<LootCondition> getConditions()
    {
        return conditions;
    }

    public void setConditions(List<LootCondition> conditions)
    {
        this.conditions = conditions;
    }

    public void addCondition(LootCondition condition)
    {
        conditions.add(condition);
    }

    public void clearConditions()
    {
        conditions.clear();
    }

    public String getName()
    {
        return name;
    }
}
