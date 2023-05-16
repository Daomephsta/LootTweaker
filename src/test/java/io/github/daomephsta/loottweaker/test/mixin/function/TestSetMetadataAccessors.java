package io.github.daomephsta.loottweaker.test.mixin.function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.SetMetadata;

@Mixin(SetMetadata.class)
public interface TestSetMetadataAccessors
{
    @Accessor
    public RandomValueRange getMetaRange();
}