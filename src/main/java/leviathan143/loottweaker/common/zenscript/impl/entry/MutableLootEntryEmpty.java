package leviathan143.loottweaker.common.zenscript.impl.entry;

import java.util.List;

import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.lib.LootConditions;
import leviathan143.loottweaker.common.lib.QualifiedEntryIdentifier;
import leviathan143.loottweaker.common.lib.QualifiedPoolIdentifier;
import leviathan143.loottweaker.common.zenscript.api.entry.LootEntryItemRepresentation;
import leviathan143.loottweaker.common.zenscript.api.entry.LootEntryTableRepresentation;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class MutableLootEntryEmpty extends AbstractMutableLootEntry
{
    MutableLootEntryEmpty(LootEntryEmpty entry, QualifiedPoolIdentifier qualifiedId, ErrorHandler errorHandler)
    {
        super(entry, qualifiedId, errorHandler);
    }

    public MutableLootEntryEmpty(QualifiedEntryIdentifier qualifiedId, int weight, int quality, LootCondition[] conditions, ErrorHandler errorHandler)
    {
        super(qualifiedId, weight, quality, conditions, errorHandler);
    }

    public MutableLootEntryEmpty(QualifiedEntryIdentifier qualifiedId, int weight, int quality, List<LootCondition> conditions, ErrorHandler errorHandler)
    {
        super(qualifiedId, weight, quality, conditions, errorHandler);
    }

    @Override
    public MutableLootEntryEmpty deepClone()
    {
        return new MutableLootEntryEmpty(getQualifiedId(), weight(), quality(),
            LootConditions.deepClone(getConditions()), errorHandler);
    }

    @Override
    public LootEntryEmpty toImmutable()
    {
        return new LootEntryEmpty(weight(), quality(), getConditions().toArray(LootConditions.NONE), name());
    }

    @Override
    public LootEntryItemRepresentation asItemEntry()
    {
        errorHandler.error("%s is not an ItemEntry", describe());
        return null;
    }

    @Override
    public LootEntryTableRepresentation asTableEntry()
    {
        errorHandler.error("%s is not a TableEntry", describe());
        return null;
    }
}
