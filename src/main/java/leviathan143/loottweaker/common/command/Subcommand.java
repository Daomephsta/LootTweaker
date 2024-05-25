package leviathan143.loottweaker.common.command;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.LootTableFinder;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;


public interface Subcommand
{
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

    public default List<String> getCompletions(MinecraftServer server, ICommandSender sender, String[] args,
        BlockPos targetPos)
    {
        return Collections.emptyList();
    }
    
    public int getMaxArguments();

    public static List<String> suggestTableIds(String prompt)
    {
        return Streams.stream(LootTableFinder.DEFAULT.findAll())
            .flatMap(rl -> 
            {
                if (rl.getNamespace().equals("minecraft"))
                    return Stream.of(rl.toString(), rl.getPath());
                return Stream.of(rl.toString());
            })
            .filter(id -> id.startsWith(prompt))
            .collect(toList());
    }

    public static WrongUsageException wrongUsage(String keySuffix, Object... args)
    {
        return new WrongUsageException(LootTweaker.MODID + keySuffix, args);
    }
}