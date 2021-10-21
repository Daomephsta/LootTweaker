package leviathan143.loottweaker.common.zenscript.wrapper;

import java.util.Arrays;
import java.util.stream.Stream;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootFunctionAccessors;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.Arguments;
import leviathan143.loottweaker.common.lib.DataParser;
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
    private final DataParser loggingParser;

	public ZenLootFunctionWrapper(LootFunction function, LootTweakerContext context)
	{
		this.function = function;
        this.context = context;
        this.loggingParser = new DataParser(LootTableManagerAccessors.getGsonInstance(), 
            e -> context.getErrorHandler().error(e.getMessage()));
	}
	
	@ZenMethod
	public ZenLootFunctionWrapper addConditionsHelper(ZenLootConditionWrapper[] conditions)
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
    
    @ZenMethod
    public ZenLootFunctionWrapper addConditionsJson(IData[] conditionsJson)
    {

        // Will be null for INVALID
        if (function == null)
            return this;
        if (!Arguments.nonNull(context.getErrorHandler(), "conditions", conditionsJson)) 
            return INVALID;
        Stream<LootCondition> parsedConditions = Arrays.stream(conditionsJson)
            .map(c -> loggingParser.parse(c, LootCondition.class))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get);
        LootFunctionAccessors.setConditions(function, 
            Stream.concat(Arrays.stream(function.getConditions()), parsedConditions)
                .toArray(LootCondition[]::new));
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
