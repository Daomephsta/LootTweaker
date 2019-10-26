package io.github.daomephsta.loottweaker.test.assertion;

import org.assertj.core.api.InstanceOfAssertFactory;

import io.github.daomephsta.loottweaker.test.assertion.loot.entry.LootEntryEmptyAssert;
import io.github.daomephsta.loottweaker.test.assertion.loot.entry.LootEntryItemAssert;
import io.github.daomephsta.loottweaker.test.assertion.loot.entry.LootEntryTableAssert;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;

public class LootTweakerInstanceOfAssertionFactories
{
    public static final InstanceOfAssertFactory<LootEntryItem, LootEntryItemAssert> LOOT_ENTRY_ITEM = 
        new InstanceOfAssertFactory<>(LootEntryItem.class, LootEntryItemAssert::new);
    public static final InstanceOfAssertFactory<LootEntryTable, LootEntryTableAssert> LOOT_ENTRY_TABLE= 
        new InstanceOfAssertFactory<>(LootEntryTable.class, LootEntryTableAssert::new);
    public static final InstanceOfAssertFactory<LootEntryEmpty, LootEntryEmptyAssert> LOOT_ENTRY_EMPTY = 
        new InstanceOfAssertFactory<>(LootEntryEmpty.class, LootEntryEmptyAssert::new);
}
