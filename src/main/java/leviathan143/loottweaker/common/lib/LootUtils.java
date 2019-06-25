package leviathan143.loottweaker.common.lib;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.IData;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import leviathan143.loottweaker.common.zenscript.ZenLootConditionWrapper;
import leviathan143.loottweaker.common.zenscript.ZenLootFunctionWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
	public static final LootCondition[] NO_CONDITIONS = new LootCondition[0];
	public static final LootEntry[] NO_ENTRIES = new LootEntry[0];
	public static final LootFunction[] NO_FUNCTIONS = new LootFunction[0];
	public static final LootPool[] NO_POOLS = new LootPool[0];


	public static void dump(World world, ResourceLocation tableLoc, File dumpTarget)
	{	
		if (world.isRemote) return;
		LootTable table = world.getLootTableManager().getLootTableFromLocation(tableLoc);
		try
		{
			dumpTarget.getParentFile().mkdirs();
			dumpTarget.createNewFile();
			FileWriter writer = new FileWriter(dumpTarget);
			try
			{
				JsonWriter dumper = CommonMethodHandles.getLootTableGSON().newJsonWriter(writer);
				dumper.setIndent("  ");
				CommonMethodHandles.getLootTableGSON().toJson(table, table.getClass(), dumper);
			}
			catch (Throwable t)
			{
				LootTweakerMain.logger.warn("Failed to dump loot table {}", tableLoc);
				t.printStackTrace();
			}
			writer.close();
			LootTweakerMain.logger.info(String.format("Loot table %s saved to %s", tableLoc, dumpTarget.getCanonicalPath()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
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

	// Conditions
	public static LootCondition[] parseConditions(Object[] conditions)
	{
		if (conditions == null) return NO_CONDITIONS;
		LootCondition[] parsedConditions = new LootCondition[conditions.length];
		for (int c = 0; c < conditions.length; c++)
		{
			if (conditions[c] instanceof JsonElement)
				parsedConditions[c] = parseJSONCondition((JsonElement) conditions[c]);
			else if (conditions[c] instanceof ZenLootConditionWrapper)
				parsedConditions[c] = ((ZenLootConditionWrapper) conditions[c]).condition;
			else
				CraftTweakerAPI.logError(String.format("%s is not an instance of IData or LootCondition!", conditions[c]));
		}
		return parsedConditions;
	}

	public static LootCondition parseJSONCondition(JsonElement conditionJSON)
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
			else if (functions[f] instanceof ZenLootFunctionWrapper)
				parsedFunctions[f] = ((ZenLootFunctionWrapper) functions[f]).function;
			else
				CraftTweakerAPI.logError(String.format("%s is not an instance of IData or LootFunction!", functions[f]));
		}
		return parsedFunctions;
	}

	public static LootFunction parseJSONFunction(JsonElement functionJson)
	{
		return CommonMethodHandles.getLootTableGSON().fromJson(functionJson, LootFunction.class);
	}
}
