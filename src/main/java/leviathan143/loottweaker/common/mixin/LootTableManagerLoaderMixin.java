package leviathan143.loottweaker.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import leviathan143.loottweaker.common.duck.LootLoadingContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;

@Mixin(targets = "net.minecraft.world.storage.loot.LootTableManager$Loader")
public abstract class LootTableManagerLoaderMixin
{
    @Inject(method = "load", at = @At("HEAD"))
    private void lootweaker$pushContext(ResourceLocation id, CallbackInfoReturnable<LootTable> info)
    {
        LootLoadingContext.push().tableId = id;
    }

    @Inject(method = "load", at = @At("RETURN"))
    private void lootweaker$popContext(ResourceLocation id, CallbackInfoReturnable<LootTable> info)
    {
        LootLoadingContext.pop();
    }
}
