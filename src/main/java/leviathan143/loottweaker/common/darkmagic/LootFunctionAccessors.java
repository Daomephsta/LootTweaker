package leviathan143.loottweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;

public class LootFunctionAccessors extends AbstractAccessors
{
    private static final MethodHandle SetCount$countRangeGetter,
                                      SetDamage$damageRangeGetter,
                                      SetMetadata$metaRangeGetter,
                                      SetNBT$tagGetterGetter;
    static
    {
        try
        {
            SetCount$countRangeGetter = createFieldGetter(SetCount.class, "field_186568_a", "countRange");
            SetDamage$damageRangeGetter = createFieldGetter(SetDamage.class, "field_186566_b", "damageRange");
            SetMetadata$metaRangeGetter = createFieldGetter(SetMetadata.class, "field_186573_b", "metaRange");
            SetNBT$tagGetterGetter = createFieldGetter(SetNBT.class, "field_186570_a", "tag");
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to initialize loot function test accessor method handles", t);
        }
    }

    public static RandomValueRange getCountRange(SetCount setCount)
    {
        try
        {
            return (RandomValueRange) SetCount$countRangeGetter.invokeExact(setCount);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke SetCount countRange getter method handle", e);
        }
    }

    public static RandomValueRange getDamageRange(SetDamage setDamage)
    {
        try
        {
            return (RandomValueRange) SetDamage$damageRangeGetter.invokeExact(setDamage);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke SetDamage damageRange getter method handle", e);
        }
    }

    public static RandomValueRange getMetaRange(SetMetadata setMetadata)
    {
        try
        {
            return (RandomValueRange) SetMetadata$metaRangeGetter.invokeExact(setMetadata);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke SetMetadata metaRange getter method handle", e);
        }
    }

    public static NBTTagCompound getTag(SetNBT setNbt)
    {
        try
        {
            return (NBTTagCompound) SetNBT$tagGetterGetter.invokeExact(setNbt);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke SetNBT tag getter method handle", e);
        }
    }
}
