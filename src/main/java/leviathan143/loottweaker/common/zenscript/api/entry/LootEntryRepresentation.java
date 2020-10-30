package leviathan143.loottweaker.common.zenscript.api.entry;

import stanhebben.zenscript.annotations.ZenMethod;

public interface LootEntryRepresentation
{
    @ZenMethod
    public String getName();

    public String describe();
}
