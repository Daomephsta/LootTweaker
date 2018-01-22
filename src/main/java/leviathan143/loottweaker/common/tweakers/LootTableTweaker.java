package leviathan143.loottweaker.common.tweakers;

import java.util.Map;

import com.google.common.collect.Maps;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import leviathan143.loottweaker.common.lib.DataToJSONConverter;
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
	// Stores the added LootPools as LootTables until they can be added to the
	// real LootTables
	private static Map<ResourceLocation, ZenLootTableWrapper> tweakedTableStorage = Maps.newHashMap();
	
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
	}

	private static void applyTweaks(ResourceLocation tableName, LootTable table)
	{
		if (table.isFrozen()) return;
		if (tweakedTableStorage.containsKey(tableName))
		{
			if (!LootTableList.getAll().contains(tableName))
			{
				CraftTweakerAPI.logError(String.format("No loot table with name %s exists!", tableName));
				return;
			}
			tweakedTableStorage.get(tableName).applyLootTweaks(table);
		}
	}
}
