package leviathan143.loottweaker.common.zenscript;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Lists;

import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import leviathan143.loottweaker.common.lib.IDelayedTweak;
import leviathan143.loottweaker.common.lib.LootUtils;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(Constants.MODID + ".vanilla.loot.LootPool")
public class ZenLootPoolWrapper 
{
    private final LootPool backingPool;
    private final List<IDelayedTweak<LootPool, ZenLootPoolWrapper>> delayedTweaks = Lists.newArrayList();

    public ZenLootPoolWrapper(LootPool pool) 
    {
	if(pool == null) throw new IllegalArgumentException("Backing pool cannot be null!");
	backingPool = pool;
    }

    @ZenMethod
    public void addConditionsHelper(ZenLootConditionWrapper[] conditions)
    {
	MineTweakerAPI.apply(new AddConditions(this, LootUtils.parseConditions(conditions)));
    }

    public void addConditionsJSON(String[] conditions)
    {
	MineTweakerAPI.apply(new AddConditions(this, LootUtils.parseConditions(conditions)));
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
	if(!LootTableList.getAll().contains(new ResourceLocation(tableName)))
	{
	    MineTweakerImplementationAPI.logger.logError(tableName + " is not a loot table!");
	    return;
	}
	removeEntry(tableName);
    }

    @ZenMethod
    public void removeEntry(String entryName)
    {
	MineTweakerAPI.apply(new RemoveLootEntry(this, entryName));
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
	addItemEntryInternal(iStack, weight, quality, LootUtils.parseFunctions(functions), LootUtils.parseConditions(conditions), name);
    }

    @ZenMethod
    public void addItemEntryJSON(IItemStack iStack, int weight, int quality, String[] functions, String[] conditions, @Optional String name)
    {
	addItemEntryInternal(iStack, weight, quality, LootUtils.parseFunctions(functions), LootUtils.parseConditions(conditions), name);
    }

