package leviathan143.loottweaker.common.lib;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import leviathan143.loottweaker.common.darkmagic.LootEntryAccessors;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.conditions.LootCondition;


/**
 * @author Daomephsta A loot entry serialiser that "serialises" non-vanilla loot
 *         entries type in a best effort manner intended to provide users with
 *         info, falling back to vanilla for vanilla types.
 */
public class RobustLootEntrySerialiser implements JsonSerializer<LootEntry>
{
    private final JsonSerializer<LootEntry> vanilla = new LootEntry.Serializer();

    @Override
    public JsonElement serialize(LootEntry value, Type type, JsonSerializationContext context)
    {
        if (value instanceof LootEntryItem || value instanceof LootEntryTable || value instanceof LootEntryEmpty)
            return vanilla.serialize(patch(value), type, context);

        JsonObject json = new JsonObject();
        if (value.getEntryName() != null) json.addProperty("entryName", value.getEntryName());
        json.addProperty("weight", LootEntryAccessors.getWeight(value));
        json.addProperty("quality", LootEntryAccessors.getQuality(value));
        json.addProperty("type", "loottweaker:best_effort");
        LootCondition[] conditions = LootEntryAccessors.getConditions(value);
        if (conditions.length > 0) json.add("conditions", context.serialize(conditions));
        json.addProperty("_comment", "A best effort serialisation of a non-serialisable entry");
        json.addProperty("class", value.getClass().getName());
        return json;
    }

    /** Mutates an entry to avoid bugs in the vanilla serialiser **/
    private LootEntry patch(LootEntry entry)
    {
        if (LootEntryAccessors.getConditionsUnsafe(entry) == null)
            LootEntryAccessors.setConditions(entry, LootConditions.NONE);
        return entry;
    }
}
