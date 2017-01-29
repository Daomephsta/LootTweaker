package leviathan143.loottweaker.common.tweakers;

import leviathan143.loottweaker.common.tweakers.loot.LootTableTweaker;
import leviathan143.loottweaker.common.zenscript.*;
import minetweaker.MineTweakerAPI;

public class RegisterZenscript 
{
	public static void register() 
	{
		MineTweakerAPI.registerClass(LootTableTweaker.class);
		MineTweakerAPI.registerClass(EventDropsTweaker.class);
		LootTableTweaker.onRegister();
		EventDropsTweaker.onRegister();
		
		MineTweakerAPI.registerClass(ZenLootTableWrapper.class);
		MineTweakerAPI.registerClass(ZenLootPoolWrapper.class);
		MineTweakerAPI.registerClass(ZenLootConditionWrapper.class);
		MineTweakerAPI.registerClass(ZenLootFunctionWrapper.class);
		
		MineTweakerAPI.registerClass(ConditionHelper.class);
		MineTweakerAPI.registerClass(FunctionHelper.class);
	}
}
