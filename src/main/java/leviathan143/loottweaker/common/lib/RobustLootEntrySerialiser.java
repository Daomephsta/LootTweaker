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
    public JsonElement serialize(LootEntry entry, Type type, JsonSerializationContext context)
    {
        if (entry instanceof LootEntryItem || entry instanceof LootEntryTable || entry instanceof LootEntryEmpty)
            return vanilla.serialize(patch(entry), type, context);

        JsonObject json = new JsonObject();
        json.addProperty("weight", LootEntryAccessors.getWeight(entry));
        json.addProperty("quality", LootEntryAccessors.getQuality(entry));
        json.addProperty("type", "loottweaker:best_effort");
        LootCondition[] conditions = LootEntryAccessors.getConditions(entry);
        if (conditions.length > 0) json.add("conditions", context.serialize(conditions));
        if (trySerialise(entry, json, context))
            json.addProperty("loottweaker:comment", "A best effort serialisation of a mod-added serialisable entry type");
        else
            json.addProperty("loottweaker:comment", "A best effort serialisation of a mod-added non-serialisable entry type");
        json.addProperty("loottweaker:class", entry.getClass().getName());
        return json;
    }

    private boolean trySerialise(LootEntry entry, JsonObject json, JsonSerializationContext context)
    {
        int before = json.size();
        // Attempt serialisation with LootEntry.serialize
        LootEntryAccessors.serialise(entry, json, context);
        return json.size() != before;
    }

    /** Mutates an entry to avoid bugs in the vanilla serialiser **/
    private LootEntry patch(LootEntry entry)
    {
        if (LootEntryAccessors.getConditionsUnsafe(entry) == null)
            LootEntryAccessors.setConditions(entry, LootConditions.NONE);
        return entry;
    }
}
