package leviathan143.loottweaker.common.tweakers;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.zenscript.ZenLootTableWrapper;
import net.minecraft.client.resources.I18n;
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
@ZenClass(LootTweakerMain.MODID + ".vanilla.loot.LootTables")
@Mod.EventBusSubscriber(modid = LootTweakerMain.MODID)
public class LootTableTweaker
{
	public static final Logger LOGGER = LogManager.getLogger();
	// Store data about added loot until it can be added to the real LootTables
	private static Map<ResourceLocation, ZenLootTableWrapper> tweakedTableStorage = Maps.newHashMap();
	// Tables that should not be checked for existence
	private static Collection<ResourceLocation> uncheckedTables = new HashSet<>();
	private static boolean tableLoadingStarted = false;
	
	@ZenMethod
	public static ZenLootTableWrapper getTable(String tableName)
	{
		return getTableInternal(new ResourceLocation(tableName));
	}

	@ZenMethod
	public static ZenLootTableWrapper getTableUnchecked(String tableName)
	{
		ResourceLocation tableLoc = new ResourceLocation(tableName);
		uncheckedTables.add(tableLoc);
		return getTableInternal(tableLoc);
	}
	
	private static ZenLootTableWrapper getTableInternal(ResourceLocation tableLoc)
	{
		if (!tweakedTableStorage.containsKey(tableLoc))
		{
			tweakedTableStorage.put(tableLoc, new ZenLootTableWrapper(tableLoc));
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
			if (!LootTableList.getAll().contains(entry.getKey()) && !uncheckedTables.contains(entry.getKey()))
				CraftTweakerAPI.logError(I18n.format(LootTweakerMain.MODID + ".messages.error.invalidTableName", entry.getKey()));
		}
	}

	private static void applyTweaks(ResourceLocation tableName, LootTable table)
	{
		if (tweakedTableStorage.containsKey(tableName))
		{
			if (table.isFrozen())
			{
				LOGGER.debug("Skipped modifying loot table {} because it is frozen", tableName);
				return;
			}
			tweakedTableStorage.get(tableName).applyLootTweaks(table);
		}
	}
}
