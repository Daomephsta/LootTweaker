package leviathan143.loottweaker.common.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.loot.LootTableList;


public class SubcommandListLootTables implements Subcommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        for (ResourceLocation table : LootTableList.getAll())
        {
            sender.sendMessage(new TextComponentString(table.toString()));
        }
    }
}
