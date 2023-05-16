package leviathan143.loottweaker.common.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;


@Mixin(LootFunctionManager.class)
public interface LootFunctionManagerAccessors
{
    @Accessor("CLASS_TO_SERIALIZER_MAP")
    public static Map<Class<? extends LootFunction>, LootFunction.Serializer<?>> getClassToSerialiserMap()
    {
        throw new IllegalStateException("Accessor stub invoked");
    }
}
