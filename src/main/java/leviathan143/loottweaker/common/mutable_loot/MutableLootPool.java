package leviathan143.loottweaker.common.mutable_loot;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

import leviathan143.loottweaker.common.lib.LootConditions;
import leviathan143.loottweaker.common.mixin.LootPoolAccessors;
import leviathan143.loottweaker.common.mutable_loot.entry.MutableLootEntry;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;


public class MutableLootPool
{
    private String name;
    private Map<String, MutableLootEntry> entries;
    private List<LootCondition> conditions;
    private RandomValueRange rolls, bonusRolls;

    MutableLootPool(LootPool pool)
    {
        this.name = pool.getName();
        List<LootEntry> immutableEntries = ((LootPoolAccessors) pool).getEntries();
        this.entries = new LinkedHashMap<>(immutableEntries.size());
        for (LootEntry entry : immutableEntries)
        {
            MutableLootEntry mutableEntry = MutableLootEntry.from(entry);
            entries.put(mutableEntry.getName(), mutableEntry);
        }
        this.conditions = ((LootPoolAccessors) pool).getConditions();
        this.rolls = pool.getRolls();
        this.bonusRolls = pool.getBonusRolls();
    }

    public MutableLootPool(String name, Map<String, MutableLootEntry> entries, List<LootCondition> conditions,
        RandomValueRange rolls, RandomValueRange bonusRolls)
    {
        this.name = name;
        this.entries = entries;
        this.conditions = conditions;
        this.rolls = rolls;
        this.bonusRolls = bonusRolls;
    }

    public MutableLootPool deepClone()
    {
        //Can never be duplicate entries when deep cloning, but be informative just in case
        BinaryOperator<MutableLootEntry> mergeFunction = (a, b) ->
        {
            throw new IllegalStateException(String.format(
                "Unexpected duplicate entry '%s' while deep cloning mutable pool '%s'. Report this to the mod author",
                a.getName(), getName()));
        };
        Map<String, MutableLootEntry> entriesDeepClone = entries.entrySet()
            .stream()
            .collect(toMap(Map.Entry::getKey, e -> e.getValue().deepClone(), mergeFunction, HashMap::new));
        return new MutableLootPool(name, entriesDeepClone, LootConditions.deepClone(conditions), rolls,
            bonusRolls);
    }

    public LootPool toImmutable()
    {
        LootEntry[] entriesArray = entries.values()
            .stream()
            .map(MutableLootEntry::toImmutable)
            .toArray(LootEntry[]::new);
        return new LootPool(entriesArray, conditions.toArray(LootConditions.NONE), rolls, bonusRolls, name);
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

    public void addConditions(List<LootCondition> newConditions)
    {
        conditions.addAll(newConditions);
    }

    public void clearConditions()
    {
        conditions.clear();
    }

    public RandomValueRange getRolls()
    {
        return rolls;
    }

    public void setRolls(RandomValueRange rolls)
    {
        this.rolls = rolls;
    }

    public RandomValueRange getBonusRolls()
    {
        return bonusRolls;
    }

    public void setBonusRolls(RandomValueRange bonusRolls)
    {
        this.bonusRolls = bonusRolls;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Map<String, MutableLootEntry> getEntries()
    {
        return entries;
    }

    public MutableLootEntry getEntry(String name)
    {
        return entries.get(name);
    }

    public void addEntry(MutableLootEntry entry)
    {
        if (entries.putIfAbsent(entry.getName(), entry) != null) throw new IllegalArgumentException(
            String.format("Duplicate entry name '%s' in pool '%s'", entry.getName(), this.getName()));
    }

    public MutableLootEntry removeEntry(String name)
    {
        return entries.remove(name);
    }

    public void clearEntries()
    {
        entries.clear();
    }
}
