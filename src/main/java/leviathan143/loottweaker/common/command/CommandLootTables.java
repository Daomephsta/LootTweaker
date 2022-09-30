package leviathan143.loottweaker.common.command;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import crafttweaker.mc1120.commands.CraftTweakerCommand;
import leviathan143.loottweaker.common.LootTweaker;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;


public class CommandLootTables extends CraftTweakerCommand
{
    private final Map<String, Subcommand> subcommands = ImmutableMap.<String, Subcommand>builder()
        .put("all", new SubcommandDumpAll())
        .put("byName", new SubcommandDumpNamed())
        .put("target", new SubcommandDumpTargetsLootTable())
        .put("list", new SubcommandListLootTables())
        .build();

    public CommandLootTables()
    {
        super("loottables");
    }

    @Override
    protected void init()
    {
        setDescription(new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.desc"));
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (args.length < 1)
        {
            sender.sendMessage(new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.usage"));
            return;
        }
        Subcommand subcommand = subcommands.get(args[0]);
        if (subcommand != null)
        {
            subcommand.execute(server, sender, args);
        }
        else
            sender.sendMessage(
                new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.unknownSubcommand", args[0]));
    }
}
