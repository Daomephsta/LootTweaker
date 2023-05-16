package io.github.daomephsta.loottweaker.test.mixin.condition;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;

@Mixin(RandomChanceWithLooting.class)
public interface TestRandomChanceWithLootingAccessors
{
    @Accessor
    public float getChance();

    @Accessor
    public float getLootingMultiplier();
}