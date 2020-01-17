package io.github.daomephsta.loottweaker.test;

import leviathan143.loottweaker.common.ErrorHandler;

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
    public void error(String format, Object... args)
    {
        throw new LootTweakerException(String.format(format, args));
    }

    @Override
    public void error(String message)
    {
        throw new LootTweakerException(message);
    }

    @Override
    public void warn(String format, Object... args)
    {
        throw new LootTweakerException(String.format(format, args));
    }

    @Override
    public void warn(String message)
    {
        throw new LootTweakerException(message);
    }
}
