package io.github.daomephsta.loottweaker.test.assertion.loot.entry;

import io.github.daomephsta.loottweaker.test.assertion.LootTweakerInstanceOfAssertionFactories;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;

public class LootEntryAssert extends AbstractLootEntryAssert<LootEntryAssert, LootEntry>
{
    public LootEntryAssert(LootEntry actual)
    {
        super(actual, LootEntryAssert.class);
    }

    public LootEntryItemAssert asItemEntry()
    {
        isNotNull();
        
        if (!(actual instanceof LootEntryItem))
            failWithMessage("Expecting entry '%s' to be an item entry, was %s entry", actual.getEntryName(), getEntryType(actual));
        return asInstanceOf(LootTweakerInstanceOfAssertionFactories.LOOT_ENTRY_ITEM);
    }
    
    public LootEntryTableAssert asLootTableEntry()
    {
        isNotNull();
        
        if (!(actual instanceof LootEntryTable))
            failWithMessage("Expected entry '%s' to be a loot table entry, was %s entry", actual.getEntryName(), getEntryType(actual));
        return asInstanceOf(LootTweakerInstanceOfAssertionFactories.LOOT_ENTRY_TABLE);    
    }
    
    public LootEntryAssert isEmptyEntry()
    {
        isNotNull();
        
        if (!(actual instanceof LootEntryEmpty))
            failWithMessage("Expected entry '%s' to be an empty entry, was %s entry", actual.getEntryName(), getEntryType(actual));
        return this;    
    }
    
    private String getEntryType(LootEntry lootEntry)
    {
        if (lootEntry instanceof LootEntryItem)
            return "an item";
        else if (lootEntry instanceof LootEntryTable)
            return "a loot table";
        else if (lootEntry instanceof LootEntryEmpty)
            return "an empty";
        else return "an unknown";
    }
}
