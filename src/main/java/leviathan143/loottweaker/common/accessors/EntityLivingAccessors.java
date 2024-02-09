package leviathan143.loottweaker.common.accessors;

import java.lang.invoke.MethodHandle;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;


public class EntityLivingAccessors extends AbstractAccessors
{
    private static final MethodHandle entityLiving$getLootTable;

    static
    {
        try
        {
            entityLiving$getLootTable = method(EntityLiving.class, "func_184647_J").returnType(LootTable.class)
                .invoker();
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to initialize EntityLiving accessor method handles", t);
        }
    }

    public static ResourceLocation getLootTable(EntityLiving living)
    {
        try
        {
            return (ResourceLocation) entityLiving$getLootTable.invokeExact(living);
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Could not invoke entity loot table getter method handle", t);
        }
    }
}
