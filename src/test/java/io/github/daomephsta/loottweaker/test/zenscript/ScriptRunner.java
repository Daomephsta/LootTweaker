package io.github.daomephsta.loottweaker.test.zenscript;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;

import com.google.common.io.ByteStreams;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.network.NetworkSide;
import crafttweaker.runtime.CrTTweaker;
import crafttweaker.runtime.providers.ScriptProviderCustom;
import crafttweaker.socket.SingleError;

public class ScriptRunner
{
    public static void run(String scriptPath)
    {
        CrTTweaker tweaker = createTweaker(scriptPath);
        List<SingleError> parseExceptions = new ArrayList<>();
        tweaker.loadScript(false, "crafttweaker", parseExceptions, false);
        if (!parseExceptions.isEmpty())
        {
            for (SingleError error : parseExceptions)
            {
                switch (error.level)
                {
                case INFO:
                    CraftTweakerAPI.logInfo(String.format("%s@%d[%d] %s",
                        error.fileName, error.line, error.offset, error.explanation));
                    break;
                case WARN:
                    CraftTweakerAPI.logWarning(String.format("%s@%d[%d] %s",
                        error.fileName, error.line, error.offset, error.explanation));
                    break;
                case ERROR:
                    CraftTweakerAPI.logError(String.format("%s@%d[%d] %s",
                        error.fileName, error.line, error.offset, error.explanation));
                    break;
                }

            }
            Assertions.fail("There were errors while parsing scripts");
        }
    }

    private static CrTTweaker createTweaker(String scriptPath)
    {
        CrTTweaker tweaker = new CrTTweaker();
        ScriptProviderCustom provider = new ScriptProviderCustom(scriptPath);
        tweaker.setScriptProvider(provider);
        try (InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(scriptPath))
        {
            if (stream == null)
                throw new IllegalArgumentException("No script found at " + scriptPath);
            provider.add(scriptPath, ByteStreams.toByteArray(stream));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        tweaker.setNetworkSide(NetworkSide.SIDE_SERVER);
        return tweaker;
    }
}
