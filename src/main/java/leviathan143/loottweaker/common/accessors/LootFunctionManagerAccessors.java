package leviathan143.loottweaker.common.accessors;

import java.lang.invoke.MethodHandle;
import java.util.Map;

import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;


public class LootFunctionManagerAccessors extends AbstractAccessors
{
    private static final MethodHandle LootFunctionManager$CLASS_TO_SERIALIZER_MAPGetter;

    static
    {
        try
        {
            LootFunctionManager$CLASS_TO_SERIALIZER_MAPGetter = createFieldGetter(LootFunctionManager.class,
                "field_186585_b", "CLASS_TO_SERIALIZER_MAP");
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to initialize LootFunctionManager accessor method handles", t);
        }
    }

    public static Map<Class<? extends LootFunction>, LootFunction.Serializer<?>> getClassToSerialiserMap()
    {
        try
        {
            return (Map<Class<? extends LootFunction>, LootFunction.Serializer<?>>) LootFunctionManager$CLASS_TO_SERIALIZER_MAPGetter
                .invokeExact();
        }
        catch (Throwable t)
        {
            throw new RuntimeException(
                "Could not invoke LootFunctionManager class to serialiser map getter method handle", t);
        }
    }
}
