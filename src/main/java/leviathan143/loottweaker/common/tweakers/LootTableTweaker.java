package leviathan143.loottweaker.common.tweakers;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import leviathan143.loottweaker.common.lib.LootUtils;
import leviathan143.loottweaker.common.zenscript.ZenLootTableWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(Constants.MODID + ".vanilla.loot.LootTables")
@Mod.EventBusSubscriber(modid = Constants.MODID)
public class LootTableTweaker
{
	private static final Logger logger = LogManager.getLogger();
	// Stores the added LootPools as LootTables until they can be added to the real LootTables
	private static Map<ResourceLocation, ZenLootTableWrapper> tweakedTableStorage = Maps.newHashMap();
	private static boolean tableLoadingStarted = false;
	
	@ZenMethod
	public static ZenLootTableWrapper getTable(String tableName)
	{
		ResourceLocation tableLoc = new ResourceLocation(tableName);
		if (!tweakedTableStorage.containsKey(tableLoc))
		{
			tweakedTableStorage.put(tableLoc, new ZenLootTableWrapper(new LootTable(LootUtils.NO_POOLS), tableLoc));
		}
		return tweakedTableStorage.get(tableLoc);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onTableLoad(LootTableLoadEvent event)
	{
		applyTweaks(event.getName(), event.getTable());
		if(!tableLoadingStarted)
		{
			tableLoadingStarted = true;
			onTableLoadingStarted();
		}
	}
	
	private static void onTableLoadingStarted()
	{
		for(Map.Entry<ResourceLocation, ZenLootTableWrapper> entry : tweakedTableStorage.entrySet())
		{
			if (!LootTableList.getAll().contains(entry.getKey()))
				CraftTweakerAPI.logError(String.format("No loot table with name %s exists!", entry.getKey()));
		}
	}

	private static void applyTweaks(ResourceLocation tableName, LootTable table)
	{
		if (tweakedTableStorage.containsKey(tableName))
		{
			if (table.isFrozen())
			{
				logger.debug("Skipped modifying loot table {} because it is frozen", tableName);
				return;
			}
			tweakedTableStorage.get(tableName).applyLootTweaks(table);
		}
	}
}
