package io.github.daomephsta.loottweaker.test;

import leviathan143.loottweaker.common.ErrorHandler;

/**
 * Error handler that throws all errors as exceptions. Used in tests to
 * check that errors occur when expected.
 *
 * @author Daomephsta
 *
 */
public class ThrowingErrorHandler implements ErrorHandler
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
