package leviathan143.loottweaker.common.zenscript.api;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.LootTweaker;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootPool")
public interface LootPoolRepresentation
{
    @ZenMethod
    public void addEmptyEntry(@Optional String name);

    @ZenMethod
    public void addEmptyEntry(int weight, @Optional String name);

    @ZenMethod
    public void addEmptyEntry(int weight, int quality, @Optional String name);

    @ZenMethod
    public void addEmptyEntryJson(int weight, int quality, IData[] conditions, @Optional String name);

    @ZenMethod
    public void removeEntry(String entryId);

    @ZenMethod
    public void removeAllEntries();
}
