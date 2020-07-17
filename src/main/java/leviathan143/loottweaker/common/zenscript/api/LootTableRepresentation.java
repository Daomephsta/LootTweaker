package leviathan143.loottweaker.common.zenscript.api;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import stanhebben.zenscript.annotations.IterableSimple;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootTable")
@IterableSimple(LootTweaker.MODID + ".RemovableLootPool")
public interface LootTableRepresentation
{
    @ZenMethod
    public void removePool(String poolId);
}
