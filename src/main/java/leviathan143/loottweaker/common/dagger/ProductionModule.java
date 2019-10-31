package leviathan143.loottweaker.common.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import leviathan143.loottweaker.common.CTLoggingErrorHandler;
import leviathan143.loottweaker.common.ErrorHandler;

@Module
public class ProductionModule
{
    @Provides
    @Singleton
    static ErrorHandler provideErrorHandler()
    {
        return new CTLoggingErrorHandler();
    }
}
