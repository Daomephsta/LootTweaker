package leviathan143.loottweaker.common.zenscript.factory;

import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.api.data.IData;
import crafttweaker.mc1120.data.NBTConverter;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.DataParser;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootFunctionWrapper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.*;

public class LootFunctionFactoryImpl
{
    private static final LootCondition[] NO_CONDITIONS = new LootCondition[0];
    private final ErrorHandler errorHandler;
    private final DataParser loggingParser;

    public LootFunctionFactoryImpl(ErrorHandler errorHandler)
    {
        this.errorHandler = errorHandler;
        this.loggingParser = new DataParser(LootTableManagerAccessors.getGsonInstance(), e -> errorHandler.error(e.getMessage()));
    }

    public ZenLootFunctionWrapper enchantRandomly(String[] enchantIDList)
    {
        List<Enchantment> enchantments = Lists.newArrayListWithCapacity(enchantIDList.length);
        for (String id : enchantIDList)
        {
            Enchantment ench = Enchantment.getEnchantmentByLocation(id);
            if (ench == null)
            {
                errorHandler.error("%s is not a valid enchantment id", id);
                continue;
            }
            enchantments.add(ench);
        }
        return new ZenLootFunctionWrapper(new EnchantRandomly(NO_CONDITIONS, enchantments));
    }

    public ZenLootFunctionWrapper enchantWithLevels(int min, int max, boolean isTreasure)
    {
        return new ZenLootFunctionWrapper(new EnchantWithLevels(NO_CONDITIONS, new RandomValueRange(min, max), isTreasure));
    }

    public ZenLootFunctionWrapper lootingEnchantBonus(int min, int max, int limit)
    {
        return new ZenLootFunctionWrapper(new LootingEnchantBonus(NO_CONDITIONS, new RandomValueRange(min, max), limit));
    }

    public ZenLootFunctionWrapper setCount(int min, int max)
    {
        return new ZenLootFunctionWrapper(new SetCount(NO_CONDITIONS, new RandomValueRange(min, max)));
    }

    public ZenLootFunctionWrapper setDamage(float min, float max)
    {
        if (max > 1.0F)
        {
            errorHandler.error("Items cannot recieve more than 100%% damage!");
            return ZenLootFunctionWrapper.INVALID;
        }
        return new ZenLootFunctionWrapper(new SetDamage(NO_CONDITIONS, new RandomValueRange(min, max)));
    }

    public ZenLootFunctionWrapper setMetadata(int min, int max)
    {
        return new ZenLootFunctionWrapper(new SetMetadata(NO_CONDITIONS, new RandomValueRange(min, max)));
    }

    public ZenLootFunctionWrapper setNBT(IData nbtData)
    {
        NBTBase nbt = NBTConverter.from(nbtData);
        if (!(nbt instanceof NBTTagCompound))
        {
            errorHandler.error("Expected compound nbt tag, got %s", nbtData);
            return ZenLootFunctionWrapper.INVALID;
        }
        return new ZenLootFunctionWrapper(new SetNBT(NO_CONDITIONS, (NBTTagCompound) nbt));
    }

    public ZenLootFunctionWrapper smelt()
    {
        return new ZenLootFunctionWrapper(new Smelt(NO_CONDITIONS));
    }

    public ZenLootFunctionWrapper parse(IData json)
    {
        return loggingParser.parse(json, LootFunction.class).map(ZenLootFunctionWrapper::new).orElse(ZenLootFunctionWrapper.INVALID);
    }
}
