package leviathan143.loottweaker.common.zenscript;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.world.IWorld;
import daomephsta.loot_shared.utility.loot.fix.LootFixer;
import daomephsta.loot_shared.zenscript.api.LootGenerator;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.world.storage.loot.LootTable;
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
    public static ZenLootTableWrapper newTable(String id)
    {
        return TWEAK_MANAGER.newTable(id);
    }
	
	@ZenMethod
	public static LootGenerator createLootGenerator(IWorld world)
	{
		return TWEAK_MANAGER.createLootGenerator(world); 
	}

    @Mod.EventHandler
    public static void onServerStarting(FMLServerStartingEvent event)
    {
        TWEAK_MANAGER.onServerStart(event.getServer());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTableLoad(LootTableLoadEvent event)
    {
    	// Custom tables don't fire this event
        LootTable table = LootFixer.fixTable(event.getTable(), event.getName(), false);
		event.setTable(TWEAK_MANAGER.tweakTable(event.getName(), table));
    }
}
