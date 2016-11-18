package leviathan143.droptweaker.server;

import org.apache.logging.log4j.Level;

import leviathan143.droptweaker.common.CommonProxy;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.server.FMLServerHandler;

public class ServerProxy extends CommonProxy
{	
	MinecraftServer mcServer = FMLServerHandler.instance().getServer();
	
	public World getWorld()
	{
		if(mcServer.worldServers.length == 0)
		{
			FMLLog.log(Level.WARN, "World does not exist yet");
			return null;
		}
		return mcServer.getEntityWorld();
	}
}