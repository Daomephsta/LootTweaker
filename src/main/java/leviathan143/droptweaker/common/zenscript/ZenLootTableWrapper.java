package leviathan143.droptweaker.common.zenscript;

import java.util.Map;

import com.google.common.collect.Maps;

import leviathan143.droptweaker.common.LootUtils;
import leviathan143.droptweaker.common.darkmagic.CommonMethodHandles;
import leviathan143.droptweaker.common.handlers.DropHandler;
import leviathan143.droptweaker.common.tweakers.loot.LootTableTweaker;
import leviathan143.droptweaker.common.tweakers.loot.LootTableTweaker.LootTweakType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("droptweaker.vanilla.loot.LootTable")
public class ZenLootTableWrapper 
{
	private ResourceLocation name;
	private final LootTable backingTable;
	private final Map<String, LootTableTweaker.LootTweakType> lootTweakTypeMap = Maps.<String, LootTableTweaker.LootTweakType>newHashMap();
	//If true the table is wiped
	private boolean clear;

	public ZenLootTableWrapper(LootTable table, ResourceLocation name) 
	{
		this.backingTable = table;
		this.name = name;
	}
	
	@ZenMethod
	public void clearDefaults()
	{
		clear = true;
		DropHandler.clearedLootTables.add(name);
	}

	@ZenMethod
	public void addPool(String poolName, int minRolls, int maxRolls, int minBonusRolls, int maxBonusRolls)
	{
		backingTable.addPool(LootUtils.createPool(poolName, minRolls, maxRolls, minBonusRolls, maxBonusRolls));
		lootTweakTypeMap.put(poolName, LootTweakType.ADD);
	}
	
	@ZenMethod
	public void removePool(String poolName)
	{
		lootTweakTypeMap.put(poolName, LootTweakType.REMOVE);
	}
	
	@ZenMethod
	public ZenLootPoolWrapper getPool(String poolName)
	{
		//Create a temporary pool if the pool is a default pool
		if(LootUtils.DEFAULT_POOL_REGEX.matcher(poolName).find())
			backingTable.addPool(LootUtils.createTemporaryPool(poolName));
		//Don't set the tweak type to TWEAK if the pool already exists
		if(!lootTweakTypeMap.containsKey(poolName))
			lootTweakTypeMap.put(poolName, LootTweakType.TWEAK);
		return new ZenLootPoolWrapper(backingTable.getPool(poolName));
	}

	public void applyLootTweaks(LootTable table)
	{
		if(clear)
		{
			for(java.util.Iterator<LootPool> iter = CommonMethodHandles.getPoolsFromTable(table).iterator(); iter.hasNext();)
			{
				iter.next();
				iter.remove();
			}
		}
		for(Map.Entry<String, LootTableTweaker.LootTweakType> lootTweak : lootTweakTypeMap.entrySet())
		{
			switch (lootTweak.getValue()) 
			{
			case ADD:
				table.addPool(backingTable.getPool(lootTweak.getKey()));
				break;

			case TWEAK:
				ZenLootPoolWrapper.applyLootTweaks(backingTable.getPool(lootTweak.getKey()), table.getPool(lootTweak.getKey()));
				break;

			case REMOVE:
				table.removePool(lootTweak.getKey());
				break;

			default:
				break;
			}
		}
	}
}
