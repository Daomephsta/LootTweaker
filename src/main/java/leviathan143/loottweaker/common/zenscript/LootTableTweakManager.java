package leviathan143.loottweaker.common.zenscript;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;

public class LootTableTweakManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<ResourceLocation, ZenLootTableWrapper> tweakedTables = new HashMap<>();
    private final ErrorHandler errorHandler;

    @Inject
    LootTableTweakManager(ErrorHandler errorHandler)
    {
        this.errorHandler = errorHandler;
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
        return tweakedTables.computeIfAbsent(tableId, id -> new ZenLootTableWrapper(id));
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
                wrapper.applyTweaks(table);
            else
                errorHandler.handle("No loot table with name %s exists!", tableId);
        }
    }
}
