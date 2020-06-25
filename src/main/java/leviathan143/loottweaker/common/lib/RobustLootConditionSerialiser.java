package leviathan143.loottweaker.common.lib;

import java.lang.reflect.Type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootConditionManagerAccessors;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;

public class RobustLootConditionSerialiser implements JsonSerializer<LootCondition>
{
    private static final Logger SANITY_LOGGER = LogManager.getLogger(LootTweaker.MODID + ".sanity_checks");
    private final JsonSerializer<LootCondition> vanilla = new LootConditionManager.Serializer();

    @Override
    public JsonElement serialize(LootCondition condition, Type type, JsonSerializationContext context)
    {
        if (checkSerialisable(condition))
            return vanilla.serialize(condition, type, context);

        JsonObject json = new JsonObject();
        json.addProperty("_comment", "A best effort serialisation of a non-serialisable loot condition");
        json.addProperty("class", condition.getClass().getName());
        return json;
    }

    private boolean checkSerialisable(LootCondition condition)
    {
        if (LootConditionManagerAccessors.getClassToSerialiserMap().containsKey(condition.getClass()))
        {
            return true;
        }
        else
        {
            SANITY_LOGGER.error("No serialiser registered for loot condition {}", condition.getClass().getName());
            return false;
        }
    }
}
