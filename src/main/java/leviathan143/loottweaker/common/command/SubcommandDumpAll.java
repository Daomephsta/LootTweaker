package leviathan143.loottweaker.common.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.LootTableDumper;
import leviathan143.loottweaker.common.lib.LootTableFinder;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;


public class SubcommandDumpAll implements Subcommand
{
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (!LootTableFinder.DEFAULT.fullScanPerformed())
            sender.sendMessage(new TextComponentTranslation("loottweaker.messages.info.locatingLootTables"));
        for (ResourceLocation tableId : LootTableFinder.DEFAULT.findAll())
        {
            try
            {
                LootTableDumper.DEFAULT.dump(sender.getEntityWorld(), tableId);
            }
            catch (Exception e)
            {
                sender.sendMessage(
                    new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.all.exception", tableId));
                LOGGER.error("Unable to dump {}", tableId, unwrap(e));
            }
        }
        sender.sendMessage(new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.all.done"));
    }

    private Throwable unwrap(Throwable t)
    {
        Throwable unwrapped = t;
        while (unwrapped.getCause() != null && unwrapped.getMessage().equals(unwrapped.getCause().toString()))
            unwrapped = unwrapped.getCause();
        return unwrapped;
    }
}
