package leviathan143.loottweaker.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Config(modid = LootTweaker.MODID)
@Mod.EventBusSubscriber(modid = LootTweaker.MODID)
public class LTConfig
{
    private static final Logger LOGGER = LogManager.getLogger(LootTweaker.MODNAME);

    @LangKey(LootTweaker.MODID + ".config.warnings.category")
    public static Warnings warnings = new Warnings();

    public static class Warnings
    {
        @RequiresMcRestart
        @Comment("Warns about deprecated methods on world load. Resets if the version of LootTweaker is changed.")
        @LangKey(LootTweaker.MODID + ".config.warnings.deprecation")
        public boolean deprecation = true;

        @RequiresMcRestart
        @Comment("Warns when newTable() is passed a table id that implictly or explicitly uses the 'minecraft' namescape.")
        @LangKey(LootTweaker.MODID + ".config.warnings.newTableMinecraftNamespace")
        public boolean newTableMinecraftNamespace = true;
    }

    @LangKey(LootTweaker.MODID + ".config.workarounds.category")
    public static Workarounds workarounds = new Workarounds();

    public static class Workarounds
    {
        @Comment("Classes to force initialise during pre-init. Use only if directed to by LootTweaker author.")
        @LangKey(LootTweaker.MODID + ".config.workarounds.forceInitClasses")
        public String[] forceInitClasses = {};
    }

    @Comment("Do not touch!")
    @LangKey(LootTweaker.MODID + ".config.lastCfgVersion")
    public static String lastCfgVersion = LootTweaker.VERSION;

    public static void onLoad()
    {
        if (!lastCfgVersion.equals(LootTweaker.VERSION))
        {
            lastCfgVersion = LootTweaker.VERSION;
            warnings.deprecation = true;
        }
        forceInitClasses();
        ConfigManager.sync(LootTweaker.MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void syncConfig(OnConfigChangedEvent e)
    {
        if (e.getModID().equals(LootTweaker.MODID))
        {
            forceInitClasses();
            ConfigManager.sync(LootTweaker.MODID, Config.Type.INSTANCE);
        }
    }

    private static void forceInitClasses()
    {
        for (String className : workarounds.forceInitClasses)
        {
            try
            {
                Class.forName(className);
                LOGGER.info("{} initialisation ensured", className);
            }
            catch (ClassNotFoundException e)
            {
                LOGGER.error("Could not ensure initialisation of {}", className, e);
            }
        }
    }
}
