package leviathan143.loottweaker.common.lib;

import static com.google.common.base.Predicates.not;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;

import leviathan143.loottweaker.common.compatibility.PlaceboCompatibility;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;


public class LootTableFinder
{
    public static final LootTableFinder DEFAULT = new LootTableFinder();
    private static final Logger LOGGER = LogManager.getLogger();
    private final Set<ResourceLocation> fileBacked = new HashSet<>();
    private boolean fullScanPerformed = false;

    public boolean exists(ResourceLocation tableId)
    {
        if (fileBacked.contains(tableId) ||
            LootTableList.getAll().contains(tableId) ||
            PlaceboCompatibility.tableExists(tableId))
            return true;

        //Cache
        String assetLocation = "assets/" + tableId.getNamespace() + "/loot_tables/" + tableId.getPath() + ".json";
        if (getClass().getClassLoader().getResource(assetLocation) != null)
        {
            add(tableId);
            return true;
        }
        else
            return false;
    }

    private boolean add(ResourceLocation tableId)
    {
        return fileBacked.add(tableId);
    }

    public boolean fullScanPerformed()
    {
        return fullScanPerformed;
    }

    public Iterable<ResourceLocation> findAll()
    {
        if (!fullScanPerformed)
        {
            LOGGER.info("Locating all existing loot tables");
            ClassLoader modClassLoader = Loader.instance().getModClassLoader();
            Set<Path> visitedSources = new HashSet<>();
            for (ModContainer mod : Loader.instance().getActiveModList())
            {
                //Skip mods with bogus JAR locations
                if (mod.getSource() == null)
                {
                    LOGGER.info("Skipped {} ({}) as it reported a null source", mod.getModId(), mod.getName());
                    continue;
                }
                if (!mod.getSource().exists())
                {
                    //Log at debug level instead of info when it's MCP or Minecraft, to avoid user confusion
                    Level level = mod.getModId().equals("minecraft") || mod.getModId().equals("mcp")
                        ? Level.DEBUG
                        : Level.INFO;
                    LOGGER.log(level, "Skipped {} ({}) as it reported a nonexistent source {}", mod.getModId(),
                        mod.getName(), mod.getSource().getAbsolutePath());
                    continue;
                }
                Path sourcePath = mod.getSource().toPath();
                //Skip already visited sources
                if (visitedSources.contains(sourcePath)) continue;
                LOGGER.debug("Visiting source of {} at {}", mod.getModId(), mod.getSource());
                visitSource(modClassLoader, sourcePath, this::add);
                visitedSources.add(sourcePath);
            }
            LOGGER.info("All existing loot tables located");
            fullScanPerformed = true;
        }
        return Stream.of(fileBacked, LootTableList.getAll(), PlaceboCompatibility.getAll())
            .reduce(Sets::union)
            .get(); // Optional cannot be empty as the stream is non-empty
    }

    private void visitSource(ClassLoader modClassLoader, Path sourcePath, Consumer<ResourceLocation> idSubmitter)
    {
        try
        {
            if (Files.isDirectory(sourcePath))
            {
                FileSystem fs = FileSystems.getDefault();
                Path assetsDir = fs.getPath("assets");
                if (!Files.exists(assetsDir)) return;
                walkAssetsDirectory(assetsDir, idSubmitter);
            }
            try (FileSystem fs = getFileSystem(modClassLoader, sourcePath))
            {
                Path assetsDir = fs.getPath("assets");
                if (!Files.exists(assetsDir)) return;
                walkAssetsDirectory(assetsDir, idSubmitter);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private FileSystem getFileSystem(ClassLoader modClassLoader, Path sourcePath) throws IOException
    {
        return !Files.isDirectory(sourcePath)
            ? FileSystems.newFileSystem(sourcePath, modClassLoader)
            : FileSystems.getDefault();
    }

    private void walkAssetsDirectory(Path assetsDir, Consumer<ResourceLocation> idSubmitter) throws IOException
    {
        for (Path domain : (Iterable<Path>) Files.walk(assetsDir, 1).filter(not(assetsDir::equals))::iterator)
        {
            Path lootTablesDir = domain.resolve("loot_tables");
            if (!Files.exists(lootTablesDir)) continue;
            walkLootTablesDirectory(lootTablesDir, idSubmitter);
        }
    }

    private void walkLootTablesDirectory(Path lootTablesDir, Consumer<ResourceLocation> idSubmitter)
        throws IOException
    {
        for (Path lootTable : (Iterable<Path>) Files.walk(lootTablesDir)
            .filter(not(lootTablesDir::equals))::iterator)
        {
            //Just to be extra sure it's a loot table
            if (!FilenameUtils.getExtension(lootTable.getFileName().toString()).equals("json")) continue;
            String namespace = lootTablesDir.getName(1).toString();
            String path = FilenameUtils.removeExtension(lootTablesDir.relativize(lootTable).toString());
            idSubmitter.accept(new ResourceLocation(namespace, path));
        }
    }
}
