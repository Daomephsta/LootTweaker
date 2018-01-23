package leviathan143.loottweaker.common.zenscript;

import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import crafttweaker.mc1120.data.NBTConverter;
import leviathan143.loottweaker.common.DeprecationWarningManager;
import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import leviathan143.loottweaker.common.lib.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.*;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.*;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
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
			if (ench == null)
			{
				CraftTweakerAPI.logError(id + " is not a valid enchantment id");
				continue;
			}
			enchantments.add(ench);
		}
		return new ZenLootFunctionWrapper(new EnchantRandomly(LootUtils.NO_CONDITIONS, enchantments));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper enchantWithLevels(int min, int max, boolean isTreasure)
	{
		return new ZenLootFunctionWrapper(
				new EnchantWithLevels(LootUtils.NO_CONDITIONS, new RandomValueRange(min, max), isTreasure));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper lootingEnchantBonus(int min, int max, int limit)
	{
		return new ZenLootFunctionWrapper(
				new LootingEnchantBonus(LootUtils.NO_CONDITIONS, new RandomValueRange(min, max), limit));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper setCount(int min, int max)
	{
		return new ZenLootFunctionWrapper(new SetCount(LootUtils.NO_CONDITIONS, new RandomValueRange(min, max)));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper setDamage(float min, float max)
	{
		if (max > 1.0F)
		{
			CraftTweakerAPI.logError("Items cannot recieve more than 100% damage!");
			max = 1.0F;
		}
		return new ZenLootFunctionWrapper(new SetDamage(LootUtils.NO_CONDITIONS, new RandomValueRange(min, max)));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper setMetadata(int min, int max)
	{
		return new ZenLootFunctionWrapper(new SetMetadata(LootUtils.NO_CONDITIONS, new RandomValueRange(min, max)));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper setNBT(IData nbtData)
	{
		if(!ZenScriptUtils.checkIsMap(nbtData)) return null;
		return new ZenLootFunctionWrapper(new SetNBT(LootUtils.NO_CONDITIONS, (NBTTagCompound) NBTConverter.from(nbtData)));
	}

	@ZenMethod
	@Deprecated
	public static ZenLootFunctionWrapper setNBT(String nbtAsJson) throws NBTException
	{
		DeprecationWarningManager.addWarning();
		return new ZenLootFunctionWrapper(new SetNBT(LootUtils.NO_CONDITIONS, JsonToNBT.getTagFromJson(nbtAsJson)));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper smelt()
	{
		return new ZenLootFunctionWrapper(new Smelt(LootUtils.NO_CONDITIONS));
	}

	@ZenMethod
	public static ZenLootFunctionWrapper parse(IData json)
	{
		if(!ZenScriptUtils.checkIsMap(json)) return null;
		return new ZenLootFunctionWrapper(LootUtils.parseJSONFunction(DataToJSONConverter.from(json)));
	}

	@ZenMethod
	@Deprecated
	public static ZenLootFunctionWrapper parse(String json)
	{
		DeprecationWarningManager.addWarning();
		return new ZenLootFunctionWrapper(LootUtils.parseJSONFunction("{" + json + "}"));
	}
}
