package leviathan143.loottweaker.common.zenscript.api.entry;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import net.minecraft.world.storage.loot.functions.LootFunction;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootFunction")
public interface LootFunctionRepresentation
{
    public boolean isValid();

    public LootFunction toImmutable();
}
