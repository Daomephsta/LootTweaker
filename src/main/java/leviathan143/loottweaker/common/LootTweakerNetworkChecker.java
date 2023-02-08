package leviathan143.loottweaker.common;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.NetworkModHolder;
import net.minecraftforge.fml.common.network.internal.NetworkModHolder.NetworkChecker;
import net.minecraftforge.fml.relauncher.Side;

public class LootTweakerNetworkChecker extends NetworkChecker
{
    private static final Logger LOGGER = LogManager.getLogger(LootTweaker.MODNAME);

    private LootTweakerNetworkChecker(NetworkModHolder parent)
    {
        parent.super();
    }

    @Override
    public boolean check(Map<String, String> modVersions, Side remoteSide)
    {
        return checkCompatible(modVersions, remoteSide) == null;
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
        ModContainer lootTweaker = Loader.instance().getIndexedModList().get(LootTweaker.MODID);
        try
        {
            NetworkModHolder holder = getHolderRegistry().get(lootTweaker);
            Field checkerField = NetworkModHolder.class.getDeclaredField("checker");
            checkerField.setAccessible(true);
            checkerField.set(holder, new LootTweakerNetworkChecker(holder));
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            throw new RuntimeException("Failed to set network checker", e);
        }
        LOGGER.info("Successfully installed network checker");
    }

    @SuppressWarnings("unchecked")
    private static Map<ModContainer, NetworkModHolder> getHolderRegistry()
    {
        try
        {
            Field registryField = NetworkRegistry.class.getDeclaredField("registry");
            registryField.setAccessible(true);
            return (Map<ModContainer, NetworkModHolder>) registryField.get(NetworkRegistry.INSTANCE);
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            throw new RuntimeException("Failed to get holder registry from network registry", e);
        }
    }
}
