package leviathan143.loottweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;

import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class LootFunctionAccessors extends AbstractAccessors
{
    private static final MethodHandle LootFunction$conditionsSetter;
    static
    {
        try
        {
            LootFunction$conditionsSetter = createFieldSetter(LootFunction.class, "field_186555_a", "conditions");
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to initialize LootFunction accessor method handles", t);
        }
    }
    
    public static void setConditions(LootFunction function, LootCondition[] conditions)
    {
        try
        {
            LootFunction$conditionsSetter.invokeExact(function, conditions);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootFunction conditions getter method handle", e);
        }
    }
}
