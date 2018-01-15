package leviathan143.loottweaker.client;

import leviathan143.loottweaker.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy
{
	Minecraft mc = Minecraft.getMinecraft();

	@Override
	public World getWorld()
	{
		if (mc.isIntegratedServerRunning())
		{
			return mc.getIntegratedServer().getEntityWorld();
		}
		else return mc.world;
	}
}
