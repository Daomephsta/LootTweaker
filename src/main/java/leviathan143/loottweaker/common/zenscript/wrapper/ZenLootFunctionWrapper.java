package leviathan143.loottweaker.common.zenscript.wrapper;

import java.util.Arrays;
import java.util.stream.Stream;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootFunctionAccessors;
import leviathan143.loottweaker.common.lib.Arguments;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".vanilla.loot.LootFunction")
public class ZenLootFunctionWrapper
{
    public static final ZenLootFunctionWrapper INVALID = new ZenLootFunctionWrapper(null, null);
    
	public final LootFunction function;
    private final LootTweakerContext context;

	public ZenLootFunctionWrapper(LootFunction function, LootTweakerContext context)
	{
		this.function = function;
        this.context = context;
	}
	
	@ZenMethod
	public ZenLootFunctionWrapper addConditions(ZenLootConditionWrapper[] conditions)
	{
	    // Will be null for INVALID
	    if (function == null)
	        return this;
	    if (!Arguments.nonNull(context.getErrorHandler(), "conditions", conditions)) 
	        return INVALID;
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
