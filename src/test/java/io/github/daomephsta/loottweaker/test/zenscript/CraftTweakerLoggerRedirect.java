package io.github.daomephsta.loottweaker.test.zenscript;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import crafttweaker.CrafttweakerImplementationAPI;
import crafttweaker.api.player.IPlayer;
import crafttweaker.runtime.ILogger;

public class CraftTweakerLoggerRedirect implements BeforeAllCallback, AfterAllCallback, AfterTestExecutionCallback
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
    public void afterTestExecution(ExtensionContext context) throws Exception
    {
        Store dataStore = context.getStore(Namespace.GLOBAL);
        Adapter adapter = (Adapter) dataStore.get(ADAPTER_KEY);
        boolean success = adapter.warnings.isEmpty()
            && adapter.errors.isEmpty()
            && adapter.errorsWithThrowables.isEmpty();

        while (!adapter.warnings.isEmpty())
            logger.warn(adapter.warnings.remove());
        while (!adapter.errors.isEmpty())
            logger.error(adapter.errors.remove());
        while (!adapter.errorsWithThrowables.isEmpty())
        {
            Pair<String, Throwable> error = adapter.errorsWithThrowables.remove();
            logger.error(error.getKey(), error.getValue());
        }

        if (!success)
            Assertions.fail("Script generated errors and/or warnings");
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
        private final Queue<String> warnings = new ArrayDeque<>();
        private final Queue<String> errors = new ArrayDeque<>();
        private final Queue<Pair<String, Throwable>> errorsWithThrowables = new ArrayDeque<>();

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
            warnings.add(stripFormatting(message));
        }

        @Override
        public void logError(String message)
        {
            errors.add(stripFormatting(message));
        }

        @Override
        public void logError(String message, Throwable exception)
        {
            errorsWithThrowables.add(Pair.of(stripFormatting(message), exception));
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
            if (!isLogDisabled())
                logInfo(message);
        }

        @Override
        public boolean isLogDisabled()
        {
            return defaultLevelDisabled;
        }

        @Override
        public void setLogDisabled(boolean logDisabled)
        {
            this.defaultLevelDisabled  = logDisabled;
        }
    }
}
