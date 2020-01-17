package leviathan143.loottweaker.common.zenscript.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootTableAccessors;
import leviathan143.loottweaker.common.lib.LootTableFinder;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
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
	private final List<LootTableTweaker> tweaks = new ArrayList<>();
	private final Map<String, ZenLootPoolWrapper> tweakedPools = new HashMap<>();
	private final LootTweakerContext context;

    public ZenLootTableWrapper(LootTweakerContext context, ResourceLocation id)
    {
        this.context = context;
        this.id = id;
    }

    @ZenMethod
    public ZenLootPoolWrapper getPool(String poolName)
    {
        if (tweakedPools.containsKey(poolName))
            return tweakedPools.get(poolName);

        ZenLootPoolWrapper pool = context.wrapPool(poolName, id);
        enqueueTweaker((table) ->
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
                context.getErrorHandler().error("No loot pool with name %s exists in table %s!", poolName, id);
        }, String.format("Retrieved pool %s from table %s", poolName, id));
        return pool;
    }

	@ZenMethod
	public ZenLootPoolWrapper addPool(String poolName, int minRolls, int maxRolls, int minBonusRolls, int maxBonusRolls)
	{
	    ZenLootPoolWrapper pool = context.createPoolWrapper(poolName, id);
	    enqueueTweaker(pool, String.format("Queued pool %s for addition to table %s", poolName, id));
	    pool.setRolls(minRolls, maxRolls);
	    pool.setBonusRolls(minBonusRolls, maxBonusRolls);
        return pool;
	}

	@ZenMethod
	public void removePool(String poolName)
	{
        enqueueTweaker((table) ->
        {
            if (table.getPool(poolName) == null)
            {
                context.getErrorHandler().error("No loot pool with name %s exists in table %s!", poolName, id);
                return;
            }
            table.removePool(poolName);
        }, String.format("Queued pool %s of table %s for removal", poolName, id));
	}

    @ZenMethod
    public void clear()
    {
        enqueueTweaker((table) -> LootTableAccessors.getPools(table).clear(),
            String.format("Queued all pools of table %s for removal", id));
    }

    private void enqueueTweaker(LootTableTweaker tweaker, String description)
    {
        tweaks.add(tweaker);
        CraftTweakerAPI.logInfo(description);
    }

    public void applyTweakers(LootTable table)
    {
        for (LootTableTweaker tweaker : tweaks)
            tweaker.tweak(table);
    }

	public ResourceLocation getId()
    {
        return id;
    }

	public boolean isValid()
	{
	    //Use LootTableList as backup, just in case
	    return LootTableFinder.DEFAULT.exists(id) || LootTableList.getAll().contains(id);
	}

	@FunctionalInterface
	public interface LootTableTweaker
	{
	    public void tweak(LootTable table);
	}
}
