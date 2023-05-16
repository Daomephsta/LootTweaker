package leviathan143.loottweaker.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryTable;


@Mixin(LootEntryTable.class)
public interface LootEntryTableAccessors extends LootEntryAccessors
{
    @Accessor
    public ResourceLocation getTable();
}
