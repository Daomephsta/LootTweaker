package leviathan143.loottweaker.common.zenscript.wrapper;

import java.util.Arrays;
import java.util.stream.Stream;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootFunctionAccessors;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

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
	
	@ZenMethod
	public ZenLootFunctionWrapper addConditions(ZenLootConditionWrapper[] conditions)
	{
	    LootFunctionAccessors.setConditions(function, Stream.concat(
	        Arrays.stream(function.getConditions()), 
	        Arrays.stream(conditions)
	            .filter(ZenLootConditionWrapper::isValid)
	            .map(ZenLootConditionWrapper::unwrap)
	    ).toArray(LootCondition[]::new));
	    return this;
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
