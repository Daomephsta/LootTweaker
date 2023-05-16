package leviathan143.loottweaker.common.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;


@Mixin(LootConditionManager.class)
public interface LootConditionManagerAccessors
{
    @Accessor("CLASS_TO_SERIALIZER_MAP")
    public static Map<Class<? extends LootCondition>, LootCondition.Serializer<?>> getClassToSerialiserMap()
    {
        throw new IllegalStateException("Accessor stub invoked");
    }
}
