package leviathan143.loottweaker.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.google.gson.Gson;

import net.minecraft.world.storage.loot.LootTableManager;


@Mixin(LootTableManager.class)
public interface LootTableManagerAccessors
{
    @Accessor("GSON_INSTANCE")
    public static Gson getGsonInstance()
    {
        throw new IllegalStateException("Accessor stub invoked");
    }
}
