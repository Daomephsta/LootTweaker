package leviathan143.loottweaker.client;

import java.io.File;

import leviathan143.loottweaker.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy
{
	Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}

	@Override
	public World getWorld()
	{
		if (mc.isIntegratedServerRunning())
		{
			return mc.getIntegratedServer().getEntityWorld();
		}
		else return mc.world;
	}

	@Override
	public File getMCFolder()
	{
		return mc.mcDataDir;
	}
}
