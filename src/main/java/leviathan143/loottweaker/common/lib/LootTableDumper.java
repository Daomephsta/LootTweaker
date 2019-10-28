package leviathan143.loottweaker.common.lib;

import java.io.File;
import java.io.FileWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;

public class LootTableDumper
{
	public static final LootTableDumper DEFAULT = new LootTableDumper(new File("dumps/loot_tables"));
	private static final Logger LOGGER = LogManager.getLogger();

	private final File dumpFolder;

	public LootTableDumper(File dumpFolder)
	{
		assert dumpFolder.isDirectory(): "Dump folder must be a directory";
		this.dumpFolder = dumpFolder;
		this.dumpFolder.mkdirs();
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
			try(FileWriter writer = new FileWriter(dump))
			{
				Gson gsonInstance = LootTableManagerAccessors.getGsonInstance();
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
