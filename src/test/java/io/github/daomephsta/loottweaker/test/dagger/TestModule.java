package io.github.daomephsta.loottweaker.test.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.daomephsta.loottweaker.test.TestErrorHandler;
import leviathan143.loottweaker.common.ErrorHandler;

@Module
public class TestModule
{
    @Provides
    @Singleton
    static ErrorHandler provideErrorHandler()
    {
        return new TestErrorHandler();
    }
}
