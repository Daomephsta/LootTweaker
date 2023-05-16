package io.github.daomephsta.loottweaker.test.assertion.loot.entry;

import java.util.function.Predicate;

import org.apache.commons.lang3.ArrayUtils;

import io.github.daomephsta.loottweaker.test.mixin.entry.TestLootEntryItemAccessors;
import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.functions.LootFunction;


public class LootEntryItemAssert extends AbstractLootEntryAssert<LootEntryItemAssert, LootEntryItem>
{
    public LootEntryItemAssert(LootEntryItem actual)
    {
        super(actual, LootEntryItemAssert.class);
    }

    public LootEntryItemAssert spawnsItem(Item expectedItem)
    {
        isNotNull();

        Item actualItem = ((TestLootEntryItemAccessors) actual).getItem();
        if (actualItem != expectedItem) failWithMessage("Expected <%s>, was <%s>", expectedItem.getRegistryName(),
            actualItem.getRegistryName());
        return this;
    }

    public LootEntryItemAssert hasNoLootFunctions()
    {
        isNotNull();

        LootFunction[] actualFunctions = ((TestLootEntryItemAccessors) actual).getFunctions();
        if (actualFunctions.length > 0) failWithMessage("Expected '%s' to have no loot functions, has %s",
            actual.getEntryName(), ArrayUtils.toString(actualFunctions));
        return this;
    }

    public LootEntryItemAssert hasMatchingFunction(Predicate<LootFunction> matcher, String descriptor)
    {
        int matches = 0;
        LootFunction[] actualFunctions = ((TestLootEntryItemAccessors) actual).getFunctions();
        for (LootFunction function : actualFunctions)
        {
            if (matcher.test(function)) matches++;
        }
        if (matches == 0)
        {
            failWithMessage("Expected exactly one function in %s to match %s, but none matched",
                ArrayUtils.toString(actualFunctions), descriptor);
        }
        else if (matches > 1)
        {
            failWithMessage("Expected exactly one function in %s to match %s, but %d matched",
                ArrayUtils.toString(actualFunctions), descriptor, matches);
        }
        return this;
    }

    public LootEntryItemAssert hasMatchingFunction(Predicate<LootFunction> matcher, String format, Object... args)
    {
        return hasMatchingFunction(matcher, String.format(format, args));
    }
}
