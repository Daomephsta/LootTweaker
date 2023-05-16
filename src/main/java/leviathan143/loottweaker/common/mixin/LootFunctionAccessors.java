package leviathan143.loottweaker.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;


@Mixin(LootFunction.class)
public interface LootFunctionAccessors
{
    @Accessor
    public void setConditions(LootCondition[] conditions);
}
