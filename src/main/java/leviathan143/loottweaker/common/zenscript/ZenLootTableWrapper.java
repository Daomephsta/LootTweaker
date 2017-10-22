package leviathan143.loottweaker.common.zenscript;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import leviathan143.loottweaker.common.lib.IDelayedTweak;
import leviathan143.loottweaker.common.lib.LootUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(Constants.MODID + ".vanilla.loot.LootTable")
public class ZenLootTableWrapper 
{
    private ResourceLocation name;
    private final LootTable backingTable;
    private final List<IDelayedTweak<LootTable, ZenLootTableWrapper>> delayedTweaks = Lists.newArrayList();
    private final Map<String, ZenLootPoolWrapper> wrapperCache = Maps.newHashMap();
    //If true the table is wiped
    private boolean clear;

    public ZenLootTableWrapper(LootTable table, ResourceLocation name) 
    {
	this.backingTable = table;
	this.name = name;
    }

    @ZenMethod
    public void clear()
    {
	clear = true;
    }

    @ZenMethod
    public ZenLootPoolWrapper addPool(String poolName, int minRolls, int maxRolls, int minBonusRolls, int maxBonusRolls)
    {
	LootPool pool = LootUtils.createPool(poolName, minRolls, maxRolls, minBonusRolls, maxBonusRolls);
	CraftTweakerAPI.apply(new AddPool(this, name, pool));
	ZenLootPoolWrapper wrapper = new ZenLootPoolWrapper(pool);
	wrapperCache.put(poolName, wrapper);
	return wrapper;
    }

    @ZenMethod
    public void removePool(String poolName)
    {
	CraftTweakerAPI.apply(new RemovePool(this, name, poolName));
    }

    @ZenMethod
    public ZenLootPoolWrapper getPool(String poolName)
    {
	//Hopefully fix #17
	LootPool pool = backingTable.getPool(poolName);
	if(pool == null)
	{
	    pool = LootUtils.createTemporaryPool(poolName);
	    backingTable.addPool(pool);
	    //Check that an action doesn't already exist
	    if(!wrapperCache.containsKey(poolName)) delayedTweaks.add(new TweakPool(poolName));
	} 
	if(!wrapperCache.containsKey(poolName))
	    wrapperCache.put(poolName, new ZenLootPoolWrapper(pool));
	return wrapperCache.get(poolName);
    }

    private ZenLootPoolWrapper getPoolInternal(String poolName)
    {
	//Hopefully fix #17
	LootPool pool = backingTable.getPool(poolName);
	if(pool == null)
	{
	    pool = LootUtils.createTemporaryPool(poolName);
	    backingTable.addPool(pool);
	} 
	if(!wrapperCache.containsKey(poolName))
	    wrapperCache.put(poolName, new ZenLootPoolWrapper(pool));
	return wrapperCache.get(poolName);
    }

    public void applyLootTweaks(LootTable table)
    {
	if(clear) CommonMethodHandles.getPoolsFromTable(table).clear();
	for(IDelayedTweak<LootTable, ZenLootTableWrapper> tweak : delayedTweaks)
	{
	    tweak.applyTweak(table, this);
	}
    }

    private static class TweakPool implements IDelayedTweak<LootTable, ZenLootTableWrapper>
    {
	private String poolName;

	public TweakPool(String poolName)
	{
	    this.poolName = poolName;
	}

	@Override
	public void applyTweak(LootTable table, ZenLootTableWrapper wrapper)
	{
	    LootPool pool = table.getPool(poolName);
	    if(pool == null) 
	    {
		CraftTweakerAPI.logError(String.format("No loot pool with name %s exists in table %s!", poolName, wrapper.name));
		return;
	    }
	    wrapper.getPoolInternal(poolName).applyLootTweaks(pool);
	}
    }

    private static class AddPool implements IAction, IDelayedTweak<LootTable, ZenLootTableWrapper>
    {
	private ZenLootTableWrapper wrapper;
	private ResourceLocation tableName;
	private LootPool pool;

	public AddPool(ZenLootTableWrapper wrapper, ResourceLocation name, LootPool pool) 
	{
	    this.wrapper = wrapper;
	    this.tableName = name;
	    this.pool = pool;
	}

	@Override
	public void apply() 
	{
	    wrapper.delayedTweaks.add(this);
	}

	@Override
	public void applyTweak(LootTable table, ZenLootTableWrapper wrapper)
	{
	    table.addPool(pool);
	    wrapper.getPoolInternal(pool.getName()).applyLootTweaks(pool);
	}

	@Override
	public String describe() 
	{
	    return String.format("Adding pool %s to table %s", pool.getName(), tableName);
	}
    }

    private static class RemovePool implements IAction, IDelayedTweak<LootTable, ZenLootTableWrapper>
    {
	private ZenLootTableWrapper wrapper;
	private ResourceLocation tableName;
	private String poolName;

	public RemovePool(ZenLootTableWrapper wrapper, ResourceLocation tableName, String poolName) 
	{
	    this.wrapper = wrapper;
	    this.tableName = tableName;
	    this.poolName = poolName;
	}

	@Override
	public void apply() 
	{
	    wrapper.delayedTweaks.add(this);
	}

	@Override
	public void applyTweak(LootTable table, ZenLootTableWrapper wrapper)
	{
	    if(table.getPool(poolName) == null)
	    {
		CraftTweakerAPI.logError(String.format("No loot pool with name %s exists!", poolName));
		return;
	    }
	    table.removePool(poolName);
	}
	@Override
	public String describe() 
	{
	    return String.format("Adding pool %s to table %s", poolName, tableName);
	}
    }
}
