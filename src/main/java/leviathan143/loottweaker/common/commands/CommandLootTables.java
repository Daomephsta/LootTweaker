package leviathan143.loottweaker.common.commands;

import java.io.File;

import crafttweaker.mc1120.commands.CraftTweakerCommand;
import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.lib.LootUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.loot.LootTableList;

public class CommandLootTables extends CraftTweakerCommand
{
    public CommandLootTables()
    {
	super("loottables");
    }
    
    public static final String[] DESCRIPTION = new String[]{"/mt loottables [mode] <args>", "    Dumps the specified loottable(s)", "    to <minecraft folder>/dumps"};

    private static enum Option
    {
	all,
	entity,
	byName,
	list;
    }
    
    @Override
    protected void init()
    {
	setDescription(new TextComponentString("/mt loottables [mode] <args>" + System.lineSeparator() + "    Dumps the specified loottable(s)" + System.lineSeparator() + "to <minecraft folder>/dumps"));
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) 
    {
	if(args.length < 1)
	{
	    sender.sendMessage(new TextComponentString("Usage: /mt loottables [mode] <args>"));
	    sender.sendMessage(new TextComponentString("Modes: all | entity | byName"));
	    sender.sendMessage(new TextComponentString("Args: none | entityName | tableName"));
	    return;
	}
	Option option = Enum.valueOf(Option.class, args[0]);
	ResourceLocation tableLoc = null;
	switch (option)
	{
	case all:
	    for(ResourceLocation table : LootTableList.getAll())
	    {
		LootUtils.writeTableToJSON(table, LootTweakerMain.proxy.getWorld().getLootTableManager(), getLootTableDumpFilePath(table), true);
	    }
	    sender.sendMessage(new TextComponentString("Done!"));
	    break;
	    
	case byName:
	    if(args.length < 2)
	    {
		sender.sendMessage(new TextComponentString("Loot table name required!"));
		return;
	    }
	    tableLoc = new ResourceLocation(args[1]);
	    if(!LootTableList.getAll().contains(tableLoc))
	    {
		sender.sendMessage(new TextComponentString("Invalid loot table name!"));
		return;
	    }
	    LootUtils.writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager(), getLootTableDumpFilePath(tableLoc), true);
	    sender.sendMessage(new TextComponentString("Done!"));
	    break;
	    
	case entity:
	    if(args.length < 2)
	    {
		sender.sendMessage(new TextComponentString("Entity name required!"));
		return;
	    }
	    ResourceLocation entityName = new ResourceLocation(args[1]);
	    if(!EntityList.isRegistered(entityName))
	    {
		sender.sendMessage(new TextComponentString("Invalid entity name!"));
		return;
	    }
	    tableLoc = LootUtils.getEntityLootTableFromName(entityName);
	    if(tableLoc == null) return;
	    LootUtils.writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager(), getLootTableDumpFilePath(tableLoc), true);
	    sender.sendMessage(new TextComponentString("Done!"));
	    break;
	    
	case list:
	    for(ResourceLocation table : LootTableList.getAll())
	    {
		sender.sendMessage(new TextComponentString(table.toString()));
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
