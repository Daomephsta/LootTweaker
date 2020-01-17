package leviathan143.loottweaker.common;

import crafttweaker.CraftTweakerAPI;

public class CTLoggingErrorHandler implements ErrorHandler
{
    @Override
    public void error(String format, Object... args)
    {
        CraftTweakerAPI.logError(String.format(format, args));
    }

    @Override
    public void error(String message)
    {
        CraftTweakerAPI.logError(message);
    }

    @Override
    public void warn(String format, Object... args)
    {
        CraftTweakerAPI.logWarning(String.format(format, args));
    }

    @Override
    public void warn(String message)
    {
        CraftTweakerAPI.logWarning(message);
    }
}
