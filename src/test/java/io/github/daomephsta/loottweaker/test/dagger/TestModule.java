package io.github.daomephsta.loottweaker.test.dagger;

import dagger.Module;
import dagger.Provides;
import io.github.daomephsta.loottweaker.test.TestErrorHandler;
import leviathan143.loottweaker.common.ErrorHandler;

@Module
public class TestModule
{
    @Provides
    static ErrorHandler provideErrorHandler()
    {
        return new TestErrorHandler();
    }
}
