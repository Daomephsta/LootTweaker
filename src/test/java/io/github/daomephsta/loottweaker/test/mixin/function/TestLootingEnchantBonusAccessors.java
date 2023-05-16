package io.github.daomephsta.loottweaker.test.mixin.function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;

@Mixin(LootingEnchantBonus.class)
public interface TestLootingEnchantBonusAccessors
{
    @Accessor("count")
    public RandomValueRange getBonusRange();

    @Accessor
    public int getLimit();
}