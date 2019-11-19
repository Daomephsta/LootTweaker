package leviathan143.loottweaker.common.command;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.LootTableDumper;
import leviathan143.loottweaker.common.lib.LootTableFinder;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class SubcommandDumpAll implements Subcommand
{
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args)
	{
	    if (!LootTableFinder.DEFAULT.fullScanPerformed())
	        sender.sendMessage(new TextComponentTranslation("loottweaker.messages.info.locatingLootTables"));
		for (ResourceLocation tableId : LootTableFinder.DEFAULT.findAll())
		{
			LootTableDumper.DEFAULT.dump(sender.getEntityWorld(), tableId);
		}
		sender.sendMessage(new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.all.done"));
	}
}
