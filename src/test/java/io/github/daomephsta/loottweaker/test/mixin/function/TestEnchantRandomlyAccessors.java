package io.github.daomephsta.loottweaker.test.mixin.function;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;

@Mixin(EnchantRandomly.class)
public interface TestEnchantRandomlyAccessors
{
    @Accessor
    public List<Enchantment> getEnchantments();
}