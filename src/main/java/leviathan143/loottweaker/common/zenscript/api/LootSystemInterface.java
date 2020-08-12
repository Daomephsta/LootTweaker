package leviathan143.loottweaker.common.zenscript.api;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.event.IEventHandle;
import crafttweaker.util.IEventHandler;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.api.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.api.factory.LootFunctionFactory;
import net.minecraftforge.fml.common.Mod;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootTweaker")
@Mod.EventBusSubscriber(modid = LootTweaker.MODID)
public interface LootSystemInterface
{
    @ZenMethod
    public IEventHandle onLootTableLoad(IEventHandler<LootTableLoadCraftTweakerEvent> handler);

    @ZenMethod
    public void tweakLootTable(String id, LootTableConsumer tweaks);

    @ZenMethod
    public void createLootTable(String id, LootTableConsumer tweaks);

    @ZenRegister
    @ZenClass(LootTweaker.MODID + ".LootTableConsumer")
    public interface LootTableConsumer
    {
        public void apply(LootTableRepresentation lootTable);
    }

    @ZenGetter("conditions")
    public LootConditionFactory getLootConditionFactory();

    @ZenGetter("functions")
    public LootFunctionFactory getLootFunctionFactory();
}
