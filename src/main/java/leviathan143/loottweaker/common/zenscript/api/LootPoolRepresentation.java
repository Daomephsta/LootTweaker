package leviathan143.loottweaker.common.zenscript.api;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import crafttweaker.api.item.IItemStack;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.api.entry.LootConditionRepresentation;
import leviathan143.loottweaker.common.zenscript.api.entry.LootFunctionRepresentation;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootPool")
public interface LootPoolRepresentation
{
    @ZenMethod
    public void addItemEntry(IItemStack stack, @Optional String name);

    @ZenMethod
    public void addItemEntry(IItemStack stack, int weight, @Optional String name);

    @ZenMethod
    public void addItemEntry(IItemStack stack, int weight, int quality, @Optional String name);

    @ZenMethod
    public void addItemEntryJson(IItemStack stack, int weight, int quality, IData[] functions, IData[] conditions, @Optional String name);

    @ZenMethod
    public void addItemEntryHelper(IItemStack stack, int weight, int quality, LootFunctionRepresentation[] functions,
        LootConditionRepresentation[] conditions, @Optional String name);

    @ZenMethod
    public void addLootTableEntry(String delegateTableId, @Optional String name);

    @ZenMethod
    public void addLootTableEntry(String delegateTableId, int weight, @Optional String name);

    @ZenMethod
    public void addLootTableEntry(String delegateTableId, int weight, int quality, @Optional String name);

    @ZenMethod
    public void addLootTableEntryJson(String delegateTableId, int weight, int quality, IData[] conditions, @Optional String name);

    @ZenMethod
    public void addLootTableEntryHelper(String delegateTableId, int weight, int quality, LootConditionRepresentation[] conditions, @Optional String name);

    @ZenMethod
    public void addEmptyEntry(@Optional String name);

    @ZenMethod
    public void addEmptyEntry(int weight, @Optional String name);

    @ZenMethod
    public void addEmptyEntry(int weight, int quality, @Optional String name);

    @ZenMethod
    public void addEmptyEntryJson(int weight, int quality, IData[] conditions, @Optional String name);

    @ZenMethod
    public void addEmptyEntryHelper(int weight, int quality, LootConditionRepresentation[] conditions, @Optional String name);

    @ZenMethod
    public void removeEntry(String entryId);

    @ZenMethod
    public void removeAllEntries();

    @ZenMethod
    public void addConditionsJson(IData[] conditions);

    @ZenMethod
    public void addConditionsHelper(LootConditionRepresentation[] conditions);

    @ZenMethod
    public void removeAllConditions();

    @ZenMethod
    public void setRolls(float min, float max);

    @ZenMethod
    public void setBonusRolls(float min, float max);
}
