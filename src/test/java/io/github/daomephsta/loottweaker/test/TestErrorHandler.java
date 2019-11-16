package io.github.daomephsta.loottweaker.test;

import javax.inject.Singleton;

import leviathan143.loottweaker.common.ErrorHandler;

@Singleton
public class TestErrorHandler implements ErrorHandler
{
    public static class LootTweakerException extends RuntimeException
    {
        private LootTweakerException(String message)
        {
            super(message);
        }
    }

    @Override
    public void handle(String errorMessageFormat, Object... args)
    {
        throw new LootTweakerException(String.format(errorMessageFormat, args));
    }
}
