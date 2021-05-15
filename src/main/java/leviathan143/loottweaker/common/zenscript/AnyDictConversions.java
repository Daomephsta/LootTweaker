package leviathan143.loottweaker.common.zenscript;

import java.util.Map;
import java.util.Optional;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootFunctionWrapper;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import stanhebben.zenscript.annotations.ZenCaster;
import stanhebben.zenscript.annotations.ZenExpansion;

@ZenRegister
@ZenExpansion("any[any]")
public class AnyDictConversions
{
    private static Impl IMPLEMENTATION = new Impl(LootTweaker.CONTEXT);

    @ZenCaster
    public static ZenLootConditionWrapper asLootCondition(Map<String, Object> data)
    {
        return IMPLEMENTATION.asLootCondition(data);
    }

    @ZenCaster
    public static ZenLootFunctionWrapper asLootFunction(Map<String, Object> data)
    {
        return IMPLEMENTATION.asLootFunction(data);
    }

    @VisibleForTesting
    public static class Impl
    {
        private final LootTweakerContext context;
        private final Gson gson = LootTableManagerAccessors.getGsonInstance();

        public Impl(LootTweakerContext context)
        {
            this.context = context;
        }

        @VisibleForTesting
        public ZenLootConditionWrapper asLootCondition(Map<String, Object> json)
        {
            return parse(json, LootCondition.class)
                .map(ZenLootConditionWrapper::new)
                .orElse(ZenLootConditionWrapper.INVALID);
        }

        @VisibleForTesting
        public ZenLootFunctionWrapper asLootFunction(Map<String, Object> json)
        {
            return parse(json, LootFunction.class)
                .map(ZenLootFunctionWrapper::new)
                .orElse(ZenLootFunctionWrapper.INVALID);
        }

        private <T> Optional<T> parse(Map<String, Object> data, Class<T> clazz)
        {
            try
            {
                return Optional.of(gson.fromJson(gson.toJsonTree(data), clazz));
            }
            catch (JsonSyntaxException e)
            {
                context.getErrorHandler().error(e.getMessage());
                return Optional.empty();
            }
        }
    }
}
