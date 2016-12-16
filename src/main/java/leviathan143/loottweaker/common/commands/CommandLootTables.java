package leviathan143.loottweaker.common.commands;

import java.io.File;

import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.LootUtils;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class CommandLootTables implements ICommandFunction
{
	public static final String NAME = "loottables";
	public static final String[] DESCRIPTION = new String[]{"Dumps the specified loottable(s) to <minecraft folder>/dumps"};

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
		if(args[0].equals("all"))
		{
			for(ResourceLocation tableLoc : LootTableList.getAll())
			{
				LootUtils.writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager(), getLootTableDumpFilePath(tableLoc));
			}
		}
		else if(args[0].equals("entity"))
		{
			if(args.length < 2)
			{
				player.sendChat("Entity name required!");
				return;
			}
			if(!EntityList.NAME_TO_CLASS.containsKey(args[1]))
			{
				player.sendChat("Invalid entity name!");
				return;
			}
			ResourceLocation tableLoc = LootUtils.getEntityLootTableFromName(args[1]);
			if(tableLoc == null) return;
			LootUtils.writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager(), getLootTableDumpFilePath(tableLoc));
		}
		else if(args[0].equals("byName"))
		{
			if(args.length < 2)
			{
				player.sendChat("Loot table name required!");
				return;
			}
			ResourceLocation tableLoc = new ResourceLocation(args[1]);
			if(!LootTableList.getAll().contains(tableLoc))
			{
				player.sendChat("Invalid loot table name!");
				return;
			}
			LootUtils.writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager(), getLootTableDumpFilePath(tableLoc));
		}
	}
	
	private static File getLootTableDumpFilePath(ResourceLocation tableLoc)
	{
		return new File("dumps" + File.separator + "loot_tables" + File.separator + tableLoc.getResourceDomain() + File.separator + tableLoc.getResourcePath() + ".json");
	}
}
