package leviathan143.loottweaker.common.zenscript.impl.entry;

import java.util.List;

import com.google.common.collect.Lists;

import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.darkmagic.LootEntryAccessors;
import leviathan143.loottweaker.common.lib.QualifiedEntryIdentifier;
import leviathan143.loottweaker.common.lib.QualifiedPoolIdentifier;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public abstract class AbstractMutableLootEntry implements MutableLootEntry
{
    private QualifiedEntryIdentifier qualifiedId;
    private int weight, quality;
    private List<LootCondition> conditions;
    protected final ErrorHandler errorHandler;

    protected AbstractMutableLootEntry(LootEntry entry, QualifiedPoolIdentifier poolId, ErrorHandler errorHandler)
    {
        this.qualifiedId = new QualifiedEntryIdentifier(poolId, entry.getEntryName());
        this.weight = LootEntryAccessors.getWeight(entry);
        this.quality = LootEntryAccessors.getQuality(entry);
        this.conditions = Lists.newArrayList(LootEntryAccessors.getConditions(entry));
        this.errorHandler = errorHandler;
    }

    protected AbstractMutableLootEntry(QualifiedEntryIdentifier qualifiedId, int weight, int quality, LootCondition[] conditions, ErrorHandler errorHandler)
    {
        this(qualifiedId, weight, quality, Lists.newArrayList(conditions), errorHandler);
    }

    protected AbstractMutableLootEntry(QualifiedEntryIdentifier qualifiedId, int weight, int quality, List<LootCondition> conditions, ErrorHandler errorHandler)
    {
        this.qualifiedId = qualifiedId;
        this.weight = weight;
        this.quality = quality;
        this.conditions = conditions;
        this.errorHandler = errorHandler;
    }

    @Override
    public abstract MutableLootEntry deepClone();

    @Override
    public abstract LootEntry toImmutable();

    @Override
    public int weight()
    {
        return weight;
    }

    @Override
    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    @Override
    public int quality()
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

    public QualifiedEntryIdentifier getQualifiedId()
    {
        return qualifiedId;
    }

    @Override
    public String name()
    {
        return qualifiedId.getEntryName();
    }

    @Override
    public void setName(String name)
    {
        qualifiedId.setEntryName(name);
    }

    @Override
    public String describe()
    {
        return qualifiedId.toString();
    }

    @Override
    public String toString()
    {
        return describe();
    }
}
