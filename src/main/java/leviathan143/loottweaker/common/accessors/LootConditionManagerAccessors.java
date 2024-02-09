package leviathan143.loottweaker.common.accessors;

import java.lang.invoke.MethodHandle;
import java.util.Map;

import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;


public class LootConditionManagerAccessors extends AbstractAccessors
{
    private static final MethodHandle LootConditionManager$CLASS_TO_SERIALIZER_MAPGetter;

    static
    {
        try
        {
            LootConditionManager$CLASS_TO_SERIALIZER_MAPGetter = field(LootConditionManager.class, "field_186643_b").getter();
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to initialize LootFunctionManager accessor method handles", t);
        }
    }

    public static Map<Class<? extends LootCondition>, LootCondition.Serializer<?>> getClassToSerialiserMap()
    {
        try
        {
            return (Map<Class<? extends LootCondition>, LootCondition.Serializer<?>>) LootConditionManager$CLASS_TO_SERIALIZER_MAPGetter
                .invokeExact();
        }
        catch (Throwable t)
        {
            throw new RuntimeException(
                "Could not invoke LootFunctionManager class to serialiser map getter method handle", t);
        }
    }
}
