package leviathan143.loottweaker.common.command;

import java.util.List;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.LootTableFinder;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class SubcommandChest implements Subcommand
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

        RayTraceResult target = sender.getCommandSenderEntity().rayTrace(8.0F, 1.0F);
        if (target.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            World world = sender.getEntityWorld();
            TileEntityChest chest = placeChest(world, sender.getCommandSenderEntity(), target);
            chest.clear();
            chest.setLootTable(tableId, 0L);
        }
    }

    private TileEntityChest placeChest(World world, Entity placer, RayTraceResult target)
    {
        BlockPos pos = choosePosition(world, target);
        // Reuse existing chests
        if (world.getBlockState(pos).getBlock() != Blocks.CHEST)
        {
            world.destroyBlock(pos, false);
            world.setBlockState(pos, Blocks.CHEST.getDefaultState()
                .withProperty(BlockChest.FACING, placer.getHorizontalFacing().getOpposite()));
        }
        TileEntity te = world.getTileEntity(pos);
        return (TileEntityChest) te;
    }

    private BlockPos choosePosition(World world, RayTraceResult target)
    {
        IBlockState state = world.getBlockState(target.getBlockPos());
        // Reuse existing chests
        if (state.getBlock() == Blocks.CHEST)
            return target.getBlockPos();
        if (!state.getBlock().isReplaceable(world, target.getBlockPos()))
        {
            // Try the adjacent block towards the player
            BlockPos offset = target.getBlockPos().offset(target.sideHit);
            state = world.getBlockState(offset);
            if (state.getBlock().isReplaceable(world, offset))
                return offset;
        }
        return target.getBlockPos();
    }

    @Override
    public List<String> getCompletions(MinecraftServer server, ICommandSender sender, String[] args,
        BlockPos targetPos)
    {
        return Subcommand.suggestTableIds(args[0]);
    }
}
