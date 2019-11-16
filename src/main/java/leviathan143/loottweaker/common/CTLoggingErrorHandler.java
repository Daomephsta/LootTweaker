package leviathan143.loottweaker.common;

import javax.inject.Singleton;

import crafttweaker.CraftTweakerAPI;

@Singleton
public class CTLoggingErrorHandler implements ErrorHandler
{
    @Override
    public void handle(String errorMessageFormat, Object... args)
    {
        CraftTweakerAPI.logError(String.format(errorMessageFormat, args));
    }
}
