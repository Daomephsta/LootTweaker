package leviathan143.loottweaker.common.lib;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import leviathan143.loottweaker.common.mixin.LootEntryAccessors;
import leviathan143.loottweaker.common.mixin.LootTableManagerAccessors;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;


public class LootConditions
{
    public static final LootCondition[] NONE = {};

    public static List<LootCondition> deepClone(List<LootCondition> conditions)
    {
        List<LootCondition> clone = new ArrayList<>(conditions.size());
        for (int i = 0; i < conditions.size(); i++)
            clone.set(i, LootConditions.deepClone(conditions.get(i)));
        return clone;
    }

    public static LootCondition deepClone(LootCondition lootCondition)
    {
        Gson lootTableGson = LootTableManagerAccessors.getGsonInstance();
        JsonElement json = lootTableGson.toJsonTree(lootCondition);
        return lootTableGson.fromJson(json, LootCondition.class);
    }

    public static LootCondition[] get(LootEntry entry)
    {
        LootCondition[] conditions = ((LootEntryAccessors) entry).getConditionsUnsafe();
        return conditions != null
            ? conditions
            : LootConditions.NONE;
    }
}
