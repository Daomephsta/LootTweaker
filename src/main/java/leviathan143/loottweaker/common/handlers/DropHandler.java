package leviathan143.loottweaker.common.handlers;

import java.util.*;

import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropHandler
{
	//A map of itemstacks that have been removed from entity drops. Present for compatibility with mods that add additional drops through events.
	public static Map<Class<? extends Entity>, List<ItemStack>> removedEntityDrops = Maps.newHashMap();
	//A map of itemstacks that have been removed from block drops. Present for compatibility with mods that add additional drops through events.
	public static Map<Integer, List<ItemStack>> removedBlockDrops = Maps.newHashMap();

	//YOU SHALL NOT DROP
	//Drops are cleared here in case someone uses recieveCanceled
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingDrops(LivingDropsEvent event)
	{
		if(event.getEntityLiving() instanceof EntityLiving)
		{
			EntityLiving living = (EntityLiving) event.getEntityLiving();
			for(Iterator<EntityItem> iter = event.getDrops().iterator(); iter.hasNext();)
			{
				ItemStack stack = iter.next().getItem();
				if(!removedEntityDrops.containsKey(living.getClass())) continue;
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
