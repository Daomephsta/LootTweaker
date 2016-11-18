package leviathan143.loottweaker.common;

import leviathan143.loottweaker.common.tweakers.RegisterTweakers;
import leviathan143.loottweaker.common.tweakers.loot.LootTableTweaker;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

public class CommonProxy 
{	
	public void preInit(FMLPreInitializationEvent event)
	{
		registerRenders();
	}

	public void init(FMLInitializationEvent event)
	{
		RegisterTweakers.register();
	}

	public void postInit(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(LootTableTweaker.class);
	}

	public void registerRenders()
	{

	}

	public World getWorld()
	{
		return null;
	}

	public void serverStarted(FMLServerStartedEvent event) 
	{
		//System.out.println(CommonMethodHandles.getLootConditionGSON().toJson(new KilledByPlayer(false)));
		//System.out.println(CommonMethodHandles.getLootConditionGSON().fromJson("{\"inverse\":false,\"condition\":\"minecraft:killed_by_player\"}", LootCondition.class));
	}
}
