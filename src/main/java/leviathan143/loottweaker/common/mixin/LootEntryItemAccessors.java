package leviathan143.loottweaker.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.functions.LootFunction;


@Mixin(LootEntryItem.class)
public interface LootEntryItemAccessors extends LootEntryAccessors
{
    @Accessor
    public Item getItem();

    @Accessor("functions")
    public LootFunction[] getFunctionsUnsafe();
}
