package leviathan143.loottweaker.common.commands;

import java.io.File;

import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.lib.LootUtils;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class CommandLootTables implements ICommandFunction
{
    public static final String NAME = "loottables";
    public static final String[] DESCRIPTION = new String[]{"Dumps the specified loottable(s) to <minecraft folder>/dumps"};

    private static enum Option
    {
	all,
	entity,
	byName,
	list;
    }

    @Override
    public void execute(String[] args, IPlayer player) 
    {
	if(args.length < 1)
	{
	    player.sendChat("Usage: /mt loottables [mode] <args>");
	    player.sendChat("Modes:| all|  entity  | byName  |");
	    player.sendChat("Args: |none|entityName|tableName|");
	    return;
	}
	Option option = Enum.valueOf(Option.class, args[0]);
	ResourceLocation tableLoc = null;
	switch (option)
	{
	case all:
	    for(ResourceLocation table : LootTableList.getAll())
	    {
		LootUtils.writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager(), getLootTableDumpFilePath(table), true);
	    }
	    player.sendChat("Done!");
	    break;
	    
	case byName:
	    if(args.length < 2)
	    {
		player.sendChat("Loot table name required!");
		return;
	    }
	    tableLoc = new ResourceLocation(args[1]);
	    if(!LootTableList.getAll().contains(tableLoc))
	    {
		player.sendChat("Invalid loot table name!");
		return;
	    }
	    LootUtils.writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager(), getLootTableDumpFilePath(tableLoc), true);
	    player.sendChat("Done!");
	    break;
	    
	case entity:
	    if(args.length < 2)
	    {
		player.sendChat("Entity name required!");
		return;
	    }
	    ResourceLocation entityName = new ResourceLocation(args[1]);
	    if(!EntityList.isStringValidEntityName(entityName))
	    {
		player.sendChat("Invalid entity name!");
		return;
	    }
	    tableLoc = LootUtils.getEntityLootTableFromName(entityName);
	    if(tableLoc == null) return;
	    LootUtils.writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager(), getLootTableDumpFilePath(tableLoc), true);
	    player.sendChat("Done!");
	    break;
	    
	case list:
	    for(ResourceLocation table : LootTableList.getAll())
	    {
		player.sendChat(table.toString());
	    }
	    break;
	    
	default:
	    break;
	}
    }

    private static File getLootTableDumpFilePath(ResourceLocation tableLoc)
    {
	return new File("dumps" + File.separator + "loot_tables" + File.separator + tableLoc.getResourceDomain() + File.separator + tableLoc.getResourcePath() + ".json");
    }
}
