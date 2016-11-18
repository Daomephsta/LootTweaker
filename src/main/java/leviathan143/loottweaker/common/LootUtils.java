package leviathan143.loottweaker.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import minetweaker.MineTweakerAPI;
import minetweaker.api.data.DataMap;
import minetweaker.api.data.IData;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;

public class LootUtils 
{
	public static final Gson LOOT_TABLE_GSON_INSTANCE = CommonMethodHandles.getLootConditionGSON();


	//A regex that matches any vanilla pool name
	public static final Pattern DEFAULT_POOL_REGEX = Pattern.compile("(?:^(?:main$|pool[1-9]+)$)+");
	public static final LootCondition[] NO_CONDITIONS = new LootCondition[0];
	public static final LootEntry[] NO_ENTRIES = new LootEntry[0];
	public static final LootFunction[] NO_FUNCTIONS = new LootFunction[0];
	public static final LootPool[] NO_POOLS = new LootPool[0];
	//Used if the pool does not exist to prevent NPEs
	private static final LootPool EMPTY_LOOT_POOL = new LootPool(NO_ENTRIES, NO_CONDITIONS, new RandomValueRange(0), new RandomValueRange(0), "empty");

	//Tables

	public static LootTable getTable(ResourceLocation tableLoc)
	{
		LootTable table = LootTweakerMain.proxy.getWorld().getLootTableManager().getLootTableFromLocation(tableLoc);
		if(table == null)
		{
			MineTweakerAPI.logWarning(String.format("No loot table with name %s exists!", tableLoc));
			//Returned to prevent NPEs
			return LootTable.EMPTY_LOOT_TABLE;
		}
		return table;
	}

	//Pools

	public static LootPool getPool(LootTable table, String poolName)
	{
		LootPool pool = table.getPool(poolName);
		if(pool == null)
		{
			MineTweakerAPI.logWarning(String.format("No loot pool with name %s exists!", poolName));
			//Returned to prevent NPEs
			return EMPTY_LOOT_POOL;
		}
		return pool;
	}

	/**
	 * @param name - the name of the loot pool
	 * @return a temporary loot pool with that name
	 */
	public static LootPool createTemporaryPool(String name)
	{
		return createPool(name, 0, 0, 0, 0);
	}

	public static LootPool createPool(String name, int minRolls, int maxRolls, int minBonusRolls, int maxBonusRolls)
	{
		return new LootPool(NO_ENTRIES, NO_CONDITIONS, new RandomValueRange(minRolls, maxRolls), new RandomValueRange(minBonusRolls, maxBonusRolls), name);
	}

	public static void addConditionsToPool(LootPool pool, LootCondition... newConditions)
	{
		Collections.addAll(CommonMethodHandles.getConditionsFromPool(pool), newConditions);
	}

	//Conditions

	public static LootCondition[] parseConditions(String[] conditions)
	{
		if(conditions == null) return NO_CONDITIONS;
		LootCondition[] parsedConditions = new LootCondition[conditions.length];
		for(int c = 0; c < conditions.length; c++)
		{
			parsedConditions[c] = parseCondition("{" + conditions[c] + "}");
		}
		return parsedConditions;
	}

	private static LootCondition parseCondition(String condition)
	{
		return LOOT_TABLE_GSON_INSTANCE.fromJson(condition, LootCondition.class);
	}

	//Functions

	/*
	 * Returns an array of loot functions equivalent to the damage, stacksize and NBT of the input stack. 
	 */
	public static LootFunction[] translateStackToFunctions(IItemStack iStack)
	{
		ItemStack stack = MineTweakerMC.getItemStack(iStack);
		List<LootFunction> retList = new ArrayList<LootFunction>();
		if(iStack.getAmount() > 1)
		{
			retList.add(new SetCount(NO_CONDITIONS, new RandomValueRange(iStack.getAmount())));
		}
		if(iStack.getDamage() > 0)
		{
			if(stack.isItemStackDamageable())
			{
				//SetDamage takes a percentage, not a number
				retList.add(new SetDamage(NO_CONDITIONS, new RandomValueRange((float)stack.getItemDamage() / (float)stack.getMaxDamage())));
			}
			else
			{
				retList.add(new SetMetadata(NO_CONDITIONS, new RandomValueRange(iStack.getDamage())));
			}
		}
		IData stackData = iStack.getTag();
		if(stackData != DataMap.EMPTY)
		{
			retList.add(new SetNBT(NO_CONDITIONS, MineTweakerMC.getNBTCompound(stackData)));
		}
		return retList.toArray(new LootFunction[0]);
	}

	public static LootFunction[] parseFunctions(String[] functions) 
	{
		if(functions == null) return NO_FUNCTIONS;
		LootFunction[] parsedFunctions = new LootFunction[functions.length];
		for(int c = 0; c < functions.length; c++)
		{
			parsedFunctions[c] = parseFunction("{" + functions[c] + "}");
		}
		return parsedFunctions;
	}

	private static LootFunction parseFunction(String function) 
	{
		return LOOT_TABLE_GSON_INSTANCE.fromJson(function, LootFunction.class);
	}
}
