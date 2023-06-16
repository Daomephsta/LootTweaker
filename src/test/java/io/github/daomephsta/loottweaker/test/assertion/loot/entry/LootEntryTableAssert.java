package io.github.daomephsta.loottweaker.test.assertion.loot.entry;

import io.github.daomephsta.loottweaker.test.pool.TestLootEntryTableAccessors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryTable;


public class LootEntryTableAssert extends AbstractLootEntryAssert<LootEntryTableAssert, LootEntryTable>
{
    public LootEntryTableAssert(LootEntryTable actual)
    {
        super(actual, LootEntryTableAssert.class);
    }

    public LootEntryTableAssert spawnsFromTable(String expectedTable)
    {
        isNotNull();

        ResourceLocation actualTable = TestLootEntryTableAccessors.getTable(actual);
        if (!actualTable.equals(new ResourceLocation(expectedTable)))
            failWithMessage("Expected <%s>, was <%s>", expectedTable, actualTable);
        return this;
    }
}
