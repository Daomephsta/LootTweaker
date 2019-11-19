package leviathan143.loottweaker.common.command;

import java.io.File;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.LootTableDumper;
import leviathan143.loottweaker.common.lib.LootTableFinder;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;


public class SubcommandDumpNamed implements Subcommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (args.length < 2)
        {
            sender.sendMessage(new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.byName.missingName"));
            return;
        }
        ResourceLocation tableId = new ResourceLocation(args[1]);
        if (!LootTableFinder.DEFAULT.exists(tableId))
        {
            sender.sendMessage(new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.byName.invalidName"));
            return;
        }
        File dump = LootTableDumper.DEFAULT.dump(sender.getEntityWorld(), tableId);
        if (!server.isDedicatedServer())
            linkDumpFileInChat(sender, dump, tableId);
    }

    private static void linkDumpFileInChat(ICommandSender sender, File dump, ResourceLocation tableLoc)
    {
        ITextComponent message = new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.dumpLink", tableLoc);
        ITextComponent link = new TextComponentString(dump.toString());
        link.getStyle().setClickEvent(new ClickEvent(Action.OPEN_FILE, dump.toString())).setUnderlined(true).setColor(TextFormatting.AQUA);
        sender.sendMessage(message.appendSibling(link));
    }
}
