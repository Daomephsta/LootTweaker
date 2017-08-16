package leviathan143.loottweaker.common.zenscript;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import net.minecraft.world.storage.loot.functions.LootFunction;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass(Constants.MODID + ".vanilla.loot.LootFunction")
public class ZenLootFunctionWrapper
{
    public final LootFunction function;
    
    public ZenLootFunctionWrapper(LootFunction function)
    {
	this.function = function;
    }
}
