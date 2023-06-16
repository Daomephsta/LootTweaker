package leviathan143.loottweaker.common.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.LootTableDumper;
import leviathan143.loottweaker.common.lib.LootTableFinder;
import leviathan143.loottweaker.common.lib.Texts;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;


public class SubcommandDumpAll implements Subcommand
{
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (!LootTableFinder.DEFAULT.fullScanPerformed())
            sender.sendMessage(LootTweaker.translation(".messages.info.locatingLootTables"));
        LootTableDumper dumper = LootTableDumper.DEFAULT;
        for (ResourceLocation tableId : LootTableFinder.DEFAULT.findAll())
        {
            try
            {
                dumper.dump(sender.getEntityWorld(), tableId);
            }
            catch (Exception e)
            {
                sender.sendMessage(LootTweaker.translation(".commands.dump.all.exception", tableId));
                LOGGER.error("Unable to dump {}", tableId, unwrap(e));
            }
        }
        sender.sendMessage(LootTweaker.translation(".commands.dump.all.done",
            Texts.fileLink(dumper.getFolder())));
    }

    private Throwable unwrap(Throwable t)
    {
        Throwable unwrapped = t;
        while (unwrapped.getCause() != null && unwrapped.getMessage().equals(unwrapped.getCause().toString()))
            unwrapped = unwrapped.getCause();
        return unwrapped;
    }
}
