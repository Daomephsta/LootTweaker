package leviathan143.loottweaker.common.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;


public interface Subcommand
{
    public void execute(MinecraftServer server, ICommandSender sender, String[] args);
}