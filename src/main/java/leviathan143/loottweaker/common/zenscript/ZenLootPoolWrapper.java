package leviathan143.loottweaker.common.zenscript;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Lists;

import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import leviathan143.loottweaker.common.lib.IDelayedTweak;
import leviathan143.loottweaker.common.lib.LootUtils;
import crafttweaker.*;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import stanhebben.zenscript.annotations.*;

@ZenRegister
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
	CraftTweakerAPI.apply(new AddConditions(this, LootUtils.parseConditions(conditions)));
    }

    public void addConditionsJSON(String[] conditions)
    {
	CraftTweakerAPI.apply(new AddConditions(this, LootUtils.parseConditions(conditions)));
    }

    @ZenMethod
    public void removeItemEntry(IItemStack stack)
    {
	Item item = CraftTweakerMC.getItemStack(stack).getItem();
	removeEntry(item.getRegistryName().toString());
    }

    @ZenMethod
    public void removeLootTableEntry(String tableName)
    {
	if(!LootTableList.getAll().contains(new ResourceLocation(tableName)))
	{
	    CrafttweakerImplementationAPI.logger.logError(tableName + " is not a loot table!");
	    return;
	}
	removeEntry(tableName);
    }

    @ZenMethod
    public void removeEntry(String entryName)
    {
	CraftTweakerAPI.apply(new RemoveLootEntry(this, entryName));
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
	Item item = CraftTweakerMC.getItemStack(iStack).getItem();
	if(name == null)
	    name = item.getRegistryName().toString();

	CraftTweakerAPI.apply
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
	CraftTweakerAPI.apply
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
    public void setRolls(float minRolls, float maxRolls)
    {
	CraftTweakerAPI.apply(new SetRolls(this, new RandomValueRange(minRolls, maxRolls)));
    }

    @ZenMethod
    public void setBonusRolls(float minBonusRolls, float maxBonusRolls)
    {
	if(minBonusRolls <= 0.0F)
	{
	    CraftTweakerAPI.logError("Minimum parameter of bonusRolls must be greater than 0.0F!");
	    return;
	}
	if(maxBonusRolls <= 0.0F)
	{
	    CraftTweakerAPI.logError("Maximum parameter of bonusRolls must be greater than 0.0F!");
	    return;
	}
	CraftTweakerAPI.apply(new SetBonusRolls(this, new RandomValueRange(minBonusRolls, maxBonusRolls)));
    }

    public void applyLootTweaks(LootPool pool)
    {
	if(pool.isFrozen()) return;
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
		CrafttweakerImplementationAPI.logger.logError("No entry with name " + entryName);
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

	public void applyTweak(LootPool pool, ZenLootPoolWrapper zenWrapper)
	{
	    Collections.addAll(CommonMethodHandles.getConditionsFromPool(pool), conditions);
	}

	@Override
	public String describe() 
	{
	    return String.format("Adding conditions %s to pool %s", ArrayUtils.toString(conditions), wrapper.backingPool.getName());
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
    }
    
    private static abstract class UndoableDelayedPoolTweak implements IAction, IDelayedTweak<LootPool, ZenLootPoolWrapper>
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
    }
}
