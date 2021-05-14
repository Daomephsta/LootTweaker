package leviathan143.loottweaker.common.zenscript.impl.factory;

import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.api.data.IData;
import crafttweaker.mc1120.data.NBTConverter;
import leviathan143.loottweaker.common.lib.LootConditions;
import leviathan143.loottweaker.common.zenscript.api.entry.LootFunctionRepresentation;
import leviathan143.loottweaker.common.zenscript.api.factory.LootFunctionFactory;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.impl.entry.LootFunctionWrapper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.*;

public class VanillaLootFunctionFactory implements LootFunctionFactory
{
    private final LootTweakerContext context;

    public VanillaLootFunctionFactory(LootTweakerContext context)
    {
        this.context = context;
    }

    @Override
    public LootFunctionRepresentation enchantRandomly(String[] enchantIDList)
    {
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
        return new LootFunctionWrapper(new EnchantRandomly(LootConditions.NONE, enchantments));
    }

    @Override
    public LootFunctionRepresentation enchantWithLevels(int min, int max, boolean isTreasure)
    {
        return new LootFunctionWrapper(new EnchantWithLevels(LootConditions.NONE, new RandomValueRange(min, max), isTreasure));
    }

    @Override
    public LootFunctionRepresentation lootingEnchantBonus(int min, int max, int limit)
    {
        return new LootFunctionWrapper(new LootingEnchantBonus(LootConditions.NONE, new RandomValueRange(min, max), limit));
    }

    @Override
    public LootFunctionRepresentation setCount(int min, int max)
    {
        return new LootFunctionWrapper(new SetCount(LootConditions.NONE, new RandomValueRange(min, max)));
    }

    @Override
    public LootFunctionRepresentation setDamage(float min, float max)
    {
        if (max > 1.0F)
        {
            context.getErrorHandler().error("Items cannot recieve more than 100% damage!");
            return LootFunctionWrapper.INVALID;
        }
        return new LootFunctionWrapper(new SetDamage(LootConditions.NONE, new RandomValueRange(min, max)));
    }

    @Override
    public LootFunctionRepresentation setMetadata(int min, int max)
    {
        return new LootFunctionWrapper(new SetMetadata(LootConditions.NONE, new RandomValueRange(min, max)));
    }

    @Override
    public LootFunctionRepresentation setNBT(IData nbtData)
    {
        NBTBase nbt = NBTConverter.from(nbtData);
        if (!(nbt instanceof NBTTagCompound))
        {
            context.getErrorHandler().error("Expected compound nbt tag, got %s", nbtData);
            return LootFunctionWrapper.INVALID;
        }
        return new LootFunctionWrapper(new SetNBT(LootConditions.NONE, (NBTTagCompound) nbt));
    }

    @Override
    public LootFunctionRepresentation smelt()
    {
        return new LootFunctionWrapper(new Smelt(LootConditions.NONE));
    }
}
