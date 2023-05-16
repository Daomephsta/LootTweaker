package leviathan143.loottweaker.common.mutable_loot;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.mixin.LootTableAccessors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;


public class MutableLootTable
{
    private static final Logger SANITY_LOGGER = LogManager.getLogger(LootTweaker.MODID + ".sanity_checks");
    private final ResourceLocation id;
    private Map<String, MutableLootPool> pools;

    public MutableLootTable(LootTable table, ResourceLocation id)
    {
        this.id = id;
        List<LootPool> immutablePools = ((LootTableAccessors) table).getPools();
        this.pools = new LinkedHashMap<>(immutablePools.size());
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

    public MutableLootTable(ResourceLocation id, Map<String, MutableLootPool> pools)
    {
        this.id = id;
        this.pools = pools;
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
        Map<String, MutableLootPool> poolsDeepClone = pools.entrySet()
            .stream()
            .collect(toMap(Map.Entry::getKey, e -> e.getValue().deepClone(), mergeFunction, HashMap::new));
        return new MutableLootTable(id, poolsDeepClone);
    }

    public LootTable toImmutable()
    {
        LootPool[] poolsArray = pools.values().stream().map(MutableLootPool::toImmutable).toArray(LootPool[]::new);
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
        if (pools.putIfAbsent(pool.getName(), pool) != null) throw new IllegalArgumentException(
            String.format("Duplicate pool name '%s' in table '%s'", pool.getName(), id));
    }

    public MutableLootPool removePool(String name)
    {
        return pools.remove(name);
    }

    public void clearPools()
    {
        pools.clear();
    }
}
