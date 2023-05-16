package io.github.daomephsta.loottweaker.test.mixin.function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.SetCount;

@Mixin(SetCount.class)
public interface TestSetCountAccessors
{
    @Accessor
    public RandomValueRange getCountRange();
}