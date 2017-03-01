package leviathan143.loottweaker.common.zenscript;

import org.apache.commons.lang3.ArrayUtils;

import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import leviathan143.loottweaker.common.lib.LootUtils;
import leviathan143.loottweaker.common.loot.LootEntryPendingRemoval;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
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
    public void addConditionsMixed(Object[] conditions)
    {
	MineTweakerAPI.apply(new AddConditions(backingPool, LootUtils.convertToConditions(conditions)));
    }

    @ZenMethod
    public void addConditions(ZenLootConditionWrapper[] conditions)
    {
	MineTweakerAPI.apply(new AddConditions(backingPool, LootUtils.convertConditionWrappers(conditions)));
    }

    @ZenMethod
    public void addConditions(String[] conditions)
    {
	MineTweakerAPI.apply(new AddConditions(backingPool, LootUtils.parseConditions(conditions)));
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
    public void addItemEntryMixed(IItemStack iStack, int weight, int quality, Object[] functions, Object[] conditions)
    {
	addItemEntry(iStack, weight, quality, LootUtils.convertToFunctions(functions), LootUtils.convertToConditions(conditions));
    }

    @ZenMethod
    public void addItemEntry(IItemStack iStack, int weight, int quality, ZenLootFunctionWrapper[] functions, ZenLootConditionWrapper[] conditions)
    {
	addItemEntry(iStack, weight, quality, LootUtils.convertFunctionWrappers(functions), LootUtils.convertConditionWrappers(conditions));
    }

    @ZenMethod
    public void addItemEntry(IItemStack iStack, int weight, int quality, String[] functions, String[] conditions)
    {
	addItemEntry(iStack, weight, quality, LootUtils.parseFunctions(functions), LootUtils.parseConditions(conditions));
    }

    private void addItemEntry(IItemStack iStack, int weight, int quality, LootFunction[] functions, LootCondition[] conditions)
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
				LootUtils.addStackFunctions(iStack, functions),
				conditions,
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
    public void addLootTableEntryMixed(String tableName, int weightIn, int qualityIn, Object[] conditions)
    {
	addLootTableEntry(tableName, weightIn, qualityIn, LootUtils.convertToConditions(conditions));
    }
    
    @ZenMethod
    public void addLootTableEntry(String tableName, int weightIn, int qualityIn, ZenLootConditionWrapper[] conditions)
    {
	addLootTableEntry(tableName, weightIn, qualityIn, LootUtils.convertConditionWrappers(conditions));
    }

    @ZenMethod
    public void addLootTableEntry(String tableName, int weightIn, int qualityIn, String[] conditions)
    {
	addLootTableEntry(tableName, weightIn, qualityIn, LootUtils.parseConditions(conditions));
    }

    private void addLootTableEntry(String tableName, int weightIn, int qualityIn, LootCondition[] conditions)
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
				conditions, 
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
	private LootEntry entry;
	private LootPool pool;

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
	private String entryName;
	private LootPool pool;

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
    
    private static class AddConditions implements IUndoableAction
    {
	private LootCondition[] conditions;
	private LootPool pool;

	public AddConditions(LootPool pool, LootCondition[] conditions) 
	{
	    this.conditions = conditions;
	    this.pool = pool;
	}

	@Override
	public void apply() 
	{
	    LootUtils.addConditionsToPool(pool, conditions);
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
	    return String.format("Adding conditions %s to pool %s", ArrayUtils.toString(conditions), pool.getName());
	}

	@Override
	public String describeUndo() 
	{
	    return String.format("Removing conditions %s from pool %s", ArrayUtils.toString(conditions), pool.getName());
	}

	@Override
	public Object getOverrideKey() 
	{
	    return null;
	}
    }
}
