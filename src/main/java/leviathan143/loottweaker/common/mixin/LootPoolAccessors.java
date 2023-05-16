package leviathan143.loottweaker.common.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootCondition;


@Mixin(LootPool.class)
public interface LootPoolAccessors
{
    @Accessor("poolConditions")
    public List<LootCondition> getConditions();

    @Accessor("lootEntries")
    public List<LootEntry> getEntries();
}
