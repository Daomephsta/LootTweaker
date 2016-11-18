package leviathan143.droptweaker.common.tweakers;

import leviathan143.droptweaker.common.tweakers.loot.LootTableTweaker;
import leviathan143.droptweaker.common.zenscript.ZenLootPoolWrapper;
import leviathan143.droptweaker.common.zenscript.ZenLootTableWrapper;
import minetweaker.MineTweakerAPI;

public class RegisterTweakers 
{
	public static void register() 
	{
		MineTweakerAPI.registerClass(LootTableTweaker.class);
		MineTweakerAPI.registerClass(ZenLootTableWrapper.class);
		MineTweakerAPI.registerClass(ZenLootPoolWrapper.class);
		MineTweakerAPI.registerClass(EventDropsTweaker.class);
		LootTableTweaker.onRegister();
		EventDropsTweaker.onRegister();
	}
}
