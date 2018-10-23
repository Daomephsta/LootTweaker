package leviathan143.loottweaker.server;

import org.apache.logging.log4j.*;

import leviathan143.loottweaker.common.CommonProxy;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.FMLServerHandler;

public class ServerProxy extends CommonProxy
{
	private static final Logger logger = LogManager.getLogger();
	private static final MinecraftServer mcServer = FMLServerHandler.instance().getServer();

	@Override
	public World getWorld()
	{
		if (mcServer.worlds.length == 0)
		{
			logger.log(Level.WARN, "World does not exist yet");
			return null;
		}
		return mcServer.getEntityWorld();
	}
}