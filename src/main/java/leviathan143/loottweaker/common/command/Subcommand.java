package leviathan143.loottweaker.common.command;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Streams;

import leviathan143.loottweaker.common.lib.LootTableFinder;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;


public interface Subcommand
{
    public void execute(MinecraftServer server, ICommandSender sender, String[] args);

    public default List<String> getCompletions(MinecraftServer server, ICommandSender sender, String[] args,
        BlockPos targetPos)
    {
        return Collections.emptyList();
    }

    public static List<String> suggestTableIds(String promptIn)
    {
        final String prompt = promptIn.contains(":")
            ? promptIn
            : "minecraft:" + promptIn;
        return Streams.stream(LootTableFinder.DEFAULT.findAll())
            .map(ResourceLocation::toString)
            .filter(id -> id.startsWith(prompt))
            .collect(toList());
    }
}