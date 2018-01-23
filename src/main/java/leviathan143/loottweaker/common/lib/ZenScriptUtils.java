package leviathan143.loottweaker.common.lib;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.IData;

public class ZenScriptUtils
{
	public static boolean checkIsMap(IData data)
	{
		if(data instanceof DataMap) return true;
		else 
		{
			CraftTweakerAPI.logError("Expected map, recieved " + data);
			return false;
		}
	}
	
	
}
