package io.github.daomephsta.loottweaker.test.mixin.condition;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.properties.EntityOnFire;

@Mixin(EntityOnFire.class)
public interface TestEntityOnFireAccessors
{
    @Accessor
    public boolean isOnFire();
}