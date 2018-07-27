package leviathan143.loottweaker.common.zenscript;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass(Constants.MODID + ".vanilla.loot.ModFunctions")
public class ModFunctionHelper 
{
	public static final ModFunctionHelper INSTANCE = new ModFunctionHelper();
}
