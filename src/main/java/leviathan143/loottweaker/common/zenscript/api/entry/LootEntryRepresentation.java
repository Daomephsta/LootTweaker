package leviathan143.loottweaker.common.zenscript.api.entry;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.Describable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootEntry")
public interface LootEntryRepresentation extends Describable
{
    @ZenGetter
    public String name();

    @ZenGetter
    public int weight();

    @ZenGetter
    public int quality();

    @ZenMethod
    default public boolean isItemEntry()
    {
        return false;
    }

    @ZenMethod
    default public boolean isTableEntry()
    {
        return false;
    }

    @ZenMethod
    default public boolean isUnknownType()
    {
        return false;
    }

    @ZenMethod
    public LootEntryItemRepresentation asItemEntry();

    @ZenMethod
    public LootEntryTableRepresentation asTableEntry();
}
