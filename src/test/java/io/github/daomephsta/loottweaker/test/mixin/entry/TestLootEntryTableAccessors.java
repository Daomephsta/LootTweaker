package io.github.daomephsta.loottweaker.test.mixin.entry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryTable;


@Mixin(LootEntryTable.class)
public interface TestLootEntryTableAccessors
{
    @Accessor
    public ResourceLocation getTable();
}
