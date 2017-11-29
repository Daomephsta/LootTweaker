package leviathan143.loottweaker.common.commands;

import java.io.File;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import leviathan143.loottweaker.common.lib.LootUtils;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
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

public class CommandLootTables implements ICommandFunction
{
    public static final String NAME = "loottables";
    public static final String[] DESCRIPTION = new String[]{"/mt loottables [mode] <args>", "    Dumps the specified loottable(s)", "    to <minecraft folder>/dumps"};

    private static enum Option
    {
	all,
	target,
	byName,
	list;
    }

    @Override
    public void execute(String[] args, IPlayer player) 
    {
	if(args.length < 1)
	{
	    player.sendChat("Usage: /mt loottables [mode] <args>");
	    player.sendChat("Modes: all | target | byName | list");
	    player.sendChat("Args: none | none | tableName | none");
	    return;
	}
	Option option = Enum.valueOf(Option.class, args[0]);
	ResourceLocation tableLoc = null;
	switch (option)
	{
	case all:
	    for(ResourceLocation table : LootTableList.getAll())
	    {
		LootUtils.writeTableToJSON(table, LootTweakerMain.proxy.getWorld().getLootTableManager(), getLootTableDumpFilePath(table), true);
	    }
	    player.sendChat("Done!");
	    break;
	    
	case byName:
	    if(args.length < 2)
	    {
		player.sendChat("Loot table name required!");
		return;
	    }
	    tableLoc = new ResourceLocation(args[1]);
	    if(!LootTableList.getAll().contains(tableLoc))
	    {
		player.sendChat("Invalid loot table name!");
		return;
	    }
	    LootUtils.writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager(), getLootTableDumpFilePath(tableLoc), true);
	    player.sendChat("Done!");
	    break;
	    
	case target:
	    	EntityPlayer playerEntity = MineTweakerMC.getPlayer(player);
		RayTraceResult target = getLookTarget(playerEntity, 8.0D);
		switch(target.typeOfHit)
		{
		case BLOCK:
		    TileEntity te = playerEntity.getEntityWorld().getTileEntity(target.getBlockPos());
		    if(te instanceof ILootContainer) tableLoc = ((ILootContainer) te).getLootTable();
		    else playerEntity.sendMessage(new TextComponentString("The target does not have a loot table"));
		    break;
		case ENTITY:
		    if(target.entityHit instanceof EntityLiving) tableLoc = CommonMethodHandles.getEntityLootTable((EntityLiving) target.entityHit);
		    else playerEntity.sendMessage(new TextComponentString("The target does not have a loot table"));
		    break;
		case MISS: 
		    playerEntity.sendMessage(new TextComponentString("Nothing in range")); 
		    return;
		default: 
		    return;
		}
		if(tableLoc == null) return;
		LootUtils.writeTableToJSON(tableLoc, LootTweakerMain.proxy.getWorld().getLootTableManager(), getLootTableDumpFilePath(tableLoc), true);
		playerEntity.sendMessage(new TextComponentString("Done!"));
	    break;
	    
	case list:
	    for(ResourceLocation table : LootTableList.getAll())
	    {
		player.sendChat(table.toString());
	    }
	    break;
	    
	default:
	    break;
	}
    }

    private static File getLootTableDumpFilePath(ResourceLocation tableLoc)
    {
	return new File("dumps" + File.separator + "loot_tables" + File.separator + tableLoc.getResourceDomain() + File.separator + tableLoc.getResourcePath() + ".json");
    }
    
    /**Copied from {@code EntityRenderer#getMouseOver(float)} and modified to work on any side**/
    private static RayTraceResult getLookTarget(Entity entity, double distance)
    {
	RayTraceResult result = null;
	Entity targetedEntity = null;

	Vec3d eyePos = entity.getPositionEyes(1.0F);
	Vec3d look = entity.getLook(1.0F);
	Vec3d lookTarget = eyePos.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
	result = entity.getEntityWorld().rayTraceBlocks(eyePos, lookTarget, false, false, true);
	boolean flag = false;
	double d1 = distance;

	if (result != null)
	{
	    d1 = result.hitVec.distanceTo(eyePos);
	}

	Vec3d vec3d3 = null;
	List<Entity> list = entity.getEntityWorld().getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance).expandXyz(1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
	{
	    public boolean apply(@Nullable Entity p_apply_1_)
	    {
		return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
	    }
	}));
	double d2 = d1;

	for (Entity entity1 : list)
	{
	    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz((double)entity1.getCollisionBorderSize());
	    RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(eyePos, lookTarget);

	    if (axisalignedbb.isVecInside(eyePos))
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
		    if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity1.canRiderInteract())
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
	    result = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing)null, new BlockPos(vec3d3));
	}

	if (targetedEntity != null && (d2 < d1 || result == null)) result = new RayTraceResult(targetedEntity, vec3d3);
	return result;
    }
}
