package leviathan143.droptweaker.common.tweakers.loot;

import java.util.Map;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Maps;

import leviathan143.droptweaker.common.DropTweakerMain;
import leviathan143.droptweaker.common.LootUtils;
import leviathan143.droptweaker.common.handlers.DropHandler;
import leviathan143.droptweaker.common.zenscript.ZenLootTableWrapper;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.MineTweakerImplementationAPI.ReloadEvent;
import minetweaker.util.IEventHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("droptweaker.vanilla.LootTables")
public class LootTableTweaker
{	
	//Stores the added LootPools as LootTables until they can be added to the real LootTables
	private static Map<ResourceLocation, ZenLootTableWrapper> tweakedTableStorage = Maps.newHashMap();

	public enum LootTweakType
	{
		//Add a new pool
		ADD,
		//Modify an existing pool
		TWEAK,
		//Remove an existing pool
		REMOVE;
	}

	@ZenMethod
	public static ZenLootTableWrapper getTable(String tableName)
	{
		ResourceLocation tableLoc = new ResourceLocation(tableName);
		if(!tweakedTableStorage.containsKey(tableLoc))
		{
			tweakedTableStorage.put(tableLoc, new ZenLootTableWrapper(new LootTable(LootUtils.NO_POOLS), tableLoc));
		}
		return tweakedTableStorage.get(tableLoc);
	}

	@SubscribeEvent
	public static void onTableLoad(LootTableLoadEvent event)
	{
		if(tweakedTableStorage.containsKey(event.getName()))
		{
			tweakedTableStorage.get(event.getName()).applyLootTweaks(event.getTable());
		}
	}

	public static void onRegister()
	{
		MineTweakerImplementationAPI.onReloadEvent(new IEventHandler<ReloadEvent>() 
		{
			@Override
			public void handle(ReloadEvent paramT) 
			{
				tweakedTableStorage.clear();
				DropHandler.clearedLootTables.clear();
			}
		});

		MineTweakerImplementationAPI.onPostReload(new IEventHandler<ReloadEvent>() 
		{
			@Override
			public void handle(ReloadEvent paramT) 
			{
				DropTweakerMain.proxy.getWorld().getLootTableManager().reloadLootTables();
				DropTweakerMain.logger.log(Level.INFO, "Reloading loot tables");
			}
		});
	}
}
