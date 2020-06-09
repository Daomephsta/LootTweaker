package leviathan143.loottweaker.common;

import java.util.HashSet;
import java.util.Set;

import crafttweaker.CraftTweakerAPI;
import net.minecraftforge.fml.common.Mod;
import stanhebben.zenscript.annotations.ZenClass;

/**
 * Handles deprecation warnings. Makes sure they're printed once for each deprecated function to avoid log spam.
 */
@Mod.EventBusSubscriber(modid = LootTweaker.MODID)
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
			if (zenClass == null)
			    throw new IllegalArgumentException(clazz.getName() + " is not a ZenClass");
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
		CraftTweakerAPI.logWarning("LootTweaker: One or more scripts use deprecated methods. Check crafttweaker.log for more information.");
		for(String deprecatedObj : deprecatedObjectsUsed)
		{
			CraftTweakerAPI.logInfo(String.format("%s is deprecated", deprecatedObj));
		}
		CraftTweakerAPI.logInfo("Refer to https://loottweaker-docs.readthedocs.io/en/latest/reference/deprecations.html for more info. This warning can be disabled in the config.");
	}
}
