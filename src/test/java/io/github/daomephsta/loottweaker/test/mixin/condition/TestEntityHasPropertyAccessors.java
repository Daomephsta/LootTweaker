package io.github.daomephsta.loottweaker.test.mixin.condition;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.properties.EntityProperty;

@Mixin(EntityHasProperty.class)
public interface TestEntityHasPropertyAccessors
{
    @Accessor
    public EntityTarget getTarget();

    @Accessor
    public EntityProperty[] getProperties();
}