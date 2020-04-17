package leviathan143.loottweaker.common.command;

import java.io.File;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.EntityLivingAccessors;
import leviathan143.loottweaker.common.lib.LootTableDumper;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.ILootContainer;

public class SubcommandDumpTargetsLootTable implements Subcommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (sender instanceof Entity)
        {
            ResourceLocation tableId = null;
            RayTraceResult target = getLookTarget((Entity) sender, 8.0D);
            switch (target.typeOfHit)
            {
            case BLOCK:
                TileEntity te = sender.getEntityWorld().getTileEntity(target.getBlockPos());
                if (te instanceof ILootContainer)
                    tableId = ((ILootContainer) te).getLootTable();
                else
                    sender.sendMessage(new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.target.noTable"));
                break;
            case ENTITY:
                if (target.entityHit instanceof EntityLiving)
                    tableId = EntityLivingAccessors.getLootTable((EntityLiving) target.entityHit);
                else
                    sender.sendMessage(new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.target.noTable"));
                break;
            case MISS:
                sender.sendMessage(new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.target.noTarget"));
                return;
            default:
                return;
            }
            if (tableId == null) return;
            File dump = LootTableDumper.DEFAULT.dump(sender.getEntityWorld(), tableId);
            if (!server.isDedicatedServer())
                linkDumpFileInChat(sender, dump, tableId);
        }
        else
            sender.sendMessage(new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.target.senderNotEntity"));
    }

    private static RayTraceResult getLookTarget(Entity observer, double distance)
    {
        Vec3d eyePos = observer.getPositionEyes(1.0F);
        Vec3d look = observer.getLook(1.0F);
        Vec3d lookTarget = eyePos.add(look.x * distance, look.y * distance, look.z * distance);
        World world = observer.getEntityWorld();
        RayTraceResult result = world.rayTraceBlocks(eyePos, lookTarget);
        double blockHitDistance = 0.0D; // The distance to the block that was
                                        // hit
        if (result != null) blockHitDistance = result.hitVec.distanceTo(eyePos);

        // Encloses the entire area where entities that could collide with this
        // ray exist
        AxisAlignedBB entitySearchArea = new AxisAlignedBB(eyePos.x, eyePos.y, eyePos.z,
                lookTarget.x, lookTarget.y, lookTarget.z);
        Entity hitEntity = null; // The closest entity that was hit
        double entityHitDistance = 0.0D; // The squared distance to the closest
                                            // entity that was hit
        for (Entity candidate : world.getEntitiesInAABBexcluding(observer, entitySearchArea, EntitySelectors.NOT_SPECTATING))
        {
            // The collision AABB of the entity expanded by the collision border
            // size
            AxisAlignedBB collisionBB = candidate.getEntityBoundingBox().grow(candidate.getCollisionBorderSize());
            RayTraceResult intercept = collisionBB.calculateIntercept(eyePos, lookTarget);
            if (intercept != null)
            {
                double distance1 = eyePos.distanceTo(intercept.hitVec);

                if ((distance1 < blockHitDistance || blockHitDistance == 0)
                        && (distance1 < entityHitDistance || entityHitDistance == 0.0D))
                {
                    entityHitDistance = distance1;
                    hitEntity = candidate;
                }
            }
        }

        if (hitEntity != null) result = new RayTraceResult(hitEntity, hitEntity.getPositionVector());

        if (result == null) result = new RayTraceResult(Type.MISS, lookTarget, null, new BlockPos(lookTarget));
        return result;
    }

    private static void linkDumpFileInChat(ICommandSender sender, File dump, ResourceLocation tableLoc)
    {
        ITextComponent message = new TextComponentTranslation(LootTweaker.MODID + ".commands.dump.dumpLink", tableLoc);
        ITextComponent link = new TextComponentString(dump.toString());
        link.getStyle().setClickEvent(new ClickEvent(Action.OPEN_FILE, dump.toString())).setUnderlined(true).setColor(TextFormatting.AQUA);
        sender.sendMessage(message.appendSibling(link));
    }
}
