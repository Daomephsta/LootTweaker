package leviathan143.loottweaker.common.zenscript;

import java.util.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import leviathan143.loottweaker.common.lib.*;
import leviathan143.loottweaker.common.zenscript.actions.AddLootEntry;
import leviathan143.loottweaker.common.zenscript.actions.UndoableDelayedPoolTweak;
import leviathan143.loottweaker.common.zenscript.adders.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweakerMain.MODID + ".vanilla.loot.LootPool")
public class ZenLootPoolWrapper
{
	private static final Logger logger = LogManager.getLogger();
	final LootPool backingPool;
	final List<IDelayedTweak<LootPool, ZenLootPoolWrapper>> delayedTweaks = Lists.newArrayList();

	public ZenLootPoolWrapper(LootPool pool)
	{
		if (pool == null) throw new IllegalArgumentException("Backing pool cannot be null!");
		backingPool = pool;
	}

	@ZenMethod
	public void addConditionsHelper(ZenLootConditionWrapper[] conditions)
	{
		CraftTweakerAPI.apply(new AddConditions(this, LootUtils.parseConditions(conditions)));
	}

	@ZenMethod
	public void addConditionsJson(IData[] conditions)
	{
		if(!checkAllAreMaps(conditions)) return;
		JsonElement[] conditionsJSON = Arrays.stream(conditions).map(DataToJSONConverter::from).toArray(JsonElement[]::new);
		CraftTweakerAPI.apply(new AddConditions(this, LootUtils.parseConditions(conditionsJSON)));
	}

	@ZenMethod
	public void removeEntry(String entryName)
	{
		CraftTweakerAPI.apply(new RemoveLootEntry(this, entryName));
	}
	
	@ZenMethod
	public ItemEntryAdder itemEntryAdder(IItemStack stack)
	{
		return new ItemEntryAdder(this, stack);
	}
	
	@ZenMethod
	public LootTableEntryAdder lootTableEntryAdder(ResourceLocation table)
	{
		return new LootTableEntryAdder(this, table);
	}
	
	@ZenMethod
	public EmptyEntryAdder emptyEntryAdder()
	{
		return new EmptyEntryAdder(this);
	}

	@ZenMethod
	public void addItemEntry(IItemStack stack, int weightIn, @Optional String name)
	{
		addItemEntry(stack, weightIn, 1, name);
	}

	@ZenMethod
	public void addItemEntry(IItemStack stack, int weightIn, int qualityIn, @Optional String name)
	{
		addItemEntryHelper(stack, weightIn, qualityIn, null, null, name);
	}

	@ZenMethod
	public void addItemEntryHelper(IItemStack iStack, int weight, int quality, ZenLootFunctionWrapper[] functions, ZenLootConditionWrapper[] conditions, @Optional String name)
	{
		addItemEntryInternal(iStack, weight, quality, LootUtils.parseFunctions(functions),
				LootUtils.parseConditions(conditions), name);
	}
	
	@ZenMethod
	public void addItemEntryJson(IItemStack iStack, int weight, int quality, IData[] functions, IData[] conditions, @Optional String name)
	{
		if(!checkAllAreMaps(functions)) return;
		if(!checkAllAreMaps(conditions)) return;
		JsonElement[] conditionsJSON = Arrays.stream(conditions).map(DataToJSONConverter::from).toArray(JsonElement[]::new);
		JsonElement[] functionsJSON = Arrays.stream(functions).map(DataToJSONConverter::from).toArray(JsonElement[]::new);
		addItemEntryInternal(iStack, weight, quality, LootUtils.parseFunctions(functionsJSON),
				LootUtils.parseConditions(conditionsJSON), name);
	}

	private void addItemEntryInternal(IItemStack iStack, int weight, int quality, LootFunction[] functions, LootCondition[] conditions, String name)
	{
		Item item = CraftTweakerMC.getItemStack(iStack).getItem();
		if (name == null) name = item.getRegistryName().toString();

		CraftTweakerAPI.apply(new AddLootEntry(this, new LootEntryItem(item, weight, quality,
				LootUtils.addStackFunctions(iStack, functions), conditions, name)));
	}

	@ZenMethod
	public void addLootTableEntry(String tableName, int weightIn, @Optional String name)
	{
		addLootTableEntry(tableName, weightIn, 1, name);
	}

	@ZenMethod
	public void addLootTableEntry(String tableName, int weightIn, int qualityIn, @Optional String name)
	{
		addLootTableEntryHelper(tableName, weightIn, qualityIn, null, name);
	}

	@ZenMethod
	public void addLootTableEntryHelper(String tableName, int weightIn, int qualityIn, ZenLootConditionWrapper[] conditions, @Optional String name)
	{
		addLootTableEntryInternal(tableName, weightIn, qualityIn, LootUtils.parseConditions(conditions), name);
	}

	@ZenMethod
	public void addLootTableEntryJson(String tableName, int weightIn, int qualityIn, IData[] conditions, @Optional String name)
	{
		if(!checkAllAreMaps(conditions)) return;
		JsonElement[] json = Arrays.stream(conditions).map(DataToJSONConverter::from).toArray(JsonElement[]::new);
		addLootTableEntryInternal(tableName, weightIn, qualityIn, LootUtils.parseConditions(json), name);
	}

