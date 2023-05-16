package leviathan143.loottweaker.common.lib;

import java.lang.reflect.Type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.mixin.LootFunctionManagerAccessors;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;


public class RobustLootFunctionSerialiser implements JsonSerializer<LootFunction>
{
    private static final Logger SANITY_LOGGER = LogManager.getLogger(LootTweaker.MODID + ".sanity_checks");
    private final JsonSerializer<LootFunction> vanilla = new LootFunctionManager.Serializer();

    @Override
    public JsonElement serialize(LootFunction function, Type type, JsonSerializationContext context)
    {
        if (checkSerialisable(function)) return vanilla.serialize(function, type, context);

        JsonObject json = new JsonObject();
        json.addProperty("_comment", "A best effort serialisation of a non-serialisable loot function");
        json.addProperty("class", function.getClass().getName());
        if (function.getConditions() != null && function.getConditions().length > 0)
            json.add("conditions", context.serialize(function.getConditions()));
        return json;
    }

    private boolean checkSerialisable(LootFunction function)
    {
        if (LootFunctionManagerAccessors.getClassToSerialiserMap().containsKey(function.getClass()))
        {
            return true;
        }
        else
        {
            SANITY_LOGGER.error("No serialiser registered for loot function {}", function.getClass().getName());
            return false;
        }
    }
}
