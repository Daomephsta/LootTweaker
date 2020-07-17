package leviathan143.loottweaker.common.zenscript.impl;

import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.zenscript.api.LootSystemInterface;

public class LootTweakerContext
{
    private final ErrorHandler errorHandler;

    public LootTweakerContext(ErrorHandler errorHandler)
    {
        this.errorHandler = errorHandler;
    }

    public ErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

    public LootSystemInterface lootSystem()
    {
        return new LootTweakerApi(this);
    }
}
