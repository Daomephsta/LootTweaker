package leviathan143.loottweaker.common.zenscript.api;

import java.util.Iterator;

import com.google.common.base.Supplier;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.Describable;
import leviathan143.loottweaker.common.zenscript.api.iteration.LootIterator;
import stanhebben.zenscript.annotations.IterableSimple;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootTable")
public interface LootTableRepresentation extends Describable
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

    @ZenRegister
    @ZenClass(LootTweaker.MODID + ".PoolsIterator")
    @IterableSimple(LootTweaker.MODID + ".LootPool")
    public static class PoolsIterator<P extends LootPoolRepresentation> extends LootIterator<P, LootPoolRepresentation>
    {
        public PoolsIterator(Iterator<P> delegate, ErrorHandler errorHandler, Supplier<String> message)
        {
            super(delegate, errorHandler, message);
        }
    }

    @ZenMethod
    public PoolsIterator<? extends LootPoolRepresentation> poolsIterator();
}
