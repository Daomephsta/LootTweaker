package leviathan143.loottweaker.common.zenscript.impl.entry;

import java.util.List;

import leviathan143.loottweaker.common.darkmagic.LootEntryTableAccessors;
import leviathan143.loottweaker.common.lib.LootConditions;
import leviathan143.loottweaker.common.lib.QualifiedEntryIdentifier;
import leviathan143.loottweaker.common.lib.QualifiedPoolIdentifier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class MutableLootEntryTable extends AbstractMutableLootEntry
{
    private ResourceLocation delegateTableId;

    MutableLootEntryTable(LootEntryTable entry, QualifiedPoolIdentifier qualifiedId)
    {
        super(entry, qualifiedId);
        this.delegateTableId = LootEntryTableAccessors.getTable(entry);
    }

    public MutableLootEntryTable(QualifiedEntryIdentifier qualifiedId, int weight, int quality, LootCondition[] conditions, ResourceLocation delegateTableId)
    {
        super(qualifiedId, weight, quality, conditions);
        this.delegateTableId = delegateTableId;
    }

    public MutableLootEntryTable(QualifiedEntryIdentifier qualifiedId, int weight, int quality, List<LootCondition> conditions, ResourceLocation delegateTableId)
    {
        super(qualifiedId, weight, quality, conditions);
        this.delegateTableId = delegateTableId;
    }

    @Override
    public MutableLootEntryTable deepClone()
    {
        return new MutableLootEntryTable(getQualifiedId(), getWeight(), getQuality(),
            LootConditions.deepClone(getConditions()), delegateTableId);
    }

    @Override
    public LootEntryTable toImmutable()
    {
        return new LootEntryTable(delegateTableId, getWeight(), getQuality(),
            getConditions().toArray(LootConditions.NONE), getName());
    }

    public ResourceLocation getDelegateTableId()
    {
        return delegateTableId;
    }

    public void setDelegateTableId(ResourceLocation delegateTableId)
    {
        this.delegateTableId = delegateTableId;
    }
}
