package leviathan143.droptweaker.common.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import leviathan143.droptweaker.common.darkmagic.CommonMethodHandles;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropHandler
{
	//A list of loot tables that have had their default drops removed. Used to prevent mods adding additional drops through events. 
	public static List<ResourceLocation> clearedLootTables = new ArrayList<ResourceLocation>();
	//A map of itemstacks that have been removed from entity drops. Present for compatibility with mods that add additional drops through events.
	public static Map<Class<? extends Entity>, List<ItemStack>> removedEntityDrops = Maps.newHashMap();
	//A map of itemstacks that have been removed from block drops. Present for compatibility with mods that add additional drops through events.
	public static Map<Integer, List<ItemStack>> removedBlockDrops = Maps.newHashMap();

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onBlockDrops(BlockEvent.HarvestDropsEvent event)
	{
		for(Iterator<ItemStack> iter = event.getDrops().iterator(); iter.hasNext();)
		{
			ItemStack stack = iter.next();
			for(ItemStack removedStack : removedBlockDrops.get(Block.getStateId(event.getState())))
			{
				if(ItemStack.areItemsEqual(stack, removedStack))
				{
					iter.remove();
					break;
				}
			}
		}
	}

	//YOU SHALL NOT DROP
	//Drops are cleared here in case someone uses recieveCanceled
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingDrops(LivingDropsEvent event)
	{
		if(event.getEntityLiving() instanceof EntityLiving)
		{
			EntityLiving living = (EntityLiving) event.getEntityLiving();
			ResourceLocation tableLoc = CommonMethodHandles.getEntityLootTable(living);
			if(clearedLootTables.contains(tableLoc))
			{
				event.getDrops().clear();
				return;
			}
			for(Iterator<EntityItem> iter = event.getDrops().iterator(); iter.hasNext();)
			{
				ItemStack stack = iter.next().getEntityItem();
				for(ItemStack removedStack : removedEntityDrops.get(living.getClass()))
				{
					if(ItemStack.areItemsEqual(stack, removedStack))
					{
						iter.remove();
						break;
					}
				}
			}
		}
	}
}
