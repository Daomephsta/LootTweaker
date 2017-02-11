package leviathan143.loottweaker.common.zenscript;

import java.util.Map;

import com.google.common.collect.Maps;

import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import leviathan143.loottweaker.common.LootUtils;
import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import leviathan143.loottweaker.common.tweakers.loot.LootTableTweaker;
import leviathan143.loottweaker.common.tweakers.loot.LootTableTweaker.LootTweakType;
import minetweaker.MineTweakerAPI;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(Constants.MODID + ".vanilla.loot.LootTable")
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
	public void clear()
	{
		clear = true;
	}

	@ZenMethod
	public ZenLootPoolWrapper addPool(String poolName, int minRolls, int maxRolls, int minBonusRolls, int maxBonusRolls)
	{
		LootPool pool = LootUtils.createPool(poolName, minRolls, maxRolls, minBonusRolls, maxBonusRolls);
		backingTable.addPool(pool);
		lootTweakTypeMap.put(poolName, LootTweakType.ADD);
		return new ZenLootPoolWrapper(pool);
	}

	@ZenMethod
	public void removePool(String poolName)
	{
		lootTweakTypeMap.put(poolName, LootTweakType.REMOVE);
	}

	@ZenMethod
	public ZenLootPoolWrapper getPool(String poolName)
	{
		LootPool pool = null;
		//Create a temporary pool if the pool is a default pool
		if(LootUtils.DEFAULT_POOL_REGEX.matcher(poolName).find())
		{
			pool = LootUtils.createTemporaryPool(poolName);
			backingTable.addPool(pool);
		}
		//Don't set the tweak type to TWEAK if the pool already exists
		if(!lootTweakTypeMap.containsKey(poolName))
			lootTweakTypeMap.put(poolName, LootTweakType.TWEAK);
		return new ZenLootPoolWrapper(pool != null ? pool : backingTable.getPool(poolName));
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
		    	if(backingTable.getPool(lootTweak.getKey()) == null)
		    	{
		    	    MineTweakerAPI.logError(String.format("No loot pool with name %s exists!", lootTweak.getKey()));
		    	}
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
