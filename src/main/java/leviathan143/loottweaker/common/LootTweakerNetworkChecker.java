package leviathan143.loottweaker.common;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import daomephsta.loot_shared.CustomNetworkChecker;
import net.minecraftforge.fml.common.network.internal.NetworkModHolder;
import net.minecraftforge.fml.relauncher.Side;

public class LootTweakerNetworkChecker extends CustomNetworkChecker
{
    private static final Logger LOGGER = LogManager.getLogger(LootTweaker.MODNAME);

    private LootTweakerNetworkChecker(NetworkModHolder parent)
    {
        super(parent);
    }

    @Override
    public String checkCompatible(Map<String, String> modVersions, Side remoteSide)
    {
        //Reject vanilla clients or servers
        if (!modVersions.containsKey("forge"))
            return "Rejected vanilla install";
        String remoteLTVersion = modVersions.get(LootTweaker.MODID);
        //Client without can connect to server with, but not vice versa
        if (remoteLTVersion == null)
        {
            if (remoteSide == Side.CLIENT)
            {
                LOGGER.info("Accepted non-existent client LootTweaker install");
                return null;
            }
            else
                return "Rejected non-existent server LootTweaker install";
        }
        //Network compatibility is not guaranteed between versions
        if (!remoteLTVersion.equals(LootTweaker.VERSION))
        {
            return String.format("Rejected %s LootTweaker install because its version %s differs from local version %s",
                remoteSide.name().toLowerCase(), remoteLTVersion, LootTweaker.VERSION);
        }
        return null;
    }

    public static void install()
    {
        install(LootTweakerNetworkChecker::new, LootTweaker.MODID);
    }
}
