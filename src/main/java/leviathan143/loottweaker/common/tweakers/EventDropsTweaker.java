package leviathan143.loottweaker.common.tweakers;

import java.util.Collections;

import com.google.common.collect.Lists;

import leviathan143.loottweaker.common.handlers.DropHandler;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.MineTweakerImplementationAPI.ReloadEvent;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.util.IEventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.droptweaker.Drops")
public class EventDropsTweaker
{
	@ZenMethod
	public static void removeEntityDrop(String entityName, IItemStack drop)
	{
		Class<? extends Entity> entityClass = EntityList.NAME_TO_CLASS.get(entityName);
		if(DropHandler.removedEntityDrops.containsKey(entityClass))
		{
			Collections.addAll(DropHandler.removedEntityDrops.get(entityClass), MineTweakerMC.getItemStack(drop));
		}
		else
		{
			
			DropHandler.removedEntityDrops.put(entityClass, Lists.newArrayList(MineTweakerMC.getItemStack(drop)));
		}
	}
	
	public static void onRegister()
	{
		MineTweakerImplementationAPI.onReloadEvent(new IEventHandler<ReloadEvent>() 
		{
			@Override
			public void handle(ReloadEvent paramT) 
			{
				DropHandler.removedEntityDrops.clear();
			}
		});
	}
}
