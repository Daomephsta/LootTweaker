package leviathan143.loottweaker.common.zenscript.factory;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import crafttweaker.api.data.IData;
import crafttweaker.mc1120.data.NBTConverter;
import leviathan143.loottweaker.common.lib.Arguments;
import leviathan143.loottweaker.common.lib.LootConditions;
import leviathan143.loottweaker.common.lib.RandomValueRanges;
import leviathan143.loottweaker.common.zenscript.JsonMapConversions;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootFunctionWrapper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.functions.*;


public class LootFunctionFactoryImpl
{
    private final LootTweakerContext context;
    private final JsonMapConversions.Impl jsonMapConversions;

    public LootFunctionFactoryImpl(LootTweakerContext context)
    {
        this.context = context;
        this.jsonMapConversions = new JsonMapConversions.Impl(context);
    }

    public ZenLootFunctionWrapper enchantRandomly(String[] enchantIDList)
    {
        if (!Arguments.nonNull(context.getErrorHandler(), "enchantment IDs", enchantIDList))
            return ZenLootFunctionWrapper.INVALID;
        List<Enchantment> enchantments = Lists.newArrayListWithCapacity(enchantIDList.length);
        for (String id : enchantIDList)
        {
            Enchantment ench = Enchantment.getEnchantmentByLocation(id);
            if (ench == null)
            {
                context.getErrorHandler().error("%s is not a valid enchantment id", id);
                continue;
            }
            enchantments.add(ench);
        }
        return new ZenLootFunctionWrapper(new EnchantRandomly(LootConditions.NONE, enchantments), context);
    }

    public ZenLootFunctionWrapper enchantWithLevels(int min, int max, boolean isTreasure)
    {
        return new ZenLootFunctionWrapper(new EnchantWithLevels(LootConditions.NONE,
            RandomValueRanges.checked(context.getErrorHandler(), min, max), isTreasure), context);
    }

    public ZenLootFunctionWrapper lootingEnchantBonus(int min, int max, int limit)
    {
        return new ZenLootFunctionWrapper(new LootingEnchantBonus(LootConditions.NONE,
            RandomValueRanges.checked(context.getErrorHandler(), min, max), limit), context);
    }

    public ZenLootFunctionWrapper setCount(int min, int max)
    {
        return new ZenLootFunctionWrapper(
            new SetCount(LootConditions.NONE, RandomValueRanges.checked(context.getErrorHandler(), min, max)),
            context);
    }

    public ZenLootFunctionWrapper setDamage(float min, float max)
    {
        if (max > 1.0F)
        {
            context.getErrorHandler().error("Items cannot recieve more than 100% damage!");
            return ZenLootFunctionWrapper.INVALID;
        }
        return new ZenLootFunctionWrapper(
            new SetDamage(LootConditions.NONE, RandomValueRanges.checked(context.getErrorHandler(), min, max)),
            context);
    }

    public ZenLootFunctionWrapper setMetadata(int min, int max)
    {
        return new ZenLootFunctionWrapper(
            new SetMetadata(LootConditions.NONE, RandomValueRanges.checked(context.getErrorHandler(), min, max)),
            context);
    }

    public ZenLootFunctionWrapper setNBT(IData nbtData)
    {
        NBTBase nbt = NBTConverter.from(nbtData);
        if (!(nbt instanceof NBTTagCompound))
        {
            context.getErrorHandler().error("Expected compound nbt tag, got %s", nbtData);
            return ZenLootFunctionWrapper.INVALID;
        }
        return new ZenLootFunctionWrapper(new SetNBT(LootConditions.NONE, (NBTTagCompound) nbt), context);
    }

    public ZenLootFunctionWrapper smelt()
    {
        return new ZenLootFunctionWrapper(new Smelt(LootConditions.NONE), context);
    }

    public ZenLootFunctionWrapper parse(Map<String, ?> json)
    {
        return jsonMapConversions.asLootFunction(json);
    }

    public ZenLootFunctionWrapper zenscript(ZenLambdaLootFunction.Delegate delegate)
    {
        return new ZenLootFunctionWrapper(new ZenLambdaLootFunction(delegate, LootConditions.NONE), context);
    }
}
