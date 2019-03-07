package leviathan143.loottweaker.common.commands;

import java.io.File;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import crafttweaker.mc1120.commands.CraftTweakerCommand;
import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import leviathan143.loottweaker.common.lib.LootUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootTableList;

public class CommandLootTables extends CraftTweakerCommand
{
	public CommandLootTables()
	{
		super("loottables");
	}

	private static enum Option
	{
		all, target, byName, list;
	}

	@Override
	protected void init()
	{
		setDescription(new TextComponentTranslation(LootTweakerMain.MODID + ".commands.dump.desc"));
	}

	@Override
	public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args)
	{
		if (args.length < 1)
		{
			sender.sendMessage(new TextComponentTranslation(LootTweakerMain.MODID + ".commands.dump.usage"));
			return;
		}
		Option option = Enum.valueOf(Option.class, args[0]);
		ResourceLocation tableLoc = null;
		switch (option)
		{
		case all :
			for (ResourceLocation table : LootTableList.getAll())
			{
				LootUtils.dump(LootTweakerMain.proxy.getWorld(), table, getLootTableDumpFilePath(table));
			}
			sender.sendMessage(new TextComponentTranslation(LootTweakerMain.MODID + ".commands.dump.all.done"));
			break;

		case byName:
			if (args.length < 2)
			{
				sender.sendMessage(new TextComponentTranslation(LootTweakerMain.MODID + ".commands.dump.byName.missingName"));
				return;
			}
			tableLoc = new ResourceLocation(args[1]);
			if (!LootTableList.getAll().contains(tableLoc))
			{
				sender.sendMessage(new TextComponentTranslation(LootTweakerMain.MODID + ".commands.dump.byName.invalidName"));
				return;
			}
			File dumpPathByName = getLootTableDumpFilePath(tableLoc);
			LootUtils.dump(LootTweakerMain.proxy.getWorld(), tableLoc, dumpPathByName);
			linkDumpFileInChat(sender, dumpPathByName, tableLoc);
			break;

		case target:
			if (sender instanceof Entity)
			{
				RayTraceResult target = getLookTarget((Entity) sender, 8.0D);
				switch (target.typeOfHit)
				{
				case BLOCK :
					TileEntity te = sender.getEntityWorld().getTileEntity(target.getBlockPos());
					if (te instanceof ILootContainer) tableLoc = ((ILootContainer) te).getLootTable();
					else sender.sendMessage(new TextComponentTranslation(LootTweakerMain.MODID + ".commands.dump.target.noTable"));
					break;
				case ENTITY :
					if (target.entityHit instanceof EntityLiving)
						tableLoc = CommonMethodHandles.getEntityLootTable((EntityLiving) target.entityHit);
					else
						sender.sendMessage(new TextComponentTranslation(LootTweakerMain.MODID + ".commands.dump.target.noTable"));
					break;
				case MISS :
					sender.sendMessage(new TextComponentTranslation(LootTweakerMain.MODID + ".commands.dump.target.noTarget"));
					return;
				default :
					return;
				}
				if (tableLoc == null) return;
				File dumpPathTarget = getLootTableDumpFilePath(tableLoc);
				LootUtils.dump(LootTweakerMain.proxy.getWorld(), tableLoc, dumpPathTarget);
				linkDumpFileInChat(sender, dumpPathTarget, tableLoc);
			}
			else sender.sendMessage(new TextComponentTranslation(LootTweakerMain.MODID + ".commands.dump.target.senderNotEntity"));
			break;

		case list:
			for (ResourceLocation table : LootTableList.getAll())
			{
				sender.sendMessage(new TextComponentString(table.toString()));
			}
			break;

		default :
			break;
		}
	}

	private static File getLootTableDumpFilePath(ResourceLocation tableLoc)
	{
		return new File("dumps" + File.separator + "loot_tables" + File.separator + tableLoc.getNamespace()
				+ File.separator + tableLoc.getPath() + ".json");
	}

	private static void linkDumpFileInChat(ICommandSender sender, File dumpPath, ResourceLocation tableLoc)
	{
		ITextComponent message = new TextComponentTranslation(LootTweakerMain.MODID + ".commands.dump.dumpLink", tableLoc);
		ITextComponent link = new TextComponentString(dumpPath.toString());
		link.getStyle()
			.setClickEvent(new ClickEvent(Action.OPEN_FILE, dumpPath.toString()))
			.setUnderlined(true)
			.setColor(TextFormatting.AQUA);
		sender.sendMessage(message.appendSibling(link));
	}

	// Copied from EntityRenderer#getMouseOver() and modified to work on any
	// side
	private static RayTraceResult getLookTarget(Entity entity, double distance)
	{
		RayTraceResult result = null;
		Entity targetedEntity = null;

		Vec3d eyePos = entity.getPositionEyes(1.0F);
		Vec3d look = entity.getLook(1.0F);
		Vec3d lookTarget = eyePos.add(look.x * distance, look.y * distance, look.z * distance);
		result = entity.getEntityWorld().rayTraceBlocks(eyePos, lookTarget, false, false, true);
		boolean flag = false;
		double d1 = distance;

		if (result != null)
		{
			d1 = result.hitVec.distanceTo(eyePos);
		}

		Vec3d vec3d3 = null;
		List<Entity> list = entity.getEntityWorld()
				.getEntitiesInAABBexcluding(entity,
						entity.getEntityBoundingBox().expand(look.x * distance, look.y * distance, look.z * distance)
								.grow(1.0D, 1.0D, 1.0D),
						Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
						{
							@Override
							public boolean apply(@Nullable Entity p_apply_1_)
							{
								return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
							}
						}));
		double d2 = d1;

		for (Entity entity1 : list)
		{
			AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox()
					.grow(entity1.getCollisionBorderSize());
			RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(eyePos, lookTarget);

			if (axisalignedbb.contains(eyePos))
			{
				if (d2 >= 0.0D)
				{
					targetedEntity = entity1;
					vec3d3 = raytraceresult == null ? eyePos : raytraceresult.hitVec;
					d2 = 0.0D;
				}
			}
			else if (raytraceresult != null)
			{
				double d3 = eyePos.distanceTo(raytraceresult.hitVec);

				if (d3 < d2 || d2 == 0.0D)
				{
					if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity()
							&& !entity1.canRiderInteract())
					{
						if (d2 == 0.0D)
						{
							targetedEntity = entity1;
							vec3d3 = raytraceresult.hitVec;
						}
					}
					else
					{
						targetedEntity = entity1;
						vec3d3 = raytraceresult.hitVec;
						d2 = d3;
					}
				}
			}
		}

		if (targetedEntity != null && flag && eyePos.distanceTo(vec3d3) > 3.0D)
		{
			targetedEntity = null;
			result = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing) null, new BlockPos(vec3d3));
		}

		if (targetedEntity != null && (d2 < d1 || result == null)) result = new RayTraceResult(targetedEntity, vec3d3);
		return result;
	}
}
