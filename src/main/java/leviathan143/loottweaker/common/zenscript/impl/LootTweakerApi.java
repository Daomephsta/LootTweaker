package leviathan143.loottweaker.common.zenscript.impl;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import crafttweaker.api.event.IEventHandle;
import crafttweaker.util.EventList;
import crafttweaker.util.IEventHandler;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.lib.LootTableFinder;
import leviathan143.loottweaker.common.zenscript.api.LootSystemInterface;
import leviathan143.loottweaker.common.zenscript.api.LootTableLoadCraftTweakerEvent;
import leviathan143.loottweaker.common.zenscript.api.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.impl.factory.VanillaLootConditionFactory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LootTweakerApi implements LootSystemInterface
{
    private final EventList<LootTableLoadCraftTweakerEvent> lootTableLoad = new EventList<>();
    private final ConvenienceHandler convenience = new ConvenienceHandler();
    private final LootConditionFactory conditionFactory;
    private final LootTweakerContext context;
    private final ErrorHandler errorHandler;

    public LootTweakerApi(LootTweakerContext context)
    {
        this.conditionFactory = new VanillaLootConditionFactory(context);
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
