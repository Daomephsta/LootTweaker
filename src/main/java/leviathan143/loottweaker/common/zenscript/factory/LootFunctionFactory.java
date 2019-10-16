package leviathan143.loottweaker.common.zenscript.factory;

import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import crafttweaker.mc1120.data.NBTConverter;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.IDataParser;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootFunctionWrapper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;
import net.minecraft.world.storage.loot.functions.EnchantWithLevels;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;
import net.minecraft.world.storage.loot.functions.Smelt;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".vanilla.loot.Functions")
public class LootFunctionFactory
{
    private static final LootCondition[] NO_CONDITIONS = new LootCondition[0];
    private static final IDataParser LOGGING_PARSER = new IDataParser(LootTableManagerAccessors.getGsonInstance(), e -> CraftTweakerAPI.logError(e.getMessage()));
    
	@ZenMethod
	public static ZenLootFunctionWrapper enchantRandomly(String[] enchantIDList)
	{
	    List<Enchantment> enchantments = Lists.newArrayListWithCapacity(enchantIDList.length);
        for (String id : enchantIDList)
        {
            Enchantment ench = Enchantment.getEnchantmentByLocation(id);
            if (ench == null)
            {
                CraftTweakerAPI.logError(String.format("%s is not a valid enchantment id", id));
                continue;
            }
            enchantments.add(ench);
        }
	    return new ZenLootFunctionWrapper(new EnchantRandomly(NO_CONDITIONS, enchantments));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper enchantWithLevels(int min, int max, boolean isTreasure)
	{
	    return new ZenLootFunctionWrapper(new EnchantWithLevels(NO_CONDITIONS, new RandomValueRange(min, max), isTreasure));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper lootingEnchantBonus(int min, int max, int limit)
	{
	    return new ZenLootFunctionWrapper(new LootingEnchantBonus(NO_CONDITIONS, new RandomValueRange(min, max), limit));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper setCount(int min, int max)
	{
	    return new ZenLootFunctionWrapper(new SetCount(NO_CONDITIONS, new RandomValueRange(min, max)));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper setDamage(float min, float max)
	{
	    if (max > 1.0F)
        {
            CraftTweakerAPI.logError("Items cannot recieve more than 100% damage!");
            return ZenLootFunctionWrapper.INVALID;
        }
	    return new ZenLootFunctionWrapper(new SetDamage(NO_CONDITIONS, new RandomValueRange(min, max)));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper setMetadata(int min, int max)
	{
	    return new ZenLootFunctionWrapper(new SetMetadata(NO_CONDITIONS, new RandomValueRange(min, max)));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper setNBT(IData nbtData)
	{
	    NBTBase nbt = NBTConverter.from(nbtData);
	    if (!(nbt instanceof NBTTagCompound))
	    {
	        CraftTweakerAPI.logError("Expected compound nbt tag, got " + nbtData);
	        return ZenLootFunctionWrapper.INVALID;
	    }
        return new ZenLootFunctionWrapper(new SetNBT(NO_CONDITIONS, (NBTTagCompound) nbt));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper smelt()
	{
	    return new ZenLootFunctionWrapper(new Smelt(NO_CONDITIONS));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper parse(IData json)
	{
	    return LOGGING_PARSER.parse(json, LootFunction.class).map(ZenLootFunctionWrapper::new).orElse(ZenLootFunctionWrapper.INVALID);
	}
}
