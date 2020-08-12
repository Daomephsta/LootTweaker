package leviathan143.loottweaker.common.zenscript.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import crafttweaker.api.event.IEventHandle;
import crafttweaker.util.EventList;
import crafttweaker.util.IEventHandler;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.LootTableDumper;
import leviathan143.loottweaker.common.lib.LootTableFinder;
import leviathan143.loottweaker.common.zenscript.api.LootSystemInterface;
import leviathan143.loottweaker.common.zenscript.api.LootTableLoadCraftTweakerEvent;
import leviathan143.loottweaker.common.zenscript.api.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.api.factory.LootFunctionFactory;
import leviathan143.loottweaker.common.zenscript.impl.factory.VanillaLootConditionFactory;
import leviathan143.loottweaker.common.zenscript.impl.factory.VanillaLootFunctionFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LootTweakerApi implements LootSystemInterface
{
    private final EventList<LootTableLoadCraftTweakerEvent> lootTableLoad = new EventList<>();
    private final ConvenienceHandler convenience = new ConvenienceHandler();
    private final Map<ResourceLocation, LootTableConsumer> tableBuilders = new Object2ObjectArrayMap<>();
    private final LootConditionFactory conditionFactory;
    private final LootFunctionFactory functionFactory;
    private final LootTweakerContext context;
    private final ErrorHandler errorHandler;

    public LootTweakerApi(LootTweakerContext context)
    {
        this.conditionFactory = new VanillaLootConditionFactory(context);
        this.functionFactory = new VanillaLootFunctionFactory(context);
        this.context = context;
        this.errorHandler = context.getErrorHandler();
        lootTableLoad.add(convenience);
    }

    @Override
    public IEventHandle onLootTableLoad(IEventHandler<LootTableLoadCraftTweakerEvent> handler)
    {
        //TODO Figure out how to error when the provided mutable table is used outside the event handler
        return lootTableLoad.add(handler);
    }

    @Override
    public void tweakLootTable(String tableId, LootTableConsumer tweaks)
    {
        //TODO Figure out how to error when the provided mutable table is used outside the event handler
        ResourceLocation tableRL = new ResourceLocation(tableId);
        if (!LootTableFinder.DEFAULT.exists(tableRL))
            errorHandler.error("No loot table with id %s exists!", tableId);
        convenience.tweaks.put(tableId, tweaks);
    }

    @Override
    public void createLootTable(String id, LootTableConsumer tweaks)
    {
        ResourceLocation tableId = new ResourceLocation(id);
        if (tableId.getNamespace().equals("minecraft"))
        {
            if (id.startsWith("minecraft"))
            {
                errorHandler.warn(
                    "Table id '%s' explicitly uses the minecraft namespace, this is discouraged", id);
            }
            else
            {
                errorHandler.warn(
                    "Table id '%s' implicitly uses the minecraft namespace, this is discouraged", id);
            }
        }
        if (LootTableFinder.DEFAULT.exists(tableId)
            || tableBuilders.putIfAbsent(tableId, tweaks) != null)
        {
            errorHandler.error("Table id '%s' already in use", id);
            return;
        }
    }

    public void onServerStarting(FMLServerStartingEvent event)
    {
        MinecraftServer server = event.getServer();
        File worldLootTables = server.getActiveAnvilConverter().getFile(server.getFolderName(), "data/loot_tables");
        LootTableDumper dumper = new LootTableDumper(worldLootTables, LootTableManagerAccessors.getGsonInstance());
        for (Entry<ResourceLocation, LootTableConsumer> builder : tableBuilders.entrySet())
        {
            MutableLootTable mutableTable = new MutableLootTable(builder.getKey(), new HashMap<>(), context);
            builder.getValue().apply(mutableTable);
            dumper.dump(mutableTable.toImmutable(), builder.getKey());
        }
    }

    public LootTable processLootTable(ResourceLocation tableId, LootTable table)
    {
        LootTableLoadCraftTweakerEventImpl ctEvent = new LootTableLoadCraftTweakerEventImpl(tableId, table, context);
        lootTableLoad.publish(ctEvent);
        if (ctEvent.wasTableModified())
            return ctEvent.getModifiedTable();
        return table;
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event)
    {
        event.setTable(processLootTable(event.getName(), event.getTable()));
    }

    @Override
    public LootConditionFactory getLootConditionFactory()
    {
        return conditionFactory;
    }

    @Override
    public LootFunctionFactory getLootFunctionFactory()
    {
        return functionFactory;
    }

    private static class ConvenienceHandler implements IEventHandler<LootTableLoadCraftTweakerEvent>
    {
        private final Multimap<String, LootTableConsumer> tweaks = MultimapBuilder.hashKeys().arrayListValues().build();

        @Override
        public void handle(LootTableLoadCraftTweakerEvent event)
        {
            for (LootTableConsumer tweak : tweaks.get(event.getTableId()))
            {
                tweak.apply(event.getTable());
            }
        }
    }
}
