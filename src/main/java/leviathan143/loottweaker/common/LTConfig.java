package leviathan143.loottweaker.common;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = LootTweaker.MODID)
@Mod.EventBusSubscriber(modid = LootTweaker.MODID)
public class LTConfig
{
	@Comment("Should LootTweaker warn about deprecated methods on world load? Resets if the version of LootTweaker is changed.")
	@LangKey(LootTweaker.MODID + ".config.deprecationWarnings")
	public static boolean deprecationWarnings = true;
	@Comment("Do not touch!")
	@LangKey(LootTweaker.MODID + ".config.lastCfgVersion")
	public static String lastCfgVersion = LootTweaker.VERSION;

	public static void onLoad()
	{
		if(!lastCfgVersion.equals(LootTweaker.VERSION))
		{
			lastCfgVersion = LootTweaker.VERSION;
			deprecationWarnings = true;
		}
		ConfigManager.sync(LootTweaker.MODID, Config.Type.INSTANCE);
	}
	
	@SubscribeEvent
	public static void syncConfig(ConfigChangedEvent e)
	{
		if (e.getModID().equals(LootTweaker.MODID)) ConfigManager.sync(LootTweaker.MODID, Config.Type.INSTANCE);
	}
}
