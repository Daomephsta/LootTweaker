package io.github.daomephsta.loottweaker.test.mixin.entry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;


@Mixin(LootEntry.class)
public interface TestLootEntryAccessors
{
    @Accessor
    public int getWeight();

    @Accessor
    public int getQuality();

    @Accessor
    public LootCondition[] getConditions();
}
