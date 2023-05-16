package leviathan143.loottweaker.common.lib;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import leviathan143.loottweaker.common.mixin.LootEntryItemAccessors;
import leviathan143.loottweaker.common.mixin.LootTableManagerAccessors;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.functions.LootFunction;


public class LootFunctions
{
    public static final LootFunction[] NONE = {};

    public static List<LootFunction> deepClone(List<LootFunction> functions)
    {
        List<LootFunction> clone = new ArrayList<>(functions.size());
        for (int i = 0; i < functions.size(); i++)
            clone.set(i, LootFunctions.deepClone(functions.get(i)));
        return clone;
    }

    public static LootFunction deepClone(LootFunction lootFunction)
    {
        Gson lootTableGson = LootTableManagerAccessors.getGsonInstance();
        JsonElement json = lootTableGson.toJsonTree(lootFunction);
        return lootTableGson.fromJson(json, LootFunction.class);
    }

    public static LootFunction[] get(LootEntryItem entry)
    {
        LootFunction[] functions = ((LootEntryItemAccessors) entry).getFunctionsUnsafe();
        return functions != null
            ? functions
            : LootFunctions.NONE;
    }
}
