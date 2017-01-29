package leviathan143.loottweaker.common.zenscript;

import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import net.minecraft.world.storage.loot.functions.LootFunction;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass(Constants.MODID + ".vanilla.loot.LootFunction")
public class ZenLootFunctionWrapper
{
    public final LootFunction function;
    
    public ZenLootFunctionWrapper(LootFunction function)
    {
	this.function = function;
    }
}
