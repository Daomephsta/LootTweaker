package leviathan143.loottweaker.common.tweakers.loot;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public static Set<ResourceLocation> blockLootTables = new HashSet<ResourceLocation>();

    @ZenMethod
    public static ZenLootTableWrapper getTable(String tableName)
    {
	ResourceLocation tableLoc = new ResourceLocation(tableName);
	if(!LootTableList.getAll().contains(tableLoc))
	{
	    CraftTweakerAPI.logError(String.format("No loot table with name %s exists!", tableName));
	    //Returned to prevent NPEs
	    return LootUtils.EMPTY_LOOT_TABLE;
	}
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
    }
}
