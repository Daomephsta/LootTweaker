package io.github.daomephsta.loottweaker.test.assertion.loot;

import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.api.AbstractObjectAssert;

import io.github.daomephsta.loottweaker.test.assertion.loot.entry.LootEntryAssert;
import leviathan143.loottweaker.common.darkmagic.LootPoolAccessors;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootPoolAssert extends AbstractObjectAssert<LootPoolAssert, LootPool>
{
    public LootPoolAssert(LootPool pool)
    {
        super(pool, LootPoolAssert.class);
    }
    
    public LootEntryAssert extractEntry(String entryName)
    {
        isNotNull();
        
        LootEntry lootEntry = actual.getEntry(entryName);
        assertEntryExists(entryName, lootEntry);
        return new LootEntryAssert(lootEntry);
    }

    private void assertEntryExists(String entryName, LootEntry lootEntry)
    {
        if (lootEntry == null)
            failWithMessage("%nExpected pool <%s> to contain entry named <%s>", actual.getName(), entryName);
    }

    public LootPoolAssert hasEntry(String entryName)
    {
        isNotNull();
        
        LootEntry lootEntry = actual.getEntry(entryName);
        assertEntryExists(entryName, lootEntry);
        return this;
    }

    public LootPoolAssert hasNoLootConditions()
    {
        isNotNull();
        
        List<LootCondition> actualConditions = LootPoolAccessors.getConditions(actual);
        if (!actualConditions.isEmpty())
            failWithMessage("Expected '%s' to have no loot conditions, has %s", actual.getName(), ArrayUtils.toString(actualConditions));
        return this;
    }

    public LootPoolAssert hasMatchingCondition(Predicate<LootCondition> matcher, String descriptor)
    {
        int matches = 0;
        List<LootCondition> actualConditions = LootPoolAccessors.getConditions(actual);
        for (LootCondition condition : actualConditions)
        {
            if (matcher.test(condition))
                matches++;
        }
        if (matches == 0)
        {
            failWithMessage("Expected exactly one condition in %s to match %s, but none matched", 
                ArrayUtils.toString(actualConditions), descriptor);
        }
        else if (matches > 1)
        {
            failWithMessage("Expected exactly one condition in %s to match %s, but %d matched", 
                ArrayUtils.toString(actualConditions), descriptor, matches);
        }
        return this;
    }
    
    public LootPoolAssert hasMatchingCondition(Predicate<LootCondition> matcher, String format, Object... args)
    {
        return hasMatchingCondition(matcher, String.format(format, args));
    }
}
