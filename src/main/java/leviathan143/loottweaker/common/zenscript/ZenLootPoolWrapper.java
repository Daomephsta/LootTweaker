package leviathan143.loottweaker.common.zenscript;

import org.apache.commons.lang3.ArrayUtils;

import leviathan143.loottweaker.common.LootUtils;
import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(Constants.MODID + ".vanilla.loot.LootPool")
public class ZenLootPoolWrapper 
{
	private final LootPool backingPool;

	public ZenLootPoolWrapper(LootPool pool) 
	{
		backingPool = pool;
	}
	
	@ZenMethod
	public void addConditions(String[] conditions)
	{
		LootUtils.addConditionsToPool(backingPool, LootUtils.parseConditions(conditions));
	}
	
	@ZenMethod
	public void removeItemEntry(IItemStack stack)
	{
		Item item = MineTweakerMC.getItemStack(stack).getItem();
		removeEntry(item.getRegistryName().toString());
	}

	@ZenMethod
	public void removeLootTableEntry(String tableName)
	{
		removeEntry(tableName);
	}

	@ZenMethod
	public void removeEntry(String entryName)
	{
		MineTweakerAPI.apply(new RemoveLootEntry(backingPool, entryName));
	}

	@ZenMethod
	public void addItemEntry(IItemStack stack, int weightIn)
	{
		addItemEntry(stack, weightIn, 1, ArrayUtils.EMPTY_STRING_ARRAY, ArrayUtils.EMPTY_STRING_ARRAY);
	}

	@ZenMethod
	public void addItemEntry(IItemStack stack, int weightIn, int qualityIn)
	{
		addItemEntry(stack, weightIn, qualityIn, ArrayUtils.EMPTY_STRING_ARRAY, ArrayUtils.EMPTY_STRING_ARRAY);
	}

	@ZenMethod
	public void addItemEntry(IItemStack iStack, int weight, int quality, String[] functions, String[] conditions)
	{
		Item item = MineTweakerMC.getItemStack(iStack).getItem();
		MineTweakerAPI.apply
		(
				new AddLootEntry
				(
						backingPool,
						new LootEntryItem
						(
								item,
								weight,
								quality,
								ArrayUtils.addAll(LootUtils.parseFunctions(functions), LootUtils.translateStackToFunctions(iStack)),
								LootUtils.parseConditions(conditions),
								item.getRegistryName().toString() + "_" + iStack.getDamage()
						)
				)
		);
	}

	@ZenMethod
	public void addLootTableEntry(String tableName, int weightIn)
	{
		addLootTableEntry(tableName, weightIn, 1);
	}

	@ZenMethod
	public void addLootTableEntry(String tableName, int weightIn, int qualityIn)
	{
		addLootTableEntry(tableName, weightIn, qualityIn, ArrayUtils.EMPTY_STRING_ARRAY);
	}

	@ZenMethod
	public void addLootTableEntry(String tableName, int weightIn, int qualityIn, String[] conditionsIn)
	{
		MineTweakerAPI.apply
		(
				new AddLootEntry
				(
						backingPool,
						new LootEntryTable
						(
								new ResourceLocation(tableName), 
								weightIn, 
								qualityIn, 
								LootUtils.parseConditions(conditionsIn), 
								tableName
						)
				)
		);
	}
	
	public static void applyLootTweaks(LootPool backingPool, LootPool pool)
	{
		for(LootEntry tempEntry : CommonMethodHandles.getEntriesFromPool(backingPool))
		{
			if(tempEntry instanceof LootEntryPendingRemoval) pool.removeEntry(tempEntry.getEntryName());
			else pool.addEntry(tempEntry);
		}
		CommonMethodHandles.getConditionsFromPool(pool).addAll(CommonMethodHandles.getConditionsFromPool(backingPool));
	}

	private static class AddLootEntry implements IUndoableAction
	{
		LootEntry entry;
		LootPool pool;

		public AddLootEntry(LootPool pool, LootEntry entry) 
		{
			this.entry = entry;
			this.pool = pool;
		}

		@Override
		public void apply() 
		{
			pool.addEntry(entry);
		}

		@Override
		public boolean canUndo()
		{
			return true;
		}

		//No undo needed, the tweak map is cleared on reload anyway
		@Override
		public void undo() {}

		@Override
		public String describe() 
		{
			return String.format("Adding entry %s to pool %s", entry.getEntryName(), pool.getName());
		}

		@Override
		public String describeUndo() 
		{
			return String.format("Removing entry %s from pool %s", entry.getEntryName(), pool.getName());
		}

		@Override
		public Object getOverrideKey() 
		{
			return null;
		}
	}

	private static class RemoveLootEntry implements IUndoableAction
	{
		String entryName;
		LootPool pool;

		public RemoveLootEntry(LootPool pool, String entryName) 
		{
			this.entryName = entryName;
			this.pool = pool;
		}

		@Override
		public void apply() 
		{
			pool.addEntry(new LootEntryPendingRemoval(entryName));
		}

		@Override
		public boolean canUndo()
		{
			return true;
		}

		//No undo needed, the tweak map is cleared on reload anyway
		@Override
		public void undo() {}

		@Override
		public String describe() 
		{
			return String.format("Removing entry %s from pool %s", entryName, pool.getName());
		}

		@Override
		public String describeUndo() 
		{
			return String.format("Adding entry %s to pool %s", entryName, pool.getName());
		}

		@Override
		public Object getOverrideKey() 
		{
			return null;
		}
	}
}
