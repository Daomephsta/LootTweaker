package leviathan143.loottweaker.common.lib;

import java.io.File;
import java.io.FileWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;


public class LootTableDumper
{
    public static final LootTableDumper DEFAULT = new LootTableDumper(new File("dumps/loot_tables"),
        new GsonBuilder().registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
            .registerTypeAdapter(LootPool.class, new LootPool.Serializer())
            .registerTypeAdapter(LootTable.class, new LootTable.Serializer())
            .registerTypeHierarchyAdapter(LootEntry.class, new RobustLootEntrySerialiser())
            .registerTypeHierarchyAdapter(LootFunction.class, new RobustLootFunctionSerialiser())
            .registerTypeHierarchyAdapter(LootCondition.class, new RobustLootConditionSerialiser())
            .registerTypeHierarchyAdapter(LootContext.EntityTarget.class,
                new LootContext.EntityTarget.Serializer())
            .create());
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
                gsonInstance.toJson(lootTable, lootTable.getClass(), dumper);
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
}
