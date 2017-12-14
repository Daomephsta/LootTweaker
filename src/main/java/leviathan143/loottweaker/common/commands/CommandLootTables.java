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
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
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
		setDescription(new TextComponentString("/mt loottables [mode] <args>" + System.lineSeparator()
				+ "    Dumps the specified loottable(s)" + System.lineSeparator() + "to <minecraft folder>/dumps"));
	}

	@Override
	public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args)
	{
		if (args.length < 1)
		{
			sender.sendMessage(new TextComponentString("Usage: /mt loottables [mode] <args>"));
			sender.sendMessage(new TextComponentString("Modes: all | entity | byName"));
			sender.sendMessage(new TextComponentString("Args: none | entityName | tableName"));
			return;
		}
		Option option = Enum.valueOf(Option.class, args[0]);
		ResourceLocation tableLoc = null;
		switch (option)
		{
		case all :
			for (ResourceLocation table : LootTableList.getAll())
			{
				LootUtils.writeTableToJSON(table, LootTweakerMain.proxy.getWorld().getLootTableManager(),
						getLootTableDumpFilePath(table), true);
			}
			sender.sendMessage(new TextComponentString("Done!"));
			break;

		case byName :
			if (args.length < 2)
			{
				sender.sendMessage(new TextComponentString("Loot table name required!"));
				return;
			}
			tableLoc = new ResourceLocation(args[1]);
			if (!LootTableList.getAll().contains(tableLoc))
			{
				sender.sendMessage(new TextComponentString("Invalid loot table name!"));
				return;
			}
			LootUtils.writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager(),
					getLootTableDumpFilePath(tableLoc), true);
			sender.sendMessage(new TextComponentString("Done!"));
			break;

		case target :
			if (sender instanceof Entity)
			{
				RayTraceResult target = getLookTarget((Entity) sender, 8.0D);
				switch (target.typeOfHit)
				{
				case BLOCK :
					TileEntity te = sender.getEntityWorld().getTileEntity(target.getBlockPos());
					if (te instanceof ILootContainer) tableLoc = ((ILootContainer) te).getLootTable();
					else sender.sendMessage(new TextComponentString("The target does not have a loot table"));
					break;
				case ENTITY :
					if (target.entityHit instanceof EntityLiving)
						tableLoc = CommonMethodHandles.getEntityLootTable((EntityLiving) target.entityHit);
					else
						sender.sendMessage(new TextComponentString("The target does not have a loot table"));
					break;
				case MISS :
					sender.sendMessage(new TextComponentString("Nothing in range"));
					return;
				default :
					return;
				}
				if (tableLoc == null) return;
				LootUtils.writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager(),
						getLootTableDumpFilePath(tableLoc), true);
				sender.sendMessage(new TextComponentString("Done!"));
			}
			else sender.sendMessage(new TextComponentString("This command can only be executed by entities"));
			break;

		case list :
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
		return new File("dumps" + File.separator + "loot_tables" + File.separator + tableLoc.getResourceDomain()
				+ File.separator + tableLoc.getResourcePath() + ".json");
	}

	// Copied from EntityRenderer#getMouseOver() and modified to work on any
	// side
	private static RayTraceResult getLookTarget(Entity entity, double distance)
	{
		RayTraceResult result = null;
		Entity targetedEntity = null;

		Vec3d eyePos = entity.getPositionEyes(1.0F);
		Vec3d look = entity.getLook(1.0F);
		Vec3d lookTarget = eyePos.addVector(look.x * distance, look.y * distance, look.z * distance);
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
							public boolean apply(@Nullable Entity p_apply_1_)
							{
								return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
							}
						}));
		double d2 = d1;

		for (Entity entity1 : list)
		{
			AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox()
					.grow((double) entity1.getCollisionBorderSize());
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
