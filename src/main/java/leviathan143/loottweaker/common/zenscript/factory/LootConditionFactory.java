package leviathan143.loottweaker.common.zenscript.factory;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.IDataParser;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".vanilla.loot.Conditions")
public class LootConditionFactory
{
    private static final IDataParser LOGGING_PARSER = new IDataParser(LootTableManagerAccessors.getGsonInstance(), e -> CraftTweakerAPI.logError(e.getMessage()));
    
	@ZenMethod
	public static ZenLootConditionWrapper randomChance(float chance)
	{
	    return new ZenLootConditionWrapper(new RandomChance(chance));
	}

	@ZenMethod
	public static ZenLootConditionWrapper randomChanceWithLooting(float chance, float lootingMultiplier)
	{
	    return new ZenLootConditionWrapper(new RandomChanceWithLooting(chance, lootingMultiplier));
	}

	@ZenMethod
	public static ZenLootConditionWrapper killedByPlayer()
	{
	    return new ZenLootConditionWrapper(new KilledByPlayer(false));
	}

	@ZenMethod
	public static ZenLootConditionWrapper killedByNonPlayer()
	{
	    return new ZenLootConditionWrapper(new KilledByPlayer(true));
	}

	@ZenMethod
	public static ZenLootConditionWrapper parse(IData json)
	{
	    return LOGGING_PARSER.parse(json, LootCondition.class).map(ZenLootConditionWrapper::new).orElse(ZenLootConditionWrapper.INVALID);
	}
}
