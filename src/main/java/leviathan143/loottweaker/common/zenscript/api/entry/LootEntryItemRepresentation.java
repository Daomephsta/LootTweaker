package leviathan143.loottweaker.common.zenscript.api.entry;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.LootTweaker;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootEntryItem")
public interface LootEntryItemRepresentation extends LootEntryRepresentation
{
    @ZenGetter
    public String itemId();

    @ZenGetter("minMeta")
    public int minimumMetadata();

    @ZenGetter("maxMeta")
    public int maximumMetadata();

    @ZenGetter("minDamageAmount")
    public int minimumDamageAmount();

    @ZenGetter("maxDamageAmount")
    public int maximumDamageAmount();

    @ZenGetter("minDamagePercent")
    public float minimumDamagePercent();

    @ZenGetter("maxDamagePercent")
    public float maximumDamagePercent();

    @ZenGetter
    public IData nbt();

    @Override
    default LootEntryItemRepresentation asItemEntry()
    {
        return this;
    }

    @Override
    default boolean isItemEntry()
    {
        return true;
    }
}
