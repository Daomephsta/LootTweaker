package leviathan143.loottweaker.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import leviathan143.loottweaker.common.LootTweaker;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

@Mixin(LootPool.class)
public class LootPoolMixin
{

    @Inject(method = "<init>", at = @At("TAIL"))
    public void loottweaker$uniqueEntryNames(LootEntry[] entries, LootCondition[] conditions,
        RandomValueRange rolls, RandomValueRange bonusRolls, String name, CallbackInfo info)
    {
        LootTweaker.extracted((LootPool) (Object) this, entries, name);
    }
}
