package leviathan143.loottweaker.common.zenscript;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.DeprecationWarningManager;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootTweaker")
@Mod.EventBusSubscriber(modid = LootTweaker.MODID)
public class ZenLootTableTweakManager
{
    private static final LootTableTweakManager TWEAK_MANAGER = LootTweaker.CONTEXT.createLootTableTweakManager();

    @ZenMethod
    public static ZenLootTableWrapper getTable(String tableName)
    {
        return TWEAK_MANAGER.getTable(tableName);
    }

    @ZenMethod
    public static ZenLootTableWrapper getTableUnchecked(String tableName)
    {
        DeprecationWarningManager.addWarning();
        return TWEAK_MANAGER.getTableUnchecked(tableName);
    }

    @ZenMethod
    public static ZenLootTableWrapper newTable(String id)
    {
        return TWEAK_MANAGER.newTable(id);
    }

    @Mod.EventHandler
    public static void onServerStarting(FMLServerStartingEvent event)
    {
        TWEAK_MANAGER.onServerStart(event.getServer());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTableLoad(LootTableLoadEvent event)
    {
        event.setTable(TWEAK_MANAGER.tweakTable(event.getName(), event.getTable()));
    }
}
