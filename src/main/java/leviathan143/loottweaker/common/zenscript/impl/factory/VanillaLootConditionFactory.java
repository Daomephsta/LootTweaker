package leviathan143.loottweaker.common.zenscript.impl.factory;

import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.DataParser;
import leviathan143.loottweaker.common.zenscript.api.entry.LootConditionRepresentation;
import leviathan143.loottweaker.common.zenscript.api.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.impl.entry.LootConditionWrapper;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;

public class VanillaLootConditionFactory implements LootConditionFactory
{
    private final DataParser loggingParser;

    public VanillaLootConditionFactory(LootTweakerContext context)
    {
        this.loggingParser = createDataParser(context.getErrorHandler());
    }

    private DataParser createDataParser(ErrorHandler errorHandler)
    {
        return new DataParser(LootTableManagerAccessors.getGsonInstance(), e -> errorHandler.error(e.getMessage()));
    }

    @Override
    public LootConditionRepresentation randomChance(float chance)
    {
        return new LootConditionWrapper(new RandomChance(chance));
    }

    @Override
    public LootConditionRepresentation randomChanceWithLooting(float chance, float lootingMultiplier)
    {
        return new LootConditionWrapper(new RandomChanceWithLooting(chance, lootingMultiplier));
    }

    @Override
    public LootConditionRepresentation killedByPlayer()
    {
        return new LootConditionWrapper(new KilledByPlayer(false));
    }

    @Override
    public LootConditionRepresentation killedByNonPlayer()
    {
        return new LootConditionWrapper(new KilledByPlayer(true));
    }

    @Override
    public LootConditionRepresentation parse(IData json)
    {
        return loggingParser.parse(json, LootCondition.class)
            .map(LootConditionWrapper::new)
            .orElse(LootConditionWrapper.INVALID);
    }
}
