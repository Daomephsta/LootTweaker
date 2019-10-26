package io.github.daomephsta.loottweaker.test.assertion.loot.entry;

import java.util.function.Predicate;

import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.api.AbstractObjectAssert;

import io.github.daomephsta.loottweaker.test.pool.TestLootEntryAccessors;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class AbstractLootEntryAssert<SELF extends AbstractLootEntryAssert<SELF, ACTUAL>, ACTUAL extends LootEntry> 
    extends AbstractObjectAssert<SELF, ACTUAL>
{
    public AbstractLootEntryAssert(ACTUAL actual, Class<? extends AbstractLootEntryAssert<SELF,ACTUAL>> selfType)
    {
        super(actual, selfType);
    }

    public SELF hasWeight(int expectedWeight)
    {
        isNotNull();
        int actualWeight = TestLootEntryAccessors.getWeight(actual);
        if (actualWeight != expectedWeight)
            failWithMessage("Expected weight to be <%d>, but was <%d>", expectedWeight, actualWeight);
        return myself;
    }
    
    public SELF hasQuality(int expectedQuality)
    {
        isNotNull();
        int actualQuality = TestLootEntryAccessors.getQuality(actual);
        if (actualQuality != expectedQuality)
            failWithMessage("Expected quality to be <%d>, but was <%d>", expectedQuality, actualQuality);
        return myself;
    }

    public SELF hasNoLootConditions()
    {
        isNotNull();
        
        LootCondition[] actualConditions = TestLootEntryAccessors.getConditions(actual);
        if (actualConditions.length > 0)
            failWithMessage("Expected '%s' to have no loot conditions, has %s", actual.getEntryName(), ArrayUtils.toString(actualConditions));
        return myself;
    }

    public SELF hasMatchingCondition(Predicate<LootCondition> matcher, String descriptor)
    {
        int matches = 0;
        LootCondition[] actualConditions = TestLootEntryAccessors.getConditions(actual);
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
        return myself;
    }
    
    public SELF hasMatchingCondition(Predicate<LootCondition> matcher, String format, Object... args)
    {
        return hasMatchingCondition(matcher, String.format(format, args));
    }
}
