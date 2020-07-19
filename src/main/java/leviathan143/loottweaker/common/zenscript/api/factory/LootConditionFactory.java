package leviathan143.loottweaker.common.zenscript.api.factory;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.api.entry.LootConditionRepresentation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootConditionFactory")
public interface LootConditionFactory
{
    @ZenMethod
    public LootConditionRepresentation randomChance(float chance);

    @ZenMethod
    public LootConditionRepresentation randomChanceWithLooting(float chance, float lootingMultiplier);

    @ZenMethod
    public LootConditionRepresentation killedByPlayer();

    @ZenMethod
    public LootConditionRepresentation killedByNonPlayer();

    @ZenMethod
    public LootConditionRepresentation parse(IData json);
}
