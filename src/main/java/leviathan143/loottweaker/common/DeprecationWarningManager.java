package leviathan143.loottweaker.common;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import crafttweaker.CraftTweakerAPI;

/**
 * Handles deprecation warnings. Makes sure they're printed onece for each deprecated function to avoid log spam.
 */
public class DeprecationWarningManager
{
	//Deprecated stuff used and the accompanying warning to be printed
	private static final Set<Pair<String, String>> deprecatedObjectsUsed = new HashSet<Pair<String, String>>();
	
	public static void addWarning(String deprecatedObjName, String warning)
	{
		deprecatedObjectsUsed.add(Pair.of(deprecatedObjName, warning));
	}
	
	public static void printDeprecationWarnings()
	{
		for(Pair<String, String> warning : deprecatedObjectsUsed)
		{
			CraftTweakerAPI.logWarning(warning.getValue());
		}
	}
}
