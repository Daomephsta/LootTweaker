package leviathan143.loottweaker.common.command;

import java.io.File;
import java.util.List;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.LootTableDumper;
import leviathan143.loottweaker.common.lib.LootTableFinder;
import leviathan143.loottweaker.common.lib.Texts;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;


public class SubcommandDumpNamed implements Subcommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (args.length < 2)
        {
            sender.sendMessage(LootTweaker.translation(".commands.missingName"));
            return;
        }
        ResourceLocation tableId = new ResourceLocation(args[1]);
        if (!LootTableFinder.DEFAULT.exists(tableId))
        {
            sender.sendMessage(LootTweaker.translation(".commands.invalidName"));
            return;
        }
        File dump = LootTableDumper.DEFAULT.dump(sender.getEntityWorld(), tableId);
        if (!server.isDedicatedServer()) linkDumpFileInChat(sender, dump, tableId);
    }

    private static void linkDumpFileInChat(ICommandSender sender, File dump, ResourceLocation tableId)
    {
        sender.sendMessage(LootTweaker.translation(".commands.dump.dumpLink",
            Texts.styledAsString(tableId, style -> style.setUnderlined(true)), Texts.fileLink(dump)));
    }

    @Override
    public List<String> getCompletions(MinecraftServer server, ICommandSender sender, String[] args,
        BlockPos targetPos)
    {
        return Subcommand.suggestTableIds(args[0]);
    }
}
