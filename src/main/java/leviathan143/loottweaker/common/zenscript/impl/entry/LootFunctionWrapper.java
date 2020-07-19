package leviathan143.loottweaker.common.zenscript.impl.entry;

import leviathan143.loottweaker.common.zenscript.api.entry.LootFunctionRepresentation;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class LootFunctionWrapper implements LootFunctionRepresentation
{
    public static final LootFunctionWrapper INVALID = new LootFunctionWrapper(null);

    public final LootFunction function;

    public LootFunctionWrapper(LootFunction function)
    {
        this.function = function;
    }

    @Override
    public boolean isValid()
    {
        return function != null;
    }

    @Override
    public LootFunction toImmutable()
    {
        if (!isValid())
            throw new UnsupportedOperationException("Cannot unwrap an invalid LootFunctionWrapper");
        return function;
    }
}
