package leviathan143.loottweaker.common;

import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Constants.MODID)
@Mod.EventBusSubscriber(modid = Constants.MODID)
public class LTConfig
{
	@Comment("Should LootTweaker warn about deprecated methods on world load. Reset if the version of LootTweaker is changed.")
	public static boolean deprecationWarnings = true;
	@Comment("Do not touch!")
	public static String lastCfgVersion = Constants.VERSION;

	public static void onLoad()
	{
		if(!lastCfgVersion.equals(Constants.VERSION))
		{
			lastCfgVersion = Constants.VERSION;
			deprecationWarnings = true;
		}
		ConfigManager.sync(Constants.MODID, Config.Type.INSTANCE);
	}
	
	@SubscribeEvent
	public static void syncConfig(ConfigChangedEvent e)
	{
		if (e.getModID().equals(Constants.MODID)) ConfigManager.sync(Constants.MODID, Config.Type.INSTANCE);
	}
}
