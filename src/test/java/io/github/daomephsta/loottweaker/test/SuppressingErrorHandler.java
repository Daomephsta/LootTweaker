package io.github.daomephsta.loottweaker.test;

import leviathan143.loottweaker.common.ErrorHandler;

/**
 * Error handler that suppresses all errors. Used in tests to
 * check that non-void methods return appropriate fallback values
 * in the event of an error.
 *
 * @author Daomephsta
 *
 */
public class SuppressingErrorHandler implements ErrorHandler
{
    @Override
    public void error(String format, Object... args) {}

    @Override
    public void error(String message) {}

    @Override
    public void warn(String format, Object... args) {}

    @Override
    public void warn(String message) {}
}
