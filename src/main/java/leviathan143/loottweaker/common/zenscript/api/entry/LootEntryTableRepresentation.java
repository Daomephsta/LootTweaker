package leviathan143.loottweaker.common.zenscript.api.entry;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootEntryTable")
public interface LootEntryTableRepresentation extends LootEntryRepresentation
{
    @ZenGetter
    public String tableId();

    @Override
    default LootEntryTableRepresentation asTableEntry()
    {
        return this;
    }

    @Override
    default boolean isTableEntry()
    {
        return true;
    }
}
