package leviathan143.loottweaker.common;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = LootTweakerMain.MODID)
@Mod.EventBusSubscriber(modid = LootTweakerMain.MODID)
public class LTConfig
{
	@Comment("Should LootTweaker warn about deprecated methods on world load? Resets if the version of LootTweaker is changed.")
	@LangKey(LootTweakerMain.MODID + ".config.deprecationWarnings")
	public static boolean deprecationWarnings = true;
	@Comment("Do not touch!")
	@LangKey(LootTweakerMain.MODID + ".config.lastCfgVersion")
	public static String lastCfgVersion = LootTweakerMain.VERSION;

	public static void onLoad()
	{
		if(!lastCfgVersion.equals(LootTweakerMain.VERSION))
		{
			lastCfgVersion = LootTweakerMain.VERSION;
			deprecationWarnings = true;
		}
		ConfigManager.sync(LootTweakerMain.MODID, Config.Type.INSTANCE);
	}
	
	@SubscribeEvent
	public static void syncConfig(ConfigChangedEvent e)
	{
		if (e.getModID().equals(LootTweakerMain.MODID)) ConfigManager.sync(LootTweakerMain.MODID, Config.Type.INSTANCE);
	}
}
