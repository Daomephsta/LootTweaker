package leviathan143.loottweaker.common.zenscript.impl.entry;

import java.util.List;

import leviathan143.loottweaker.common.lib.LootConditions;
import leviathan143.loottweaker.common.lib.QualifiedEntryIdentifier;
import leviathan143.loottweaker.common.lib.QualifiedPoolIdentifier;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class MutableLootEntryEmpty extends AbstractMutableLootEntry
{
    MutableLootEntryEmpty(LootEntryEmpty entry, QualifiedPoolIdentifier qualifiedId)
    {
        super(entry, qualifiedId);
    }

    public MutableLootEntryEmpty(QualifiedEntryIdentifier qualifiedId, int weight, int quality, LootCondition[] conditions)
    {
        super(qualifiedId, weight, quality, conditions);
    }

    public MutableLootEntryEmpty(QualifiedEntryIdentifier qualifiedId, int weight, int quality, List<LootCondition> conditions)
    {
        super(qualifiedId, weight, quality, conditions);
    }

    @Override
    public MutableLootEntryEmpty deepClone()
    {
        return new MutableLootEntryEmpty(getQualifiedId(), getWeight(), getQuality(),
            LootConditions.deepClone(getConditions()));
    }

    @Override
    public LootEntryEmpty toImmutable()
    {
        return new LootEntryEmpty(getWeight(), getQuality(), getConditions().toArray(LootConditions.NONE), getName());
    }
}
