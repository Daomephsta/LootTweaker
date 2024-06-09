package leviathan143.loottweaker.common.lib;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.mixin.LootEntryAccessors;
import leviathan143.loottweaker.common.mixin.LootPoolAccessors;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;

public class LootNameFixer
{
    private static final Logger SANITY_LOGGER = LogManager.getLogger(LootTweaker.MODID + ".sanity_checks");
    private final ResourceLocation tableId;
    private int
        poolDiscriminator = 0,
        entryDiscriminator = 0;

    public LootNameFixer(ResourceLocation tableId)
    {
		this.tableId = tableId;
	}

	public void deduplicatePoolName(LootPool pool)
    {
        String newName = pool.getName() + poolDiscriminator;
        logOrThrow(newName,
            "Duplicate pool name '%s' in table '%s'",
            pool.getName(), tableId);
        ((LootPoolAccessors) pool).setName(newName);
    }

    public void fixCustomPoolName(LootPoolAccessors pool)
    {
        String poolName = LootTweaker.MODID + "_fixed_pool_" + poolDiscriminator;
        SANITY_LOGGER.error(
            "Pool with custom flag found in non-custom table '{}'. Renamed to '{}'.\n" +
            "Report this to the loot adder.", tableId, poolName);
        pool.setName(poolName);
    }

    public void deduplicateEntryName(String poolName, LootEntry entry)
    {
        String newName = entry.getEntryName() + entryDiscriminator;
        logOrThrow(newName,
            "Duplicate entry name '%s' in pool '%s' of table '{}'",
            entry.getEntryName(), poolName, tableId);
        ((LootEntryAccessors) entry).setName(newName);
    }

    public void fixCustomEntryName(LootEntry entry)
    {
        String newName = LootTweaker.MODID + "_fixed_entry_" + entryDiscriminator;
        SANITY_LOGGER.error(
            "Entry with custom flag found in non-custom table '{}'. Renamed to '{}'.\n" +
            "Report this to the loot adder.", tableId, newName);
        ((LootEntryAccessors) entry).setName(newName);
    }

    public static void ignoreManualPool(String poolName)
    {
        if (!SANITY_LOGGER.isDebugEnabled())
            return;
        SANITY_LOGGER.debug("Ignored pool {} manually created by\n{}", poolName, getFriendlyStacktrace(4));
    }

    public static void ignoreManualTable()
    {
        if (!SANITY_LOGGER.isDebugEnabled())
            return;
        SANITY_LOGGER.debug("Ignored table manually created by\n{}", getFriendlyStacktrace(4));
    }

    private static StringBuilder getFriendlyStacktrace(int skip)
    {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        // Trim
        stackTrace = ArrayUtils.subarray(stackTrace, skip, stackTrace.length);
        StringBuilder printedStackTrace = new StringBuilder();
        for (StackTraceElement element : stackTrace)
            printedStackTrace.append('\t').append(element).append('\n');
        return printedStackTrace;
    }

    private static void logOrThrow(String newName, String errorFormat, Object... errorArgs)
    {
        String error = String.format(errorFormat, errorArgs);
        if (!(boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
        {
            SANITY_LOGGER.error(
                "{}. Duplicate added as '{}'.\n" +
                "Report this to the loot adder.", error, newName);
        }
        else
            throw new IllegalArgumentException(error);
    }
}