    private void addItemEntryInternal(IItemStack iStack, int weight, int quality, LootFunction[] functions, LootCondition[] conditions, String name)
    { 
	Item item = MineTweakerMC.getItemStack(iStack).getItem();
	if(name == null)
	    name = item.getRegistryName().toString();

	MineTweakerAPI.apply
	(
		new AddLootEntry
		(
			this,
			new LootEntryItem
			(
				item,
				weight,
				quality,
				LootUtils.addStackFunctions(iStack, functions),
				conditions,
				name
				)
			)
		);
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
    public void addLootTableEntryJSON(String tableName, int weightIn, int qualityIn, String[] conditions, @Optional String name)
    {
	addLootTableEntryInternal(tableName, weightIn, qualityIn, LootUtils.parseConditions(conditions), name);
    }

    private void addLootTableEntryInternal(String tableName, int weightIn, int qualityIn, LootCondition[] conditions, String name)
    {
	if(name == null)
	    name = tableName;
	MineTweakerAPI.apply
	(
		new AddLootEntry
		(
			this,
			new LootEntryTable
			(
				new ResourceLocation(tableName), 
				weightIn, 
				qualityIn, 
				conditions, 
				name
				)
			)
		);
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
    public void addEmptyEntryJSON(int weight, int quality, String[] conditions, @Optional String name)
    {
	addEmptyEntryInternal(weight, quality, LootUtils.parseConditions(conditions), name);
    }
    
    public void addEmptyEntryInternal(int weightIn, int qualityIn, LootCondition[] conditions, @Optional String name)
    {
	if(name == null) name = "empty";
	CraftTweakerAPI.apply(new AddLootEntry(this, new LootEntryEmpty(weightIn, qualityIn, conditions, name)));
    }

    @ZenMethod
    public void setRolls(float minRolls, float maxRolls)
    {
	MineTweakerAPI.apply(new SetRolls(this, new RandomValueRange(minRolls, maxRolls)));
    }

    @ZenMethod
    public void setBonusRolls(float minBonusRolls, float maxBonusRolls)
    {
	MineTweakerAPI.apply(new SetBonusRolls(this, new RandomValueRange(minBonusRolls, maxBonusRolls)));
    }

    public void applyLootTweaks(LootPool pool)
    {
	for(IDelayedTweak<LootPool, ZenLootPoolWrapper> tweak : delayedTweaks)
	{
	    tweak.applyTweak(pool, this);
	}
    }

    public LootPool getPool()
    {
	return backingPool;
    }

    private static class AddLootEntry extends UndoableDelayedPoolTweak
    {
	private LootEntry entry;

	public AddLootEntry(ZenLootPoolWrapper wrapper, LootEntry entry) 
	{
	    super(wrapper);
	    this.entry = entry;
	}
	
	@Override
	public void applyTweak(LootPool pool, ZenLootPoolWrapper zenWrapper)
	{
	    if(pool.getEntry(entry.getEntryName()) != null)
	    {
		int counter = 1;
		String baseName = entry.getEntryName();
		String name = baseName;
		while(pool.getEntry(name) != null)
		{
		    name = baseName + "-lt#" + ++counter;
		}
		ObfuscationReflectionHelper.setPrivateValue(LootEntry.class, entry, name, "entryName");
	    }
	    pool.addEntry(entry);
	}

	@Override
	public String describe() 
	{
	    return String.format("Adding entry %s to pool %s", entry.getEntryName(), wrapper.backingPool.getName());
	}

	@Override
	public String describeUndo() 
	{
	    return String.format("Removing entry %s from pool %s", entry.getEntryName(), wrapper.backingPool.getName());
	}
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
	    if(pool.removeEntry(entryName) == null)
	    {
		MineTweakerAPI.logError(String.format("No entry with name %s exists in pool %s", entryName, zenWrapper.backingPool.getName()));
		return;
	    }
	}

	@Override
	public String describe() 
	{
	    return String.format("Removing entry %s from pool %s", entryName, wrapper.backingPool.getName());
	}

	@Override
	public String describeUndo() 
	{
	    return String.format("Adding entry %s to pool %s", entryName, wrapper.backingPool.getName());
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

	public void applyTweak(LootPool pool, ZenLootPoolWrapper zenWrapper)
	{
	    Collections.addAll(CommonMethodHandles.getConditionsFromPool(pool), conditions);
	}

	@Override
	public String describe() 
	{
	    return String.format("Adding conditions %s to pool %s", ArrayUtils.toString(conditions), wrapper.backingPool.getName());
	}

	@Override
	public String describeUndo() 
	{
	    return String.format("Removing conditions %s from pool %s", ArrayUtils.toString(conditions), wrapper.backingPool.getName());
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
	    return String.format("Setting rolls for pool %s to (%f, %f)", wrapper.backingPool.getName(), range.getMin(), range.getMax());
	}

	@Override
	public String describeUndo()
	{
	    return String.format("Resetting rolls for pool %s to defaults", wrapper.backingPool.getName());
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
	    return String.format("Setting bonusRolls for pool %s to (%f, %f)", wrapper.backingPool.getName(), range.getMin(), range.getMax());
	}

	@Override
	public String describeUndo()
	{
	    return String.format("Resetting bonusRolls for pool %s to defaults", wrapper.backingPool.getName());
	}
    }
    
    private static abstract class UndoableDelayedPoolTweak implements IUndoableAction, IDelayedTweak<LootPool, ZenLootPoolWrapper>
    {
	protected ZenLootPoolWrapper wrapper;
	
	public UndoableDelayedPoolTweak(ZenLootPoolWrapper wrapper)
	{
	    this.wrapper = wrapper;
	}
	
	@Override
	public void apply() 
	{
	    wrapper.delayedTweaks.add(this);
	}

	@Override
	public boolean canUndo()
	{
	    return true;
	}

	@Override
	public void undo() 
	{
	    wrapper.delayedTweaks.remove(this);
	}

	@Override
	public Object getOverrideKey() 
	{
	    return null;
	}
    }
}
