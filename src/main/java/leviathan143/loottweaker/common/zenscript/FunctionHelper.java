package leviathan143.loottweaker.common.zenscript;

import java.util.List;

import com.google.common.collect.Lists;

import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import leviathan143.loottweaker.common.lib.LootUtils;
import minetweaker.MineTweakerImplementationAPI;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.*;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.*;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(Constants.MODID + ".vanilla.loot.Functions")
public class FunctionHelper
{
    @ZenMethod
    public static ZenLootFunctionWrapper enchantRandomly(String[] enchantIDList)
    {
	List<Enchantment> enchantments = Lists.newArrayListWithCapacity(enchantIDList.length);
	for (String id : enchantIDList)
	{
	    Enchantment ench = Enchantment.getEnchantmentByLocation(id);
	    if(ench == null)
	    {
		MineTweakerImplementationAPI.logger.logError(id + " is not a valid enchantment id");
		continue;
	    }
	    enchantments.add(ench);
	}
	return new ZenLootFunctionWrapper(new EnchantRandomly(LootUtils.NO_CONDITIONS, enchantments));
    }
    
    @ZenMethod
    public static ZenLootFunctionWrapper enchantWithLevels(int min, int max, boolean isTreasure)
    {
	return new ZenLootFunctionWrapper(new EnchantWithLevels(LootUtils.NO_CONDITIONS, new RandomValueRange(min, max), isTreasure));
    }
    
    @ZenMethod
    public static ZenLootFunctionWrapper lootingEnchantBonus(int min, int max, int limit)
    {
	return new ZenLootFunctionWrapper(new LootingEnchantBonus(LootUtils.NO_CONDITIONS, new RandomValueRange(min, max), limit));
    }
    
    @ZenMethod
    public static ZenLootFunctionWrapper setCount(int min, int max)
    {
	return new ZenLootFunctionWrapper(new SetCount(LootUtils.NO_CONDITIONS, new RandomValueRange(min, max)));
    }
    
    @ZenMethod
    public static ZenLootFunctionWrapper setDamage(int min, int max)
    {
	return new ZenLootFunctionWrapper(new SetDamage(LootUtils.NO_CONDITIONS, new RandomValueRange(min, max)));
    }
    
    @ZenMethod
    public static ZenLootFunctionWrapper setMetadata(int min, int max)
    {
	return new ZenLootFunctionWrapper(new SetMetadata(LootUtils.NO_CONDITIONS, new RandomValueRange(min, max)));
    }
    
    @ZenMethod
    public static ZenLootFunctionWrapper setNBT(String nbtAsJson)
    {
	try
	{
	    return new ZenLootFunctionWrapper(new SetNBT(LootUtils.NO_CONDITIONS, JsonToNBT.getTagFromJson(nbtAsJson)));
	}
	catch (NBTException e)
	{
	    e.printStackTrace();
	}
	return new ZenLootFunctionWrapper(new SetNBT(LootUtils.NO_CONDITIONS, new NBTTagCompound()));
    }
    
    @ZenMethod
    public static ZenLootFunctionWrapper smelt()
    {
	return new ZenLootFunctionWrapper(new Smelt(LootUtils.NO_CONDITIONS));
    }
}
