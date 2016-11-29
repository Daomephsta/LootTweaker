package leviathan143.loottweaker.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;

public class LootUtils 
{
	public static final Gson LOOT_TABLE_GSON_INSTANCE = CommonMethodHandles.getLootTableGSON();
	
	//A regex that matches any vanilla pool name
	public static final Pattern DEFAULT_POOL_REGEX = Pattern.compile("(?:^(?:main$|pool[1-9]+)$)+");
	public static final LootCondition[] NO_CONDITIONS = new LootCondition[0];
	public static final LootEntry[] NO_ENTRIES = new LootEntry[0];
	public static final LootFunction[] NO_FUNCTIONS = new LootFunction[0];
	public static final LootPool[] NO_POOLS = new LootPool[0];
	//Used if the pool does not exist to prevent NPEs
	private static final LootPool EMPTY_LOOT_POOL = new LootPool(NO_ENTRIES, NO_CONDITIONS, new RandomValueRange(0), new RandomValueRange(0), "empty");

	//Tables

	public static ResourceLocation getBlockLootTableFromRegistryName(ResourceLocation registryName)
	{
		return new ResourceLocation(registryName.getResourceDomain(), "blocks/" + registryName.getResourcePath());
	}

	public static ResourceLocation getEntityLootTableFromName(String entityName)
	{
		Entity entity = EntityList.createEntityByName(entityName, LootTweakerMain.proxy.getWorld());
		if(entity == null) return null;
		if(!(entity instanceof EntityLiving)) return null;
		return CommonMethodHandles.getEntityLootTable((EntityLiving) entity);
	}
	
	public static void writeTableToJSON(ResourceLocation tableLoc, LootTableManager manager, File file)
	{
		writeTableToJSON(tableLoc, manager, file, false);
	}
	
	public static void writeTableToJSON(ResourceLocation tableLoc, LootTableManager manager, File file, boolean log)
	{
		writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager().getLootTableFromLocation(tableLoc), file, log);
	}

	public static void writeTableToJSON(ResourceLocation tableLoc, LootTable table, File file, boolean log)
	{
		try 
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(CommonMethodHandles.getLootTableGSON().toJson(table));
			writer.close();
			if (log) LootTweakerMain.logger.info(String.format("Loot table %s saved to %s", tableLoc, file.getCanonicalPath()));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
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
