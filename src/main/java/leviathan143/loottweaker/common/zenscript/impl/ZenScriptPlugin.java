package leviathan143.loottweaker.common.zenscript.impl;

import crafttweaker.CraftTweakerAPI;
import leviathan143.loottweaker.common.CTLoggingErrorHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import stanhebben.zenscript.symbols.IZenSymbol;

public class ZenScriptPlugin
{
    private static final LootTweakerContext CONTEXT = new LootTweakerContext(new CTLoggingErrorHandler());
    public static final LootTweakerApi API_INSTANCE = new LootTweakerApi(CONTEXT);

    public static void setup()
    {
        MinecraftForge.EVENT_BUS.register(API_INSTANCE);
        IZenSymbol apiSymbol = CraftTweakerAPI.getJavaStaticFieldSymbol(ZenScriptPlugin.class, "API_INSTANCE");
        CraftTweakerAPI.registerGlobalSymbol("lootTweakerApi", apiSymbol);
    }

    public static void onServerStarting(FMLServerStartingEvent event)
    {
        API_INSTANCE.onServerStarting(event);
    }
}
