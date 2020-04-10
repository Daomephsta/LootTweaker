package leviathan143.loottweaker.common.mutable_loot;

import static java.util.stream.Collectors.toMap;

import java.util.Map;

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
        this.pools = LootTableAccessors.getPools(table).stream()
            .map(MutableLootPool::new)
            .collect(toMap(MutableLootPool::getName, Functions.identity()));
    }

    private MutableLootTable(ResourceLocation id, Map<String, MutableLootPool> pools)
    {
        this.id = id;
        this.pools = pools;
    }

    @Override
    public MutableLootTable deepClone()
    {
        Map<String, MutableLootPool> poolsDeepClone = pools.entrySet().stream()
            .collect(toMap(Map.Entry::getKey, e -> e.getValue().deepClone()));
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
        pools.put(pool.getName(), pool);
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
