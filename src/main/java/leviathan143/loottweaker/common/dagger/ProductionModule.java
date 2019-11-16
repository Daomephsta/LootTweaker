package leviathan143.loottweaker.common.dagger;

import dagger.Module;
import dagger.Provides;
import leviathan143.loottweaker.common.CTLoggingErrorHandler;
import leviathan143.loottweaker.common.ErrorHandler;

@Module
public class ProductionModule
{
    @Provides
    static ErrorHandler errorHandler()
    {
        return new CTLoggingErrorHandler();
    }
}
