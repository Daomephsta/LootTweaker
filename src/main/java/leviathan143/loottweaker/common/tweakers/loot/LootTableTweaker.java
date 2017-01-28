package leviathan143.loottweaker.common.tweakers.loot;

import java.util.*;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Maps;

import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import leviathan143.loottweaker.common.LootUtils;
import leviathan143.loottweaker.common.zenscript.ZenLootTableWrapper;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.MineTweakerImplementationAPI.ReloadEvent;
import minetweaker.util.IEventHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(Constants.MODID + ".vanilla.LootTables")
public class LootTableTweaker
{	
    //Stores the added LootPools as LootTables until they can be added to the real LootTables
    private static Map<ResourceLocation, ZenLootTableWrapper> tweakedTableStorage = Maps.newHashMap();
    public static Set<ResourceLocation> blockLootTables = new HashSet<ResourceLocation>();

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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTableLoad(LootTableLoadEvent event)
    {
	applyTweaks(event.getName(), event.getTable());
    }

    private static void applyTweaks(ResourceLocation tableName, LootTable table)
    {
	if(tweakedTableStorage.containsKey(tableName))
	{
	    tweakedTableStorage.get(tableName).applyLootTweaks(table);
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
	    }
	});

	MineTweakerImplementationAPI.onPostReload(new IEventHandler<ReloadEvent>() 
	{
	    @Override
	    public void handle(ReloadEvent paramT) 
	    {
		World world = LootTweakerMain.proxy.getWorld();
		if(world != null)
		{
		    LootTableManager manager = world.getLootTableManager();
		    if(manager != null)
		    {
			manager.reloadLootTables();
			LootTweakerMain.logger.log(Level.INFO, "Reloading loot tables");
		    }
		}
	    }
	});
    }
}