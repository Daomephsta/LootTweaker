package leviathan143.loottweaker.common.zenscript.api;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootTableLoadEvent")
public interface LootTableLoadCraftTweakerEvent
{
    @ZenGetter("tableId")
    public String getTableId();

    @ZenGetter("table")
    public LootTableRepresentation getTable();
}
