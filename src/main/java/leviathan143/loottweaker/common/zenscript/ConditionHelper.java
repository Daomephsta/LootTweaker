package leviathan143.loottweaker.common.zenscript;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.DeprecationWarningManager;
import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import leviathan143.loottweaker.common.lib.*;
import net.minecraft.world.storage.loot.conditions.*;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(Constants.MODID + ".vanilla.loot.Conditions")
public class ConditionHelper
{
	@ZenMethod
	public static ZenLootConditionWrapper randomChance(float chance)
	{
		return new ZenLootConditionWrapper(new RandomChance(chance));
	}

	@ZenMethod
	public static ZenLootConditionWrapper randomChanceWithLooting(float chance, float lootingMult)
	{
		return new ZenLootConditionWrapper(new RandomChanceWithLooting(chance, lootingMult));
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
		if(!ZenScriptUtils.checkIsMap(json)) return null;
		return new ZenLootConditionWrapper(LootUtils.parseJSONCondition(DataToJSONConverter.from(json)));
	}
	
	@ZenMethod
	@Deprecated
	public static ZenLootConditionWrapper parse(String json)
	{
		DeprecationWarningManager.addWarning();
		return new ZenLootConditionWrapper(LootUtils.parseJSONCondition("{" + json + "}"));
	}
}
