package io.github.daomephsta.loottweaker.test;

import java.lang.invoke.MethodHandle;
import java.util.List;

import leviathan143.loottweaker.common.accessors.AbstractAccessors;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.*;


public class TestLootFunctionAccessors extends AbstractAccessors
{
    private static final MethodHandle SetCount$countRangeGetter, SetDamage$damageRangeGetter,
        SetMetadata$metaRangeGetter, SetNBT$tagGetterGetter, EnchantRandomly$enchantmentsGetter,
        EnchantWithLevels$randomLevelGetter, EnchantWithLevels$isTreasureGetter, LootingEnchantBonus$countGetter,
        LootingEnchantBonus$limitGetter;
    static
    {
        try
        {
            SetCount$countRangeGetter = createFieldGetter(SetCount.class, "countRange");
            SetDamage$damageRangeGetter = createFieldGetter(SetDamage.class, "damageRange");
            SetMetadata$metaRangeGetter = createFieldGetter(SetMetadata.class, "metaRange");
            SetNBT$tagGetterGetter = createFieldGetter(SetNBT.class, "tag");
            EnchantRandomly$enchantmentsGetter = createFieldGetter(EnchantRandomly.class, "enchantments");
            EnchantWithLevels$randomLevelGetter = createFieldGetter(EnchantWithLevels.class, "randomLevel");
            EnchantWithLevels$isTreasureGetter = createFieldGetter(EnchantWithLevels.class, "isTreasure");
            LootingEnchantBonus$countGetter = createFieldGetter(LootingEnchantBonus.class, "count");
            LootingEnchantBonus$limitGetter = createFieldGetter(LootingEnchantBonus.class, "limit");
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

    public static List<Enchantment> getEnchantments(EnchantRandomly enchantRandomly)
    {
        try
        {
            return (List<Enchantment>) EnchantRandomly$enchantmentsGetter.invokeExact(enchantRandomly);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke EnchantRandomly enchantments getter method handle", e);
        }
    }

    public static RandomValueRange getLevelRange(EnchantWithLevels enchantWithLevels)
    {
        try
        {
            return (RandomValueRange) EnchantWithLevels$randomLevelGetter.invokeExact(enchantWithLevels);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke EnchantWithLevels randomLevel getter method handle", e);
        }
    }

    public static boolean isTreasure(EnchantWithLevels enchantWithLevels)
    {
        try
        {
            return (boolean) EnchantWithLevels$isTreasureGetter.invokeExact(enchantWithLevels);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke EnchantWithLevels isTreasure getter method handle", e);
        }
    }

    public static RandomValueRange getBonusRange(LootingEnchantBonus lootingEnchantBonus)
    {
        try
        {
            return (RandomValueRange) LootingEnchantBonus$countGetter.invokeExact(lootingEnchantBonus);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootingEnchantBonus count getter method handle", e);
        }
    }

    public static int getLimit(LootingEnchantBonus lootingEnchantBonus)
    {
        try
        {
            return (int) LootingEnchantBonus$limitGetter.invokeExact(lootingEnchantBonus);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootingEnchantBonus limit getter method handle", e);
        }
    }
}
