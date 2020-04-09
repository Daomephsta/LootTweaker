package leviathan143.loottweaker.common.mutable_loot;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import leviathan143.loottweaker.common.darkmagic.LootTableAccessors;
import leviathan143.loottweaker.common.lib.DeepClone;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;

public class MutableLootTable implements DeepClone<MutableLootTable>
{
    private final ResourceLocation id;
    private List<MutableLootPool> pools = new ArrayList<>();

    public MutableLootTable(LootTable table, ResourceLocation id)
    {
        this.id = id;
        this.pools = LootTableAccessors.getPools(table).stream()
            .map(MutableLootPool::new)
            .collect(toList());
    }

    private MutableLootTable(ResourceLocation id, List<MutableLootPool> pools)
    {
        this.id = id;
        this.pools = pools;
    }

    @Override
    public MutableLootTable deepClone()
    {
        List<MutableLootPool> poolsDeepClone = pools.stream()
            .map(DeepClone::deepClone)
            .collect(toList());
        return new MutableLootTable(id, poolsDeepClone);
    }

    public LootTable toImmutable()
    {
        LootPool[] poolsArray = pools.stream()
            .map(MutableLootPool::toImmutable)
            .toArray(LootPool[]::new);
        return new LootTable(poolsArray);
    }

    public ResourceLocation getId()
    {
        return id;
    }

    public List<MutableLootPool> getPools()
    {
        return pools;
    }

    public void addPool(MutableLootPool pool)
    {
        pools.add(pool);
    }
}
