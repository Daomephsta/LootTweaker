package leviathan143.loottweaker.common.mutable_loot;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

import javax.annotation.Nullable;

import com.google.common.base.Functions;

import leviathan143.loottweaker.common.darkmagic.LootTableAccessors;
import leviathan143.loottweaker.common.lib.DeepClone;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;

public class MutableLootTable implements DeepClone<MutableLootTable>
{
    private final ResourceLocation id;
    private Map<String, MutableLootPool> pools;

    public MutableLootTable(LootTable table, ResourceLocation id)
    {
        this.id = id;
        //Can never be duplicate entries when pulling from an existing table, but be informative just in case
        BinaryOperator<MutableLootPool> mergeFunction = (a, b) ->
        {
            throw new IllegalStateException(String.format(
                "Unexpected duplicate pool '%s' while creating mutable table '%s' from immutable table. Report this to the mod author",
                a.getName(), id));
        };
        this.pools = LootTableAccessors.getPools(table).stream()
            .map(MutableLootPool::new)
            .collect(toMap(MutableLootPool::getName, Functions.identity(), mergeFunction, HashMap::new));
    }

    public MutableLootTable(ResourceLocation id, Map<String, MutableLootPool> pools)
    {
        this.id = id;
        this.pools = pools;
    }

    @Override
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
        return new MutableLootTable(id, poolsDeepClone);
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

    public MutableLootPool removePool(String name)
    {
        return pools.remove(name);
    }

    public void clearPools()
    {
        pools.clear();
    }
}
