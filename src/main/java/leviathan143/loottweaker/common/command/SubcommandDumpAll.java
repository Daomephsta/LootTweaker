package leviathan143.loottweaker.common.command;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.LootTableDumper;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.loot.LootTableList;

public class SubcommandDumpAll implements Subcommand 
{
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) 
	{
		for (ResourceLocation tableId : LootTableList.getAll())
		{
			LootTableDumper.DEFAULT.dump(sender.getEntityWorld(), tableId);
		}
		sender.sendMessage(new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.all.done"));
	}
}
