package io.github.daomephsta.loottweaker.test.assertion.loot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.AbstractObjectAssert;

import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;

public class LootTableAssert extends AbstractObjectAssert<LootTableAssert, LootTable>
{
    public LootTableAssert(LootTable actual)
    {
        super(actual, LootTableAssert.class);
    }

    public LootTableAssert hasPool(String poolName)
    {
        isNotNull();

        LootPool pool = actual.getPool(poolName);
        if (pool == null)
            failWithMessage("%nExpected table to contain pool named <%s>", poolName);
        return this;
    }

    public LootTableAssert hasPools(String... poolNames)
    {
        isNotNull();

        Set<String> missing = new HashSet<>();
        for (String name : poolNames)
        {
            if (actual.getPool(name) == null)
                missing.add(name);
        }
        if (!missing.isEmpty())
        {
            failWithMessage("%nExpected table to contain pools %s, missing %s",
                Arrays.toString(poolNames), missing);
        }
        return this;
    }

    public LootTableAssert doesNotHavePools(String... poolNames)
    {
        isNotNull();

        Set<String> found = new HashSet<>();
        for (String name : poolNames)
        {
            if (actual.getPool(name) != null)
                found.add(name);
        }
        if (!found.isEmpty())
        {
            failWithMessage("%nExpected table not to contain pools %s, found %s",
                Arrays.toString(poolNames), found);
        }
        return this;
    }
}
