package leviathan143.loottweaker.common.zenscript.impl.entry;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import leviathan143.loottweaker.common.ErrorHandler;
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

    public static MutableLootEntry from(LootEntry entry, QualifiedPoolIdentifier parent, ErrorHandler errorHandler)
    {
        if (entry instanceof LootEntryItem)
            return new MutableLootEntryItem((LootEntryItem) entry, parent, errorHandler);
        else if (entry instanceof LootEntryTable)
            return new MutableLootEntryTable((LootEntryTable) entry, parent, errorHandler);
        else if (entry instanceof LootEntryEmpty)
            return new MutableLootEntryEmpty((LootEntryEmpty) entry, parent, errorHandler);
        else
            return new GenericMutableLootEntry(entry, parent, errorHandler);
    }

    public MutableLootEntry deepClone();

    public LootEntry toImmutable();

    public int weight();

    public void setWeight(int weight);

    public int quality();

    public void setQuality(int quality);

    public List<LootCondition> getConditions();

    public void setConditions(List<LootCondition> conditions);

    public void addCondition(LootCondition condition);

    public void clearConditions();

    @Override
    public String name();

    public void setName(String name);
}