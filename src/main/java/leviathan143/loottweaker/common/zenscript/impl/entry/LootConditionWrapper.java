package leviathan143.loottweaker.common.zenscript.impl.entry;

import leviathan143.loottweaker.common.zenscript.api.entry.LootConditionRepresentation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootConditionWrapper implements LootConditionRepresentation
{
    public static final LootConditionWrapper INVALID = new LootConditionWrapper(null);

    public final LootCondition condition;

    public LootConditionWrapper(LootCondition condition)
    {
        this.condition = condition;
    }

    @Override
    public boolean isValid()
    {
        return condition != null;
    }

    @Override
    public LootCondition toImmutable()
    {
        if (!isValid())
            throw new UnsupportedOperationException("Cannot unwrap an invalid LootConditionWrapper");
        return condition;
    }
}
