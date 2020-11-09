package leviathan143.loottweaker.common.zenscript.impl.entry;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.QualifiedPoolIdentifier;
import leviathan143.loottweaker.common.zenscript.api.entry.LootEntryRepresentation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public interface MutableLootEntry extends LootEntryRepresentation
{
    public static final Logger __LOGGER__ = LogManager.getLogger(LootTweaker.MODID + ".mutable_loot");

    public static MutableLootEntry from(LootEntry entry, QualifiedPoolIdentifier parent)
    {
        if (entry instanceof LootEntryItem)
            return new MutableLootEntryItem((LootEntryItem) entry, parent);
        else if (entry instanceof LootEntryTable)
            return new MutableLootEntryTable((LootEntryTable) entry, parent);
        else if (entry instanceof LootEntryEmpty)
            return new MutableLootEntryEmpty((LootEntryEmpty) entry, parent);
        else
            return new GenericMutableLootEmpty(entry, parent);
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

    @Override
    public String getName();

    public void setName(String name);
}