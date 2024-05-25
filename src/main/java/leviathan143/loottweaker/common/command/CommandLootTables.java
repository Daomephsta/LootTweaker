package leviathan143.loottweaker.common.command;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ImmutableMap;

import crafttweaker.mc1120.commands.CraftTweakerCommand;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.Texts;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;


public class CommandLootTables extends CraftTweakerCommand
{
    private final Map<String, Subcommand> subcommands = ImmutableMap.<String, Subcommand>builder()
        .put("all", new SubcommandDumpAll())
        .put("byName", new SubcommandDumpNamed())
        .put("target", new SubcommandDumpTargetsLootTable())
        .put("list", new SubcommandListLootTables())
        .put("generate", new SubcommandGenerate())
        .build();
    private static final String DOCS_URL = "https://loottweaker-docs.readthedocs.io/en/latest/reference/commands.html";

    public CommandLootTables()
    {
        super("loottables");
    }

    @Override
    protected void init()
    {
        setDescription(LootTweaker.translation(".commands.dump.desc"));
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (args.length < 1 || (args.length == 1 && args[0].equals("help")))
        {
            for (String subcommand : subcommands.keySet())
                sender.sendMessage(LootTweaker.translation(".commands.dump." + subcommand  + ".usage", style -> style.setColor(TextFormatting.RED)));
            sender.sendMessage(LootTweaker.translation(".messages.openDocs", Texts.Styles.urlLink(DOCS_URL)));
            return;
        }
        Subcommand subcommand = subcommands.get(args[0]);
        if (subcommand != null)
        {
            try 
            {
                subcommand.execute(server, sender, args);
            } 
            catch (CommandException e) 
            {
                sender.sendMessage(Texts.styled(new TextComponentTranslation(e.getMessage(), e.getErrorObjects()), 
                    style -> style.setColor(TextFormatting.RED)));
            }
        }
        else
            sender.sendMessage(LootTweaker.translation(".commands.dump.unknownSubcommand", args[0]));
    }

    @Override
    public List<String> getSubSubCommand(MinecraftServer server, ICommandSender sender, String[] args,
        BlockPos targetPos)
    {
        if (args.length == 1)
            return Stream.concat(subcommands.keySet().stream(), Stream.of("help"))
                .filter(s -> s.startsWith(args[0])).collect(toList());

        Subcommand subcommand = subcommands.get(args[0]);
        if (subcommand != null && args.length <= (subcommand.getMaxArguments() + 1))
            return subcommand.getCompletions(server, sender, ArrayUtils.subarray(args, 1, args.length), targetPos);
        return Collections.emptyList();
    }
}
