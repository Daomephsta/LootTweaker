package leviathan143.loottweaker.common.lib;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import leviathan143.loottweaker.common.accessors.LootEntryAccessors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.conditions.LootCondition;


/**
 * A loot entry serialiser that "serialises" non-vanilla loot
 * entries type in a best effort manner intended to provide users with
 * info, falling back to vanilla for vanilla types.
 * @author Daomephsta
 */
public class RobustLootEntrySerialiser implements JsonSerializer<LootEntry>
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final JsonSerializer<LootEntry> vanilla = new LootEntry.Serializer();

    @Override
    public JsonElement serialize(LootEntry entry, Type type, JsonSerializationContext context)
    {
        if (entry instanceof LootEntryItem || entry instanceof LootEntryTable || entry instanceof LootEntryEmpty)
            return vanilla.serialize(patch(entry), type, context);

        JsonObject json = new JsonObject();
        json.addProperty("entryName", entry.getEntryName());
        json.addProperty("weight", LootEntryAccessors.getWeight(entry));
        json.addProperty("quality", LootEntryAccessors.getQuality(entry));
        json.addProperty("type", "loottweaker:best_effort");
        LootCondition[] conditions = LootEntryAccessors.getConditions(entry);
        if (conditions.length > 0) json.add("conditions", context.serialize(conditions));
        JsonObject bestEffort = new JsonObject();
        if (trySerialise(entry, json, context))
            bestEffort.addProperty("comment", "A best effort serialisation of a mod-added serialisable entry type");
        else
        {
            LootEntryFields.forClass(entry.getClass()).serialise(bestEffort, entry);
            bestEffort.addProperty("comment", "A best effort serialisation of a mod-added non-serialisable entry type");
        }
        bestEffort.addProperty("class", entry.getClass().getName());
        json.add("loottweaker:best_effort", bestEffort);
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

    /**
     * Cache for interesting fields of loot entry types
     * @author Daomephsta
     */
    private static class LootEntryFields
    {
        private static final Map<Class<?>, LootEntryFields> KNOWN_ENTRY_TYPES = new HashMap<>();
        private static final Set<Class<?>> PRIMITIVE_NUMBER_CLASSES =
            Sets.newHashSet(byte.class, short.class, int.class, long.class, float.class, double.class);
        private final List<Field> itemStackFields, itemFields, numberFields;

        private LootEntryFields(Class<? extends LootEntry> lootEntryClass)
        {
            this.itemStackFields = new ArrayList<>();
            this.itemFields = new ArrayList<>();
            this.numberFields = new ArrayList<>();
            Field[] allFields = lootEntryClass.getDeclaredFields();
            // Make everything accessible since most of it is of interest anyway
            Field.setAccessible(allFields, true);
            for (Field field : allFields)
            {
                if (Modifier.isStatic(field.getModifiers()))
                    continue;
                if (field.getType() == ItemStack.class)
                    itemStackFields.add(field);
                else if (Item.class.isAssignableFrom(field.getType()))
                    itemFields.add(field);
                else if (PRIMITIVE_NUMBER_CLASSES.contains(field.getType()))
                    numberFields.add(field);
            }
        }

        static LootEntryFields forClass(Class<? extends LootEntry> lootEntryClass)
        {
            return KNOWN_ENTRY_TYPES.computeIfAbsent(lootEntryClass, k -> new LootEntryFields(lootEntryClass));
        }

        void serialise(JsonObject json, LootEntry entry)
        {
            if (itemStackFields.isEmpty() && itemFields.isEmpty())
                return;
            JsonObject fields = new JsonObject();
            for (Field field : itemStackFields)
            {
                try
                {
                    JsonObject fieldRepr = new JsonObject();
                    ItemStack stack = (ItemStack) field.get(entry);
                    fieldRepr.addProperty("item", stack.getItem().getRegistryName().toString());
                    if (stack.getCount() != 1)
                        fieldRepr.addProperty("count", stack.getCount());
                    if (stack.getMetadata() != 0)
                        fieldRepr.addProperty("metadata", stack.getMetadata());
                    if (stack.hasTagCompound())
                        fieldRepr.addProperty("nbt", stack.getTagCompound().toString());
                    fields.add(field.getName(), fieldRepr);
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    LOGGER.error("Could not access loot entry's itemstack field", e);
                }
            }
            for (Field field : itemFields)
            {
                try
                {
                    Item item = (Item) field.get(entry);
                    fields.addProperty(field.getName(), item.getRegistryName().toString());
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    LOGGER.error("Could not access loot entry's item field", e);
                }
            }
            for (Field field : numberFields)
            {
                try
                {
                    fields.addProperty(field.getName(), (Number) field.get(entry));
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    LOGGER.error("Could not access loot entry field", e);
                }
            }
            json.add("fields", fields);
        }
    }
}
