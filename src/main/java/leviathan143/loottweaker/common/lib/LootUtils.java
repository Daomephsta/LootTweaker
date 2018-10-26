package leviathan143.loottweaker.common.lib;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import com.google.gson.*;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.IData;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import leviathan143.loottweaker.common.zenscript.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.*;

public class LootUtils
{
	private static final Gson PRETTY_PRINTER = new GsonBuilder().setPrettyPrinting().create();
	private static final JsonParser parser = new JsonParser();

	// A regex that matches any vanilla pool name
	public static final Pattern DEFAULT_POOL_REGEX = Pattern.compile("(?:^(?:main$|pool[1-9]+)$)+");
	public static final LootCondition[] NO_CONDITIONS = new LootCondition[0];
	public static final LootEntry[] NO_ENTRIES = new LootEntry[0];
	public static final LootFunction[] NO_FUNCTIONS = new LootFunction[0];
	public static final LootPool[] NO_POOLS = new LootPool[0];
	// Used if the pool does not exist to prevent NPEs
	public static final ZenLootPoolWrapper EMPTY_LOOT_POOL = new ZenLootPoolWrapper(
			new LootPool(NO_ENTRIES, NO_CONDITIONS, new RandomValueRange(0), new RandomValueRange(0), "empty"));
	public static final ZenLootTableWrapper EMPTY_LOOT_TABLE = new ZenLootTableWrapper(new LootTable(NO_POOLS),
			new ResourceLocation(LootTweakerMain.MODID, "empty"));

	// Tables

	public static ResourceLocation getEntityLootTableFromName(ResourceLocation entityName)
	{
		Entity entity = EntityList.createEntityByIDFromName(entityName, LootTweakerMain.proxy.getWorld());
		if (entity == null) return null;
		if (!(entity instanceof EntityLiving)) return null;
		return CommonMethodHandles.getEntityLootTable((EntityLiving) entity);
	}

	public static void writeTableToJSON(ResourceLocation tableLoc, LootTableManager manager, File file)
	{
		writeTableToJSON(tableLoc, manager, file, false);
	}

	public static void writeTableToJSON(ResourceLocation tableLoc, LootTableManager manager, File file, boolean log)
	{
		World world = LootTweakerMain.proxy.getWorld();
		if (world.isRemote) return;
		LootTable table = manager.getLootTableFromLocation(tableLoc);
		writeTableToJSON(tableLoc, table, file, log);
	}

