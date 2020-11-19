package leviathan143.loottweaker.common.zenscript.impl.entry;

import crafttweaker.api.data.IData;
import crafttweaker.api.minecraft.CraftTweakerMC;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.LTConfig;
import leviathan143.loottweaker.common.darkmagic.LootFunctionAccessors;
import leviathan143.loottweaker.common.lib.Describable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.*;

public class FunctionEffects
{
    private Item item;
    private int minMeta = 0,
                maxMeta = 0;
    private float minDamagePercent = 0.0F,
                  maxDamagePercent = 0.0F;
    private int minDamageAmount = 0,
                maxDamageAmount = 0;
    private IData nbt = null;
    private boolean initalised = false;

    public FunctionEffects(Item item)
    {
        this.item = item;
    }

    private static boolean checkConditional(LootFunction function, ErrorHandler errorHandler, String format)
    {
        if (function.getConditions().length > 0)
        {
            packdevWarn(errorHandler, format);
            return true;
        }
        return false;
    }

    public FunctionEffects initialise(Describable describable, Iterable<LootFunction> functions, ErrorHandler errorHandler)
    {
        if (initalised)
            return this;

        NBTTagCompound nbt = null;
        boolean metaFound = false,
                damageFound = false;
        boolean nonVanillaFunctions = false;

        for (LootFunction function : functions)
        {
            if (!function.getClass().getName().startsWith("net.minecraft"))
            {
                nonVanillaFunctions = true;
                continue;
            }
            if (function instanceof SetMetadata)
            {
                if (metaFound)
                {
                    this.minMeta = this.maxMeta = -1;
                    packdevWarn(errorHandler, "%s has multiple 'set_data' functions."
                        + " Cannot compute metadata.", describable.describe());
                    continue;
                }
                if (checkConditional(function, errorHandler, describable.describe() +
                    " has a conditional 'set_data' function. Cannot compute metadata."))
                {
                    continue;
                }
                RandomValueRange range = LootFunctionAccessors.getMetaRange((SetMetadata) function);
                this.setMeta(MathHelper.floor(range.getMin()), MathHelper.floor(range.getMax()));
                metaFound = true;
            }
            else if (function instanceof SetDamage)
            {
                if (damageFound)
                {
                    this.minDamagePercent = this.maxDamagePercent = -1.0F;
                    packdevWarn(errorHandler, "%s has multiple 'set_damage' functions."
                        + " Cannot compute damage.", describable.describe());
                    continue;
                }
                if (checkConditional(function, errorHandler, describable.describe() +
                    " has a conditional 'set_damage' function. Cannot compute damage."))
                {
                    continue;
                }
                RandomValueRange range = LootFunctionAccessors.getDamageRange((SetDamage) function);
                this.setDamagePercentRange(range.getMin(), range.getMax());
                damageFound = true;
            }
            else if (function instanceof SetNBT)
            {
                if (checkConditional(function, errorHandler, describable.describe() +
                    " has a conditional 'set_nbt' function. Cannot compute NBT."))
                {
                    continue;
                }
                NBTTagCompound tag = LootFunctionAccessors.getTag((SetNBT) function);
                if (nbt == null)
                    nbt = tag;
                else
                    nbt.merge(tag);
            }
            else if (function instanceof Smelt)
            {
                if (checkConditional(function, errorHandler, describable.describe() +
                    " has a conditional 'smelt' function. Cannot compute NBT, damage/metadata, or item."))
                {
                    this.setItem(Items.AIR);
                    this.setMeta(-1, -1);
                    this.setDamagePercentRange(-1.0F, -1.0F);
                    continue;
                }

                ItemStack input = null;
                if (metaFound && this.minMeta == this.maxMeta)
                    input = new ItemStack(item, 1, minMeta);
                else if (damageFound && this.minDamageAmount == this.maxDamageAmount)
                    input = new ItemStack(item, 1, minDamageAmount);
                else if (!damageFound && !metaFound)
                    input = new ItemStack(item, 1, 0);

                if (input != null)
                {
                    if (nbt != null)
                        input.setTagCompound(nbt);
                    ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
                    this.setItem(result.getItem());
                    if (result.isItemStackDamageable())
                    {
                        float damage = 1.0F - (float) result.getItemDamage() / result.getMaxDamage();
                        this.setDamagePercentRange(damage, damage);
                        this.setMeta(-1, -1);
                    }
                    else
                    {
                        this.setMeta(result.getMetadata(), result.getMetadata());
                        this.setDamagePercentRange(-1.0F, -1.0F);
                    }
                }
                else
                {
                    packdevWarn(errorHandler, "%s has a 'smelt' function, but ranged damage/metadata. "
                        + "Cannot compute NBT, damage/metadata, or item.", describable.describe());
                    this.setItem(Items.AIR);
                    this.setMeta(-1, -1);
                    this.setDamagePercentRange(-1.0F, -1.0F);
                }
            }
        }

        if (nonVanillaFunctions)
            packdevWarn(errorHandler, "%s has non-vanilla functions. Computed NBT, damage, and/or metadata may be inaccurate.", describable.describe());

        if (nbt != null)
            this.nbt = CraftTweakerMC.getIData(nbt);

        initalised = true;
        return this;
    }

    private static void packdevWarn(ErrorHandler errorHandler, String format, Object... args)
    {
        if (LTConfig.packdevMode)
            errorHandler.warn(format, args);
    }

    public Item getItem()
    {
        return item;
    }

    public void setItem(Item item)
    {
        this.item = item;
        recalculateDamageAmounts();
    }

    public int getMinMeta()
    {
        return minMeta;
    }

    public int getMaxMeta()
    {
        return maxMeta;
    }

    public void setMeta(int min, int max)
    {
        this.minMeta = min;
        this.maxMeta = max;
    }

    public float getMinDamagePercent()
    {
        return minDamagePercent;
    }

    public float getMaxDamagePercent()
    {
        return maxDamagePercent;
    }

    public void setDamagePercentRange(float min, float max)
    {
        this.minDamagePercent = min;
        this.maxDamagePercent = max;
        recalculateDamageAmounts();
    }

    private void recalculateDamageAmounts()
    {
        @SuppressWarnings("deprecation")
        int maxDamage = item.getMaxDamage();
        this.minDamageAmount = minDamagePercent > 0
            ? MathHelper.floor((1.0F - minDamagePercent) * maxDamage)
            : -1;
        this.maxDamageAmount = maxDamagePercent > 0
            ? MathHelper.floor((1.0F - maxDamagePercent) * maxDamage)
            : -1;
    }

    public int getMinDamageAmount()
    {
        return minDamageAmount;
    }

    public int getMaxDamageAmount()
    {
        return maxDamageAmount;
    }

    public IData getNbt()
    {
        return nbt;
    }
}
