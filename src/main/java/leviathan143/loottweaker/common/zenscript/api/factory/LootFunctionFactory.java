package leviathan143.loottweaker.common.zenscript.api.factory;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.api.entry.LootFunctionRepresentation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootFunctionFactory")
public interface LootFunctionFactory
{
    @ZenMethod
    public LootFunctionRepresentation enchantRandomly(String[] enchantIDList);

    @ZenMethod
    public LootFunctionRepresentation enchantWithLevels(int min, int max, boolean isTreasure);

    @ZenMethod
    public LootFunctionRepresentation lootingEnchantBonus(int min, int max, int limit);

    @ZenMethod
    public LootFunctionRepresentation setCount(int min, int max);

    @ZenMethod
    public LootFunctionRepresentation setDamage(float min, float max);

    @ZenMethod
    public LootFunctionRepresentation setMetadata(int min, int max);

    @ZenMethod
    public LootFunctionRepresentation setNBT(IData nbtData);

    @ZenMethod
    public LootFunctionRepresentation smelt();
}
