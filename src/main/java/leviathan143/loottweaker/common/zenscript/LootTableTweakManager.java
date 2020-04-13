package leviathan143.loottweaker.common.zenscript;

import java.io.File;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import leviathan143.loottweaker.common.lib.LootTableDumper;
import leviathan143.loottweaker.common.mutable_loot.MutableLootTable;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;

public class LootTableTweakManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<ResourceLocation, ZenLootTableWrapper> tweakedTables = new HashMap<>();
    private final Collection<ZenLootTableWrapper> tableBuilders = new HashSet<>();
    private final LootTweakerContext context;
    private File lastWorldDirectory;

    LootTableTweakManager(LootTweakerContext context)
    {
        this.context = context;
    }

	public ZenLootTableWrapper getTable(String tableName)
	{
	    return getTableInternal(tableName);
	}

	public ZenLootTableWrapper getTableUnchecked(String tableName)
	{
	    return getTable(tableName);
	}

	private ZenLootTableWrapper getTableInternal(String tableName)
    {
        ResourceLocation tableId = new ResourceLocation(tableName);
        return tweakedTables.computeIfAbsent(tableId, id -> context.wrapLootTable(id));
    }

    public ZenLootTableWrapper newTable(String id)
    {
        ZenLootTableWrapper builder = context.wrapLootTable(new ResourceLocation(id));
        tableBuilders.add(builder);
        return builder;
    }

    public void onWorldLoad(World world)
    {
        //Only dump once per save
        if (Objects.equals(lastWorldDirectory, world.getSaveHandler().getWorldDirectory()))
            return;
        LootTableDumper dumper = new LootTableDumper(new File(world.getSaveHandler().getWorldDirectory(), "data/loot_tables"));
        for (ZenLootTableWrapper builder : tableBuilders)
        {
            MutableLootTable mutableTable = new MutableLootTable(builder.getId(), new HashMap<>());
            builder.applyTweakers(mutableTable);
            dumper.dump(mutableTable.toImmutable(), builder.getId());
        }
        lastWorldDirectory = world.getSaveHandler().getWorldDirectory();
    }

    public LootTable tweakTable(ResourceLocation tableId, LootTable table)
    {
        if (tweakedTables.containsKey(tableId))
        {
            if (table.isFrozen())
            {
                LOGGER.debug("Skipped modifying loot table {} because it is frozen", tableId);
                return table;
            }
            ZenLootTableWrapper wrapper = tweakedTables.get(tableId);
            if (wrapper.isValid())
            {
                MutableLootTable mutableTable = new MutableLootTable(table, tableId);
                wrapper.applyTweakers(mutableTable);
                return mutableTable.toImmutable();
            }
            else
                context.getErrorHandler().error("No loot table with name %s exists!", tableId);
        }
        return table;
    }
}
