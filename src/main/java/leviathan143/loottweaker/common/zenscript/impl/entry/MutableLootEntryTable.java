package leviathan143.loottweaker.common.zenscript.impl.entry;

import java.util.List;

import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.darkmagic.LootEntryTableAccessors;
import leviathan143.loottweaker.common.lib.LootConditions;
import leviathan143.loottweaker.common.lib.QualifiedEntryIdentifier;
import leviathan143.loottweaker.common.lib.QualifiedPoolIdentifier;
import leviathan143.loottweaker.common.zenscript.api.entry.LootEntryItemRepresentation;
import leviathan143.loottweaker.common.zenscript.api.entry.LootEntryTableRepresentation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class MutableLootEntryTable extends AbstractMutableLootEntry implements LootEntryTableRepresentation
{
    private ResourceLocation delegateTableId;

    MutableLootEntryTable(LootEntryTable entry, QualifiedPoolIdentifier qualifiedId, ErrorHandler errorHandler)
    {
        super(entry, qualifiedId, errorHandler);
        this.delegateTableId = LootEntryTableAccessors.getTable(entry);
    }

    public MutableLootEntryTable(QualifiedEntryIdentifier qualifiedId, int weight, int quality, LootCondition[] conditions, ResourceLocation delegateTableId, ErrorHandler errorHandler)
    {
        super(qualifiedId, weight, quality, conditions, errorHandler);
        this.delegateTableId = delegateTableId;
    }

    public MutableLootEntryTable(QualifiedEntryIdentifier qualifiedId, int weight, int quality, List<LootCondition> conditions, ResourceLocation delegateTableId, ErrorHandler errorHandler)
    {
        super(qualifiedId, weight, quality, conditions, errorHandler);
        this.delegateTableId = delegateTableId;
    }

    @Override
    public MutableLootEntryTable deepClone()
    {
        return new MutableLootEntryTable(getQualifiedId(), weight(), quality(),
            LootConditions.deepClone(getConditions()), delegateTableId, errorHandler);
    }

    @Override
    public LootEntryTable toImmutable()
    {
        return new LootEntryTable(delegateTableId, weight(), quality(),
            getConditions().toArray(LootConditions.NONE), name());
    }

    @Override
    public String tableId()
    {
        return delegateTableId.toString();
    }

    @Override
    public LootEntryItemRepresentation asItemEntry()
    {
        errorHandler.error("%s is not an ItemEntry", describe());
        return null;
    }
}