	public static void writeTableToJSON(ResourceLocation tableLoc, LootTable table, File file, boolean log)
	{
		try
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			try
			{
				writer.write(prettify(CommonMethodHandles.getLootTableGSON().toJson(table)));
			}
			catch (Throwable t)
			{
				LootTweakerMain.logger.warn("Failed to dump loot table %s", tableLoc.toString());
				t.printStackTrace();
			}
			writer.close();
			if (log)
				LootTweakerMain.logger.info(String.format("Loot table %s saved to %s", tableLoc, file.getCanonicalPath()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	static String prettify(String jsonBarf)
	{
		return PRETTY_PRINTER.toJson(parser.parse(jsonBarf));
	}

	// Pools

	/**
	 * @param name
	 *            - the name of the loot pool
	 * @return a temporary loot pool with that name
	 */
	public static LootPool createTemporaryPool(String name)
	{
		return createPool(name, 0, 0, 0, 0);
	}

	public static LootPool createPool(String name, int minRolls, int maxRolls, int minBonusRolls, int maxBonusRolls)
	{
		return new LootPool(NO_ENTRIES, NO_CONDITIONS, new RandomValueRange(minRolls, maxRolls),
				new RandomValueRange(minBonusRolls, maxBonusRolls), name);
	}

	public static void addConditionsToPool(LootPool pool, LootCondition... newConditions)
	{
		Collections.addAll(CommonMethodHandles.getConditionsFromPool(pool), newConditions);
	}

	// Conditions

	public static LootCondition[] parseConditions(Object[] conditions)
	{
		if (conditions == null) return NO_CONDITIONS;
		LootCondition[] parsedConditions = new LootCondition[conditions.length];
		for (int c = 0; c < conditions.length; c++)
		{
			if (conditions[c] instanceof JsonElement)
				parsedConditions[c] = parseJSONCondition((JsonElement) conditions[c]);
			else if (conditions[c] instanceof String)
				parsedConditions[c] = parseJSONCondition("{" + conditions[c] + "}");
			else if (conditions[c] instanceof ZenLootConditionWrapper)
				parsedConditions[c] = ((ZenLootConditionWrapper) conditions[c]).condition;
			else
				CraftTweakerAPI.logError(I18n.format(LootTweakerMain.MODID + ".messages.error.unparseableType", conditions[c]));
		}
		return parsedConditions;
	}

	public static LootCondition parseJSONCondition(JsonElement conditionJSON)
	{
		return CommonMethodHandles.getLootTableGSON().fromJson(conditionJSON, LootCondition.class);
	}

	@Deprecated
	public static LootCondition parseJSONCondition(String conditionJSON)
	{
		return CommonMethodHandles.getLootTableGSON().fromJson(conditionJSON, LootCondition.class);
	}

	// Functions

	/* Adds loot functions equivalent to the damage, stacksize and NBT of the
	 * input stack to the passed in array, if loot functions of the same type
	 * are not present. */
	public static LootFunction[] addStackFunctions(IItemStack iStack, LootFunction[] existingFunctions)
	{
		ItemStack stack = CraftTweakerMC.getItemStack(iStack);
		boolean sizeFuncExists = false, damageFuncExists = false, nbtFuncExists = false;
		for (LootFunction lootFunction : existingFunctions)
		{
			if (lootFunction instanceof SetCount) sizeFuncExists = true;
			if (lootFunction instanceof SetDamage || lootFunction instanceof SetMetadata) damageFuncExists = true;
			if (lootFunction instanceof SetNBT) nbtFuncExists = true;
		}
		int capacityRequired = existingFunctions.length + (sizeFuncExists ? 0 : 1) + (damageFuncExists ? 0 : 1)
				+ (nbtFuncExists ? 0 : 1);
		List<LootFunction> retList = Lists.newArrayListWithCapacity(capacityRequired);
		Collections.addAll(retList, existingFunctions);
		if (iStack.getAmount() > 1 && !sizeFuncExists)
		{
			retList.add(new SetCount(NO_CONDITIONS, new RandomValueRange(iStack.getAmount())));
		}
		if (iStack.getDamage() > 0 && !damageFuncExists)
		{
			if (stack.isItemStackDamageable())
			{
				// SetDamage takes a percentage, not a number
				retList.add(new SetDamage(NO_CONDITIONS,
						new RandomValueRange((float) stack.getItemDamage() / (float) stack.getMaxDamage())));
			}
			else
			{
				retList.add(new SetMetadata(NO_CONDITIONS, new RandomValueRange(iStack.getDamage())));
			}
		}
		IData stackData = iStack.getTag();
		if (stackData != DataMap.EMPTY && !nbtFuncExists)
		{
			retList.add(new SetNBT(NO_CONDITIONS, CraftTweakerMC.getNBTCompound(stackData)));
		}

		return retList.toArray(LootUtils.NO_FUNCTIONS);
	}

	public static LootFunction[] parseFunctions(Object[] functions)
	{
		if (functions == null) return NO_FUNCTIONS;
		LootFunction[] parsedFunctions = new LootFunction[functions.length];
		for (int f = 0; f < functions.length; f++)
		{
			if (functions[f] instanceof JsonElement) parsedFunctions[f] = parseJSONFunction((JsonElement) functions[f]);
			else if (functions[f] instanceof String) parsedFunctions[f] = parseJSONFunction("{" + functions[f] + "}");
			else if (functions[f] instanceof ZenLootFunctionWrapper)
				parsedFunctions[f] = ((ZenLootFunctionWrapper) functions[f]).function;
			else
				CraftTweakerAPI.logError(I18n.format(LootTweakerMain.MODID + ".messages.error.unparseableType", functions[f]));
		}
		return parsedFunctions;
	}

	public static LootFunction parseJSONFunction(JsonElement functionJson)
	{
		return CommonMethodHandles.getLootTableGSON().fromJson(functionJson, LootFunction.class);
	}

	@Deprecated
	public static LootFunction parseJSONFunction(String functionJson)
	{
		return CommonMethodHandles.getLootTableGSON().fromJson(functionJson, LootFunction.class);
	}
}
