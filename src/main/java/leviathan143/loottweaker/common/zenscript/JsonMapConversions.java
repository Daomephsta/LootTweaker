package leviathan143.loottweaker.common.zenscript;

import java.util.Map;
import java.util.Optional;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.Arguments;
import leviathan143.loottweaker.common.lib.JsonConverter;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootFunctionWrapper;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import stanhebben.zenscript.annotations.ZenCaster;
import stanhebben.zenscript.annotations.ZenExpansion;

@ZenRegister
@ZenExpansion("any[any]")
public class JsonMapConversions
{
    public static Impl IMPLEMENTATION = new Impl(LootTweaker.CONTEXT);

    @ZenCaster
    public static ZenLootConditionWrapper asLootCondition(Map<String, IData> json)
    {
        return IMPLEMENTATION.asLootCondition(json);
    }

    @ZenCaster
    public static ZenLootFunctionWrapper asLootFunction(Map<String, IData> json)
    {
        return IMPLEMENTATION.asLootFunction(json);
    }

    @VisibleForTesting
    public static class Impl
    {
        private final LootTweakerContext context;
        private final Gson lootDeserialiser = LootTableManagerAccessors.getGsonInstance(),
                           jsonElementSerialiser = new GsonBuilder()
                               .registerTypeHierarchyAdapter(IData.class,
                                   (JsonSerializer<IData>) (src, type, context) -> JsonConverter.from(src))
                               .create();

        public Impl(LootTweakerContext context)
        {
            this.context = context;
        }

        @VisibleForTesting
        public ZenLootConditionWrapper asLootCondition(Map<String, ?> json)
        {
            return parse(json, LootCondition.class)
                .map(ZenLootConditionWrapper::new)
                .orElse(ZenLootConditionWrapper.INVALID);
        }

        @VisibleForTesting
        public ZenLootFunctionWrapper asLootFunction(Map<String, ?> json)
        {
            return parse(json, LootFunction.class)
                .map(fn -> new ZenLootFunctionWrapper(fn, context))
                .orElse(ZenLootFunctionWrapper.INVALID);
        }

        private <T> Optional<T> parse(Map<String, ?> data, Class<T> clazz)
        {
            if (!Arguments.nonNull(context.getErrorHandler(), "json", data))
                return Optional.empty();
            try
            {
                return Optional.of(lootDeserialiser.fromJson(jsonElementSerialiser.toJsonTree(data), clazz));
            }
            catch (JsonSyntaxException e)
            {
                context.getErrorHandler().error(e.getMessage());
                return Optional.empty();
            }
        }
    }
}
