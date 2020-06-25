package io.github.daomephsta.loottweaker.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.mutable_loot.entry.MutableLootEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;

public class NonVanillaLootEntryTypeTests
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addCondition()
    {
        MutableLootEntry test = MutableLootEntry.from(new NonVanillaLootEntryType(5, 1, new LootCondition[] {new KilledByPlayer(true)}, "test"));
        assertThat(test.getConditions()).hasSize(1).hasOnlyElementsOfType(KilledByPlayer.class);
        test.addCondition(new KilledByPlayer(false));
        assertThat(test.getConditions()).hasSize(2).hasOnlyElementsOfType(KilledByPlayer.class);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void clearConditions()
    {
        MutableLootEntry test = MutableLootEntry.from(new NonVanillaLootEntryType(5, 1, new LootCondition[] {new KilledByPlayer(true)}, "test"));
        assertThat(test.getConditions()).hasSize(1).hasOnlyElementsOfType(KilledByPlayer.class);
        test.clearConditions();
        assertThat(test.getConditions()).isEmpty();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setConditions()
    {
        MutableLootEntry test = MutableLootEntry.from(new NonVanillaLootEntryType(5, 1, new LootCondition[] {new KilledByPlayer(true)}, "test"));
        assertThat(test.getConditions()).hasSize(1).hasOnlyElementsOfType(KilledByPlayer.class);
        test.setConditions(Arrays.asList(new RandomChance(0.2F)));
        assertThat(test.getConditions()).hasSize(1).hasOnlyElementsOfType(RandomChance.class);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void name()
    {
        MutableLootEntry test = MutableLootEntry.from(new NonVanillaLootEntryType(5, 1, new LootCondition[0], "test"));
        assertThat(test.getName()).isEqualTo("test");
        test.setName("foo");
        assertThat(test.getName()).isEqualTo("foo");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void quality()
    {
        MutableLootEntry test = MutableLootEntry.from(new NonVanillaLootEntryType(5, 1, new LootCondition[0], "test"));
        assertThat(test.getQuality()).isEqualTo(1);
        test.setQuality(2);
        assertThat(test.getQuality()).isEqualTo(2);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void weight()
    {
        MutableLootEntry test = MutableLootEntry.from(new NonVanillaLootEntryType(5, 1, new LootCondition[0], "test"));
        assertThat(test.getWeight()).isEqualTo(5);
        test.setWeight(3);
        assertThat(test.getWeight()).isEqualTo(3);
    }

    private static class NonVanillaLootEntryType extends LootEntry
    {
        public NonVanillaLootEntryType(int weightIn, int qualityIn, LootCondition[] conditionsIn, String entryName)
        {
            super(weightIn, qualityIn, conditionsIn, entryName);
        }

        @Override
        public void addLoot(Collection<ItemStack> stacks, Random rand, LootContext context)
        {}

        @Override
        protected void serialize(JsonObject json, JsonSerializationContext context)
        {}
    }
}
