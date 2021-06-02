package leviathan143.loottweaker.common.lib;

import java.lang.reflect.Array;

import crafttweaker.CraftTweakerAPI;
import leviathan143.loottweaker.common.ErrorHandler;

public class Arguments
{
    private Arguments() {}

    public static boolean nonNull(ErrorHandler errorHandler, Object... args)
    {
        if (args.length % 2 != 0)
            throw new IllegalArgumentException("Expected list of name value pairs");
        boolean argsNonNull = true;
        for (int i = 0; i < args.length; i += 2)
        {
            String name = (String) args[i];
            Object value = args[i + 1];
            if (value == null)
            {
                errorHandler.error("%s > %s cannot be null", CraftTweakerAPI.getScriptFileAndLine(), name);
                argsNonNull = false;
            }
            else if (value.getClass().isArray())
            {
                for (int j = 0; j < Array.getLength(value); j++)
                {
                    if (Array.get(value, j) == null)
                    {
                        errorHandler.error("%s > %s[%d] cannot be null", 
                            CraftTweakerAPI.getScriptFileAndLine(), name, j);
                        argsNonNull = false;
                    }
                }
            }
        }
        return argsNonNull;
    }

}
