package leviathan143.loottweaker.common.lib;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.world.storage.loot.*;

/**
 * @author Daomephsta
 * Fixes the following bugs in the vanilla serialiser
 * <ul>
 * <li>Bonus rolls are not serialised unless both min and max are non-zero</li>
 * </ul>
 */
public class RobustLootPoolSerialiser implements JsonSerializer<LootPool>
{
    private final JsonSerializer<LootPool> vanilla = new LootPool.Serializer();

    @Override
    public JsonElement serialize(LootPool value, Type type, JsonSerializationContext context)
    {
        JsonObject json = (JsonObject) vanilla.serialize(value, type, context);
        if (value.getBonusRolls().getMin() != 0 || value.getBonusRolls().getMax() != 0)
            json.add("bonus_rolls", context.serialize(value.getBonusRolls()));
        return json;
    }
}
