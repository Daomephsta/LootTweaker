package io.github.daomephsta.loottweaker.test.mixin.function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.EnchantWithLevels;

@Mixin(EnchantWithLevels.class)
public interface TestEnchantWithLevelsAccessors
{
    @Accessor("randomLevel")
    public RandomValueRange getLevelRange();

    @Accessor("isTreasure")
    public boolean isTreasure();
}