package leviathan143.loottweaker.common.zenscript.wrapper;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import javax.inject.Inject;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootTableAccessors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".vanilla.loot.LootTable")
public class ZenLootTableWrapper
{
	private final ResourceLocation id;
	private final Queue<LootTableTweak> tweaks = new ArrayDeque<>();
	private final Map<String, ZenLootPoolWrapper> tweakedPools = new HashMap<>();
	@Inject
	ErrorHandler errorHandler;

    public ZenLootTableWrapper(ResourceLocation id)
    {
        this.id = id;
    }

    @ZenMethod
    public ZenLootPoolWrapper getPool(String poolName)
    {
        if (tweakedPools.containsKey(poolName))
            return tweakedPools.get(poolName);

        ZenLootPoolWrapper pool = new ZenLootPoolWrapper(poolName, id);
        enqueueTweak((table) ->
        {
            LootPool targetPool = table.getPool(poolName);
            if (targetPool != null)
            {
                if (!targetPool.isFrozen())
                    pool.tweak(targetPool);
                else
                    CraftTweakerAPI.logInfo(String.format("Skipped modifying pool %s of table %s because it is frozen", poolName, id));
            }
            else
                errorHandler.handle(String.format("No loot pool with name %s exists in table %s!", poolName, id));
        }, String.format("Retrieved pool %s from table %s", poolName, id));
        return pool;
    }

	@ZenMethod
	public ZenLootPoolWrapper addPool(String poolName, int minRolls, int maxRolls, int minBonusRolls, int maxBonusRolls)
	{
	    ZenLootPoolWrapper pool = new ZenLootPoolWrapper(poolName, id, minRolls, maxRolls, minBonusRolls, maxBonusRolls);
	    enqueueTweak(pool, String.format("Queued pool %s for addition to table %s", poolName, id));
        return pool;
	}

	@ZenMethod
	public void removePool(String poolName)
	{
        enqueueTweak((table) ->
        {
            if (table.getPool(poolName) == null)
            {
                errorHandler.handle(String.format("No loot pool with name %s exists in table %s!", poolName, id));
                return;
            }
            table.removePool(poolName);
        }, String.format("Queued pool %s of table %s for removal", poolName, id));
	}

    @ZenMethod
    public void clear()
    {
        enqueueTweak((table) -> LootTableAccessors.getPools(table).clear(),
            String.format("Queued all pools of table %s for removal", id));
    }

    private void enqueueTweak(LootTableTweak tweak, String description)
    {
        tweaks.add(tweak);
        CraftTweakerAPI.logInfo(description);
    }

    public void applyTweaks(LootTable table)
    {
        while(!tweaks.isEmpty())
            tweaks.poll().tweak(table);
    }

	public ResourceLocation getId()
    {
        return id;
    }

	public boolean isValid()
	{
	    //Use LootTableList as backup, just in case
	    return getClass().getClassLoader()
            .getResource("assets/" + id.getNamespace() + "/loot_tables/" + id.getPath() + ".json") != null
            || LootTableList.getAll().contains(id);
	}

	@FunctionalInterface
	public interface LootTableTweak
	{
	    public void tweak(LootTable table);
	}
}
