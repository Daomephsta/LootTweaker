package leviathan143.loottweaker.common.zenscript.wrapper;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import net.minecraft.world.storage.loot.functions.LootFunction;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".vanilla.loot.LootFunction")
public class ZenLootFunctionWrapper
{
    public static final ZenLootFunctionWrapper INVALID = new ZenLootFunctionWrapper(null);
    
	public final LootFunction function;

	public ZenLootFunctionWrapper(LootFunction function)
	{
		this.function = function;
	}
	
	public LootFunction unwrap()
    {
        return function;
    }
	
	public boolean isValid()
	{
	    return function != null;
	}
}
