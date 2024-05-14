package leviathan143.loottweaker.common.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.LootTableFinder;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;


public class SubcommandGenerate implements Subcommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (args.length < 2)
        {
            sender.sendMessage(LootTweaker.translation(".commands.generate.type"));
            return;
        }
        String type = args[1];
        if (args.length < 3)
        {
            sender.sendMessage(LootTweaker.translation(".commands.missingName"));
            return;
        }
        ResourceLocation tableId = new ResourceLocation(args[2]);
        if (!LootTableFinder.DEFAULT.exists(tableId))
        {
            sender.sendMessage(LootTweaker.translation(".messages.error.invalidTableName", tableId));
            return;
        }

        RayTraceResult target = sender.getCommandSenderEntity().rayTrace(8.0F, 1.0F);
        if (target.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            World world = sender.getEntityWorld();
            BlockPos pos = choosePosition(world, target);
            switch (type) 
            {
            case "chest":
                TileEntityChest chest = placeChest(world, sender.getCommandSenderEntity(), pos);
                chest.clear();
                chest.setLootTable(tableId, 0L);
                break;
            case "entity":
                generateEntity(sender, args, world, pos, tableId);
                break;
            }
        }
    }

    private void generateEntity(ICommandSender sender, String[] args, World world, BlockPos pos,
        ResourceLocation tableId)
    {
        if (args.length < 4)
        {
            sender.sendMessage(LootTweaker.translation(".commands.generate.missingEntityId"));
            return;
        }
        boolean hasNbt = false;
        NBTTagCompound nbt;
        if (args.length == 5)
        {
            hasNbt = true;
            String nbtString = CommandBase.buildString(args, 4);
            try
            {
                nbt = JsonToNBT.getTagFromJson(nbtString);
                hasNbt = true;
            }
            catch (NBTException nbtexception)
            {
                sender.sendMessage(LootTweaker.translation(".commands.generate.nbtError"));
                nbtexception.printStackTrace();
                return;
            }
        }
        else 
            nbt = new NBTTagCompound();
        String id = args[3];
        if (!EntityList.isRegistered(new ResourceLocation(id)))
        {
            sender.sendMessage(LootTweaker.translation(".commands.generate.invalidEntityId", id));
            return;
        }
        // Set both, as unused entity NBT keys are simply discarded 
        nbt.setString("LootTable", tableId.toString());
        nbt.setString("DeathLootTable", tableId.toString());
        summonEntity(world, id, pos, nbt, hasNbt);
    }

    private Entity summonEntity(World world, String id, BlockPos pos, NBTTagCompound nbt, boolean hasNbt)
    {
        nbt.setString("id", id);
        Entity entity = AnvilChunkLoader.readWorldEntityPos(nbt, world, pos.getX(), pos.getY(), pos.getZ(), true);
        entity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), entity.rotationYaw, entity.rotationPitch);
        if (!hasNbt && entity instanceof EntityLiving)
            ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(pos), null);
        return entity;
    }

    private TileEntityChest placeChest(World world, Entity placer, BlockPos pos)
    {
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
        System.out.println(args);
        switch (args.length)
        {
        case 1:
            return CommandBase.getListOfStringsMatchingLastWord(args, Arrays.asList("chest", "entity"));
        case 2:
            return Subcommand.suggestTableIds(args[1]);
        case 3:
            if (args[0].equals("entity"))
                return CommandBase.getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
        default:
            return Collections.emptyList();
        }
    }
}
