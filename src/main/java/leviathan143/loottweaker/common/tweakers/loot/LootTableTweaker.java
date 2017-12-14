package leviathan143.loottweaker.common.tweakers.loot;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
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
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(Constants.MODID + ".vanilla.loot.LootTables")
public class LootTableTweaker
{	
    //Stores the added LootPools as LootTables until they can be added to the real LootTables
    private static Map<ResourceLocation, ZenLootTableWrapper> tweakedTableStorage = Maps.newHashMap();

    @ZenMethod
    public static String[] getAllTables()
    {
	List<String> tables = Lists.newArrayList();
	for(ResourceLocation a : LootTableList.getAll()) tables.add(a.toString());
	return tables.toArray(new String[0]);
    }

    @ZenMethod
    public static String[] getAllTablesWithDomain(String domain)
    {
	List<String> tables = Lists.newArrayList();
	for(ResourceLocation a : LootTableList.getAll())
	{
	    if(a.getResourceDomain().equals(domain)) tables.add(a.toString());
	}
	return tables.toArray(new String[0]);
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
	if(table.isFrozen()) return;
	if(tweakedTableStorage.containsKey(tableName))
	{
	    if(!LootTableList.getAll().contains(tableName)) 
	    {
		CraftTweakerAPI.logError(String.format("No loot table with name %s exists!", tableName));
		return;
	    }
	    tweakedTableStorage.get(tableName).applyLootTweaks(table);
	}
    }

    public static void onRegister()
    {
    }
}
