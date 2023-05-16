package io.github.daomephsta.loottweaker.test.mixin.condition;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.conditions.RandomChance;

@Mixin(RandomChance.class)
public interface TestRandomChanceAccessors
{
    @Accessor
    public float getChance();
}