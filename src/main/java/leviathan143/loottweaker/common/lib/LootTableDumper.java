package leviathan143.loottweaker.common.lib;

import java.io.File;
import java.io.FileWriter;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;


public class LootTableDumper
{
    private static final Gson ROBUST_GSON = new GsonBuilder()
        .registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
        .registerTypeAdapter(LootPool.class, new RobustLootPoolSerialiser())
        .registerTypeAdapter(LootTable.class, new LootTable.Serializer())
        .registerTypeHierarchyAdapter(LootEntry.class, new RobustLootEntrySerialiser())
        .registerTypeHierarchyAdapter(LootFunction.class, new RobustLootFunctionSerialiser())
        .registerTypeHierarchyAdapter(LootCondition.class, new RobustLootConditionSerialiser())
        .registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer())
        .create();
    public static final LootTableDumper DEFAULT = new LootTableDumper(new File("dumps/loot_tables"), ROBUST_GSON);
    private static final Logger LOGGER = LogManager.getLogger();

    private final File dumpFolder;
    private final Gson gsonInstance;

    public LootTableDumper(File dumpFolder, Gson gsonInstance)
    {
        assert dumpFolder.isDirectory() : "Dump folder must be a directory";
        this.dumpFolder = dumpFolder;
        this.dumpFolder.mkdirs();
        this.gsonInstance = gsonInstance;
    }

    public static LootTableDumper robust(File dumpFolder)
    {
        return new LootTableDumper(dumpFolder, ROBUST_GSON);
    }

    public File getFolder()
    {
        return dumpFolder;
    }

    public File dump(World world, ResourceLocation tableId)
    {
        return dump(world.getLootTableManager().getLootTableFromLocation(tableId), tableId);
    }

    public File dump(LootTable lootTable, ResourceLocation tableId)
    {
        Preconditions.checkNotNull(lootTable);
        
        File dump = new File(dumpFolder, tableId.getNamespace() + '/' + tableId.getPath() + ".json");
        try
        {
            dump.getParentFile().mkdirs();
            dump.createNewFile();
            try (FileWriter writer = new FileWriter(dump))
            {
                JsonWriter dumper = gsonInstance.newJsonWriter(writer);
                dumper.setIndent("  ");
                JsonObject json = (JsonObject) gsonInstance.toJsonTree(lootTable);
                json = withInfo(json, lootTable, tableId);
                gsonInstance.toJson(json, dumper);
            }
            LOGGER.info("Loot table {} saved to {}", tableId, dump.getCanonicalPath());
            return dump;
        }
        catch (Throwable t)
        {
            LOGGER.warn("Failed to dump loot table {}", tableId, t);
        }
        return null;
    }

    private JsonObject withInfo(JsonObject old, LootTable lootTable, ResourceLocation tableId)
    {
        JsonObject info = new JsonObject();
        info.addProperty("id", tableId.toString());
        // Put info at the top for ease of access
        JsonObject json = new JsonObject();
        json.add("loottweaker:dump_info", info);
        for (Entry<String, JsonElement> entry : old.entrySet())
            json.add(entry.getKey(), entry.getValue());
        return json;
    }
}
