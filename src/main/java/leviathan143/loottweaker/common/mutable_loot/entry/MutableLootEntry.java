package leviathan143.loottweaker.common.mutable_loot.entry;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import leviathan143.loottweaker.common.LootTweaker;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.conditions.LootCondition;


public interface MutableLootEntry
{
    public static final Logger __LOGGER__ = LogManager.getLogger(LootTweaker.MODID + ".mutable_loot");

    public static MutableLootEntry from(LootEntry entry)
    {
        if (entry instanceof LootEntryItem)
            return new MutableLootEntryItem((LootEntryItem) entry);
        else if (entry instanceof LootEntryTable)
            return new MutableLootEntryTable((LootEntryTable) entry);
        else if (entry instanceof LootEntryEmpty)
            return new MutableLootEntryEmpty((LootEntryEmpty) entry);
        else
            return new GenericMutableLootEmpty(entry);
    }

    public MutableLootEntry deepClone();

    public LootEntry toImmutable();

    public int getWeight();

    public void setWeight(int weight);

    public int getQuality();

    public void setQuality(int quality);

    public List<LootCondition> getConditions();

    public void setConditions(List<LootCondition> conditions);

    public void addCondition(LootCondition condition);

    public void clearConditions();

    public String getName();

    public void setName(String name);
}