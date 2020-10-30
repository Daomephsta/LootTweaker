package leviathan143.loottweaker.common.zenscript.impl;

import static java.util.stream.Collectors.toMap;

import java.util.*;
import java.util.function.BinaryOperator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import crafttweaker.CraftTweakerAPI;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootTableAccessors;
import leviathan143.loottweaker.common.lib.QualifiedPoolIdentifier;
import leviathan143.loottweaker.common.zenscript.api.LootPoolRepresentation;
import leviathan143.loottweaker.common.zenscript.api.LootTableRepresentation;
import leviathan143.loottweaker.common.zenscript.api.iteration.LootPoolIterator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;

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
            MutableLootPool mutablePool = new MutableLootPool(pool, id, context);
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

    @Override
    public LootPoolRepresentation getPool(String poolId)
    {
        if (!pools.containsKey(poolId))
        {
            context.getErrorHandler().error("No loot pool with id '%s' exists in table '%s'", poolId, id);
            //Cannot return null, or NPEs make it harder to find any other script errors
            return new MutableLootPool(new QualifiedPoolIdentifier(new ResourceLocation("loottweaker:invalid"), "invalid"),
                new HashMap<>(), new ArrayList<>(), new RandomValueRange(0), new RandomValueRange(0), context);
        }
        return pools.get(poolId);
    }

    @Override
    public LootPoolRepresentation addPool(String poolId, float minRolls, float maxRolls)
    {
        return addPool(poolId, minRolls, maxRolls, 0, 0);
    }

    @Override
    public LootPoolRepresentation addPool(String poolId, float minRolls, float maxRolls, float minBonusRolls, float maxBonusRolls)
    {
        MutableLootPool pool = new MutableLootPool(new QualifiedPoolIdentifier(id, poolId), new HashMap<>(), new ArrayList<>(),
            new RandomValueRange(minRolls, maxRolls), new RandomValueRange(minBonusRolls, maxBonusRolls), context);
        if (pools.putIfAbsent(poolId, pool) != null)
            context.getErrorHandler().error("Cannot add pool '%s' to table '%s'. Pool names must be unique within their table.", poolId, id);
        else
            CraftTweakerAPI.logInfo(String.format("Added pool '%s' to table '%s'", poolId, id));
        return pool;
    }

    @Override
    public void removePool(String poolId)
    {
        if (pools.remove(poolId) == null)
            context.getErrorHandler().error("No pool with id '%s' exists in table '%s'", poolId, id);
        else
            CraftTweakerAPI.logInfo(String.format("Removed pool '%s' from table '%s'", poolId, id));
    }

    @Override
    public void removeAllPools()
    {
        pools.clear();
        CraftTweakerAPI.logInfo(String.format("Removed all pools from table '%s'", id));
    }

    @Override
    public Iterator<LootPoolIterator> iterator()
    {
        return new LootPoolIterator(pools.values().iterator(), context);
    }
}