	private void addLootTableEntryInternal(String tableName, int weightIn, int qualityIn, LootCondition[] conditions, String name)
	{
		if (name == null) name = tableName;
		CraftTweakerAPI.apply(new AddLootEntry(this,
				new LootEntryTable(new ResourceLocation(tableName), weightIn, qualityIn, conditions, name)));
	}

	@ZenMethod
	public void addEmptyEntry(int weight, @Optional String name)
	{
		addEmptyEntryInternal(weight, 1, LootUtils.NO_CONDITIONS, name);
	}

	@ZenMethod
	public void addEmptyEntry(int weight, int quality, @Optional String name)
	{
		addEmptyEntryInternal(weight, quality, LootUtils.NO_CONDITIONS, name);
	}

	@ZenMethod
	public void addEmptyEntryHelper(int weight, int quality, ZenLootConditionWrapper[] conditions, @Optional String name)
	{
		addEmptyEntryInternal(weight, quality, LootUtils.parseConditions(conditions), name);
	}

	@ZenMethod
	public void addEmptyEntryJson(int weight, int quality, IData[] conditions, @Optional String name)
	{
		if(!checkAllAreMaps(conditions)) return;
		JsonElement[] json = Arrays.stream(conditions).map(DataToJSONConverter::from).toArray(JsonElement[]::new);
		addEmptyEntryInternal(weight, quality, LootUtils.parseConditions(json), name);
	}

	public void addEmptyEntryInternal(int weightIn, int qualityIn, LootCondition[] conditions, @Optional String name)
	{
		if (name == null) name = "empty";
		CraftTweakerAPI.apply(new AddLootEntry(this, new LootEntryEmpty(weightIn, qualityIn, conditions, name)));
	}

	@ZenMethod
	public void setRolls(float minRolls, float maxRolls)
	{
		CraftTweakerAPI.apply(new SetRolls(this, new RandomValueRange(minRolls, maxRolls)));
	}

	@ZenMethod
	public void setBonusRolls(float minBonusRolls, float maxBonusRolls)
	{
		CraftTweakerAPI.apply(new SetBonusRolls(this, new RandomValueRange(minBonusRolls, maxBonusRolls)));
	}

	public void applyLootTweaks(LootPool pool)
	{
		if (pool.isFrozen())
		{
			logger.debug("Skipped modifying pool {} because it is frozen");
			return;
		}
		for (IDelayedTweak<LootPool, ZenLootPoolWrapper> tweak : delayedTweaks)
		{
			tweak.applyTweak(pool, this);
		}
	}

	public LootPool getPool()
	{
		return backingPool;
	}
	
	public void addDelayedTweak(IDelayedTweak<LootPool, ZenLootPoolWrapper> tweak)
	{
		delayedTweaks.add(tweak);
	}
	
	private boolean checkAllAreMaps(IData[] data)
	{
		return Arrays.stream(data).allMatch(ZenScriptUtils::checkIsMap);
	}

	private static class RemoveLootEntry extends UndoableDelayedPoolTweak
	{
		private String entryName;

		public RemoveLootEntry(ZenLootPoolWrapper wrapper, String entryName)
		{
			super(wrapper);
			this.entryName = entryName;
		}

		@Override
		public void applyTweak(LootPool pool, ZenLootPoolWrapper zenWrapper)
		{
			if (pool.removeEntry(entryName) == null)
			{
				CraftTweakerAPI.logError(I18n.format(LootTweakerMain.MODID + ".messages.error.invalidEntryName", entryName,
						zenWrapper.backingPool.getName()));
				return;
			}
		}

		@Override
		public String describe()
		{
			return String.format("Removing entry %s from pool %s", entryName, wrapper.backingPool.getName());
		}
	}

	private static class AddConditions extends UndoableDelayedPoolTweak
	{
		private LootCondition[] conditions;

		public AddConditions(ZenLootPoolWrapper wrapper, LootCondition[] conditions)
		{
			super(wrapper);
			this.conditions = conditions;
		}

		@Override
		public void applyTweak(LootPool pool, ZenLootPoolWrapper zenWrapper)
		{
			Collections.addAll(CommonMethodHandles.getConditionsFromPool(pool), conditions);
		}

		@Override
		public String describe()
		{
			return String.format("Adding conditions %s to pool %s", ArrayUtils.toString(conditions),
					wrapper.backingPool.getName());
		}
	}

	private static class SetRolls extends UndoableDelayedPoolTweak
	{
		private RandomValueRange range;

		public SetRolls(ZenLootPoolWrapper wrapper, RandomValueRange range)
		{
			super(wrapper);
			this.range = range;
		}

		@Override
		public void applyTweak(LootPool lootPool, ZenLootPoolWrapper zenWrapper)
		{
			lootPool.setRolls(range);
		}

		@Override
		public String describe()
		{
			return String.format("Setting rolls for pool %s to (%f, %f)", wrapper.backingPool.getName(), range.getMin(),
					range.getMax());
		}
	}

	private static class SetBonusRolls extends UndoableDelayedPoolTweak
	{
		private RandomValueRange range;

		public SetBonusRolls(ZenLootPoolWrapper wrapper, RandomValueRange range)
		{
			super(wrapper);
			this.range = range;
		}

		@Override
		public void applyTweak(LootPool lootPool, ZenLootPoolWrapper zenWrapper)
		{
			lootPool.setBonusRolls(range);
		}

		@Override
		public String describe()
		{
			return String.format("Setting bonusRolls for pool %s to (%f, %f)", wrapper.backingPool.getName(),
					range.getMin(), range.getMax());
		}
	}
}
