package leviathan143.loottweaker.common.zenscript.impl;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import crafttweaker.CraftTweakerAPI;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootTableAccessors;
import leviathan143.loottweaker.common.zenscript.api.LootTableRepresentation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;

public class MutableLootTable implements LootTableRepresentation
{
    private static final Logger SANITY_LOGGER = LogManager.getLogger(LootTweaker.MODID + ".sanity_checks");
    private final ResourceLocation id;
    private Map<String, MutableLootPool> pools;
    private final LootTweakerContext context;

    public MutableLootTable(LootTable table, ResourceLocation id, LootTweakerContext context)
    {
        this.context = context;
        this.id = id;
        List<LootPool> immutablePools = LootTableAccessors.getPools(table);
        this.pools = new HashMap<>(immutablePools.size());
        int uniqueSuffix = 0;
        for (LootPool pool : immutablePools)
        {
            MutableLootPool mutablePool = new MutableLootPool(pool);
            MutableLootPool existing = pools.get(pool.getName());
            if (existing != null)
            {
                String newName = mutablePool.getName() + uniqueSuffix++;
                SANITY_LOGGER.error("Unexpected duplicate pool name '{}' in table '{}'. Duplicate added as '{}'."
                    + "\nReport this to the loot adder.", mutablePool.getName(), getId(), newName);
                mutablePool.setName(newName);
                pools.put(newName, mutablePool);
            }
            else
                pools.put(mutablePool.getName(), mutablePool);
        }
    }

    public MutableLootTable(ResourceLocation id, Map<String, MutableLootPool> pools, LootTweakerContext context)
    {
        this.id = id;
        this.pools = pools;
        this.context = context;
    }

    public MutableLootTable deepClone()
    {
        //Can never be duplicate entries when deep cloning, but be informative just in case
        BinaryOperator<MutableLootPool> mergeFunction = (a, b) ->
        {
            throw new IllegalStateException(String.format(
                "Unexpected duplicate pool '%s' while deep cloning mutable table '%s'. Report this to the mod author",
                a.getName(), id));
        };
        Map<String, MutableLootPool> poolsDeepClone = pools.entrySet().stream()
            .collect(toMap(Map.Entry::getKey, e -> e.getValue().deepClone(), mergeFunction, HashMap::new));
        return new MutableLootTable(id, poolsDeepClone, context);
    }

    public LootTable toImmutable()
    {
        LootPool[] poolsArray = pools.values().stream()
            .map(MutableLootPool::toImmutable)
            .toArray(LootPool[]::new);
        return new LootTable(poolsArray);
    }

    public ResourceLocation getId()
    {
        return id;
    }

    public Map<String, MutableLootPool> getPools()
    {
        return pools;
    }

    @Nullable
    public MutableLootPool getPool(String name)
    {
        return pools.get(name);
    }

    public void addPool(MutableLootPool pool)
    {
        if (pools.putIfAbsent(pool.getName(), pool) != null)
            throw new IllegalArgumentException(String.format("Duplicate pool name '%s' in table '%s'", pool.getName(), id));
    }

    @Override
    public void removePool(String poolId)
    {
        if (pools.remove(poolId) == null)
            context.getErrorHandler().error("No pool with id '%s' exists in table '%s'", poolId, id);
        else
            CraftTweakerAPI.logInfo(String.format("Removed pool '%s' from table '%s'", poolId, id));
    }

    public void clearPools()
    {
        pools.clear();
    }
}
