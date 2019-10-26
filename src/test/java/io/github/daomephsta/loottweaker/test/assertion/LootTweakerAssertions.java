package io.github.daomephsta.loottweaker.test.assertion;

import io.github.daomephsta.loottweaker.test.assertion.loot.LootPoolAssert;
import io.github.daomephsta.loottweaker.test.assertion.loot.entry.LootEntryAssert;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;

public class LootTweakerAssertions
{
    public static LootPoolAssert assertThat(LootPool pool)
    {
        return new LootPoolAssert(pool);
    }
    
    public static LootEntryAssert assertThat(LootEntry lootEntry)
    {
        return new LootEntryAssert(lootEntry);
    }
}
