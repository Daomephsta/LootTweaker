package leviathan143.loottweaker.server;

import java.io.File;

import org.apache.logging.log4j.Level;

import leviathan143.loottweaker.common.CommonProxy;
import leviathan143.loottweaker.common.LootTweakerMain;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.server.FMLServerHandler;

public class ServerProxy extends CommonProxy
{	
    MinecraftServer mcServer = FMLServerHandler.instance().getServer();

    @Override
    public void postInit(FMLPostInitializationEvent event) 
    {
	super.postInit(event);
    }

    public World getWorld()
    {
	if(mcServer.worlds.length == 0)
	{
	    LootTweakerMain.logger.log(Level.WARN, "World does not exist yet");
	    return null;
	}
	return mcServer.getEntityWorld();
    }

    @Override
    public File getMCFolder() 
    {
	return mcServer.getDataDirectory();
    }
}