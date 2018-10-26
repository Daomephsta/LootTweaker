package leviathan143.loottweaker.common;

import java.util.HashSet;
import java.util.Set;

import crafttweaker.CraftTweakerAPI;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;

/**
 * Handles deprecation warnings. Makes sure they're printed once for each deprecated function to avoid log spam.
 */
@Mod.EventBusSubscriber(modid = LootTweakerMain.MODID)
public class DeprecationWarningManager
{
	private static final Set<String> deprecatedObjectsUsed = new HashSet<String>();

	public static void addWarning()
	{
		if(!LTConfig.deprecationWarnings) return;
		try
		{
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			StackTraceElement caller = stackTrace[2];
			Class<?> clazz = Class.forName(caller.getClassName());
			ZenClass zenClass = clazz.getAnnotation(ZenClass.class);
			String zenName = zenClass.value();
			deprecatedObjectsUsed.add(zenName + "." + caller.getMethodName());
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	public static void printDeprecationWarnings()
	{
		if(deprecatedObjectsUsed.isEmpty() || !LTConfig.deprecationWarnings) return;
		warnings = 0;
		CraftTweakerAPI.logWarning(I18n.format(LootTweakerMain.MODID + ".messages.error.deprecation.warning1"));
		for(String deprecatedObj : deprecatedObjectsUsed)
		{
			CraftTweakerAPI.logInfo(I18n.format(LootTweakerMain.MODID + ".messages.error.deprecation.warning2", deprecatedObj));
		}
		CraftTweakerAPI.logInfo(I18n.format(LootTweakerMain.MODID + ".messages.error.deprecation.warning3"));
	}

	//Why you make me do this, CraftTweaker? :..(
	private static int warnings = 0;
	@SubscribeEvent
	public static void fixChatSpam(ClientChatReceivedEvent e)
	{
		if(e.getMessage().getFormattedText().contains("LootTweaker"))
		{
			warnings++;
			if(warnings > 1) e.setCanceled(true);
		}
	}
}
