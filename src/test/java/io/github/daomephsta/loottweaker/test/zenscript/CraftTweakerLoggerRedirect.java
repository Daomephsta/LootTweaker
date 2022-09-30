package io.github.daomephsta.loottweaker.test.zenscript;

import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import crafttweaker.CrafttweakerImplementationAPI;
import crafttweaker.api.player.IPlayer;
import crafttweaker.runtime.ILogger;


public class CraftTweakerLoggerRedirect implements BeforeAllCallback, AfterAllCallback
{
    private static final String ADAPTER_KEY = "CraftTweakerLoggerRedirect.adapter";
    private final Logger logger;

    public CraftTweakerLoggerRedirect(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception
    {
        Store dataStore = context.getStore(Namespace.GLOBAL);
        Adapter adapter = new Adapter(logger);
        dataStore.put(ADAPTER_KEY, adapter);
        CrafttweakerImplementationAPI.logger.addLogger(adapter);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception
    {
        Store dataStore = context.getStore(Namespace.GLOBAL);
        CrafttweakerImplementationAPI.logger.removeLogger((ILogger) dataStore.get(ADAPTER_KEY));
    }

    private static class Adapter implements ILogger
    {
        private static final Pattern FORMATTING_CODE_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");
        private boolean defaultLevelDisabled = false;
        private final Logger delegate;

        private Adapter(Logger delegate)
        {
            this.delegate = delegate;
        }

        @Override
        public void logCommand(String message)
        {
            delegate.info(() -> stripFormatting(message));
        }

        @Override
        public void logInfo(String message)
        {
            delegate.info(() -> stripFormatting(message));
        }

        @Override
        public void logWarning(String message)
        {
            delegate.warn(() -> stripFormatting(message));
        }

        @Override
        public void logError(String message)
        {
            throw new ScriptException(stripFormatting(message));
        }

        @Override
        public void logError(String message, Throwable exception)
        {
            throw new ScriptException(stripFormatting(message), exception);
        }

        @Override
        public void logPlayer(IPlayer player)
        {
            //NO-OP What even is this supposed to do?
        }

        private String stripFormatting(String input)
        {
            return FORMATTING_CODE_PATTERN.matcher(input).replaceAll("");
        }

        @Override
        public void logDefault(String message)
        {
            if (!isLogDisabled()) logInfo(message);
        }

        @Override
        public boolean isLogDisabled()
        {
            return defaultLevelDisabled;
        }

        @Override
        public void setLogDisabled(boolean logDisabled)
        {
            this.defaultLevelDisabled = logDisabled;
        }
    }

    public static class ScriptException extends RuntimeException
    {
        private ScriptException(String message, Throwable cause)
        {
            super(message, cause);
        }

        private ScriptException(String message)
        {
            super(message);
        }
    }
}
