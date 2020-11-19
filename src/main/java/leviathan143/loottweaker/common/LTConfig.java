package leviathan143.loottweaker.common;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Config(modid = LootTweaker.MODID)
@Mod.EventBusSubscriber(modid = LootTweaker.MODID)
public class LTConfig
{
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

    @RequiresMcRestart
    @Comment("Enables additional features to assist pack development")
    @LangKey(LootTweaker.MODID + ".config.packdevMode")
    public static boolean packdevMode = false;

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
        ConfigManager.sync(LootTweaker.MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void syncConfig(ConfigChangedEvent e)
    {
        if (e.getModID().equals(LootTweaker.MODID))
            ConfigManager.sync(LootTweaker.MODID, Config.Type.INSTANCE);
    }
}
