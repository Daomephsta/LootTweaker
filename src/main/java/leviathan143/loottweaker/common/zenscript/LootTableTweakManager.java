package leviathan143.loottweaker.common.zenscript;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;

public class LootTableTweakManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<ResourceLocation, ZenLootTableWrapper> tweakedTables = new HashMap<>();
    private final LootTweakerContext context;

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

    public void tweakTable(ResourceLocation tableId, LootTable table)
    {
        if (tweakedTables.containsKey(tableId))
        {
            if (table.isFrozen())
            {
                LOGGER.debug("Skipped modifying loot table {} because it is frozen", tableId);
                return;
            }
            ZenLootTableWrapper wrapper = tweakedTables.get(tableId);
            if (wrapper.isValid())
                wrapper.applyTweakers(table);
            else
                context.getErrorHandler().error("No loot table with name %s exists!", tableId);
        }
    }
}
