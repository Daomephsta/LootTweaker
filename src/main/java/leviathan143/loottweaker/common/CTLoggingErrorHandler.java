package leviathan143.loottweaker.common;

import crafttweaker.CraftTweakerAPI;

public class CTLoggingErrorHandler implements ErrorHandler
{
    @Override
    public void handle(String errorMessageFormat, Object... args)
    {
        CraftTweakerAPI.logError(String.format(errorMessageFormat, args));
    }
}
