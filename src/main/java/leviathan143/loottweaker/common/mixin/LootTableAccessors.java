package leviathan143.loottweaker.common.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;


@Mixin(LootTable.class)
public interface LootTableAccessors
{
    @Accessor
    public List<LootPool> getPools();
}
