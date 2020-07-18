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
    public LootPoolRepresentation getPool(String poolId);

    @ZenMethod
    public LootPoolRepresentation addPool(String poolId, float minRolls, float maxRolls);

    @ZenMethod
    public LootPoolRepresentation addPool(String poolId, float minRolls, float maxRolls, float minBonusRolls, float maxBonusRolls);

    @ZenMethod
    public void removePool(String poolId);

    @ZenMethod
    public void removeAllPools();
}
