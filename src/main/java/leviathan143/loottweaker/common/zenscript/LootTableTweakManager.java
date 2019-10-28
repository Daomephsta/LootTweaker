package leviathan143.loottweaker.common.zenscript;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".vanilla.loot.LootTables")
@Mod.EventBusSubscriber(modid = LootTweaker.MODID)
public class LootTableTweakManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<ResourceLocation, ZenLootTableWrapper> tweakedTables = new HashMap<>();

    private static Phase phase = Phase.GATHER;
    public static enum Phase
    {
        GATHER, APPLY
    }

	@ZenMethod
	public static ZenLootTableWrapper getTable(String tableName)
	{
	    return getTableInternal(tableName, true);
	}

	@ZenMethod
	public static ZenLootTableWrapper getTableUnchecked(String tableName)
	{
	    return getTableInternal(tableName, false);
	}

	private static ZenLootTableWrapper getTableInternal(String tableName, boolean checkRegistered)
    {
        ResourceLocation tableId = new ResourceLocation(tableName);
        return tweakedTables.computeIfAbsent(tableId, id -> new ZenLootTableWrapper(id, checkRegistered));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTableLoad(LootTableLoadEvent event)
    {
        applyTweaks(event.getName(), event.getTable());
        if(phase != Phase.APPLY)
        {
            phase = Phase.APPLY;
            //Remove invalid tables, logging an error
            tweakedTables.values().removeIf(table ->
            {
                if (!table.isValid())
                {
                    CraftTweakerAPI.logError(String.format("No loot table with name %s exists!", table.getId()));
                    return true;
                }
                return false;
            });
        }
    }

    private static void applyTweaks(ResourceLocation tableName, LootTable table)
    {
        if (tweakedTables.containsKey(tableName))
        {
            if (table.isFrozen())
            {
                LOGGER.debug("Skipped modifying loot table {} because it is frozen", tableName);
                return;
            }
            tweakedTables.get(tableName).applyTweaks(table);
        }
    }

	public Phase getPhase()
    {
        return phase;
    }
}
