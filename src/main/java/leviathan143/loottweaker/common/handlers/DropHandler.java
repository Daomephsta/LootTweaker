package leviathan143.loottweaker.common.handlers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import leviathan143.loottweaker.common.LootUtils;
import leviathan143.loottweaker.common.loot.block.BlockLootHandler;
import leviathan143.loottweaker.common.loot.block.LootContextBlock;
import leviathan143.loottweaker.common.loot.block.LootContextBlock.Builder;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropHandler
{
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
			int stateID = Block.getStateId(event.getState());
			if(!removedBlockDrops.containsKey(stateID)) continue;
			for(ItemStack removedStack : removedBlockDrops.get(stateID))
			{
				if(ItemStack.areItemsEqual(stack, removedStack))
				{
					iter.remove();
					break;
				}
			}
		}
		ResourceLocation tableLoc = LootUtils.getBlockLootTableFromRegistryName(event.getState().getBlock().getRegistryName());
		if(!BlockLootHandler.getBlockLootTables().contains(tableLoc)) return;
		Builder lootCtxBuilder = new LootContextBlock.Builder((WorldServer) event.getWorld())
																.withPlayer(event.getHarvester())
																.withState(event.getState())
																.withFortune(event.getFortuneLevel())
																.withSilkTouch(event.isSilkTouching());
		if(event.getHarvester() != null) lootCtxBuilder.withLuck(event.getHarvester().getLuck());
		LootTable table = BlockLootHandler.getBlockLootTableManager().getLootTableFromLocation(tableLoc);
		System.out.println(table);
		//List drops = table.generateLootForPools(new Random(event.getWorld().rand.nextLong()), lootCtxBuilder.build());
		//event.getDrops().addAll(drops);
	}

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
				ItemStack stack = iter.next().getEntityItem();
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
