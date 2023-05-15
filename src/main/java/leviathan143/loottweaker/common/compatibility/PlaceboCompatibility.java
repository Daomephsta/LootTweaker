package leviathan143.loottweaker.common.compatibility;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.Loader;

public class PlaceboCompatibility
{
    private static final Map<ResourceLocation, LootTable> PLACEBO_TABLES = findPlaceboTableMap();
    private static final Logger LOGGER = LogManager.getLogger();

    public static boolean tableExists(ResourceLocation tableId)
    {
        return PLACEBO_TABLES.containsKey(tableId);
    }

    public static Set<ResourceLocation> getAll()
    {
        return PLACEBO_TABLES.keySet();
    }

    private static Map<ResourceLocation, LootTable> findPlaceboTableMap()
    {
        if (Loader.isModLoaded("placebo"))
        {
            try
            {
                Class<?> placeboLootSystem = Class.forName("shadows.placebo.loot.PlaceboLootSystem");
                Field placeboTablesField = placeboLootSystem.getField("PLACEBO_TABLES");
                @SuppressWarnings("unchecked")
                Map<ResourceLocation, LootTable> placeboTables = (Map<ResourceLocation, LootTable>) placeboTablesField.get(null);
                return placeboTables;
            }
            catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.warn("LootTweaker's Placebo compat does not support this Placebo version, report to LootTweaker", e);
            }
        }
        return Collections.emptyMap();
    }
}
