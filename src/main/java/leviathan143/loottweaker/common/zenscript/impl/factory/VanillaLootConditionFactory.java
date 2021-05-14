package leviathan143.loottweaker.common.zenscript.impl.factory;

import leviathan143.loottweaker.common.zenscript.api.entry.LootConditionRepresentation;
import leviathan143.loottweaker.common.zenscript.api.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.impl.entry.LootConditionWrapper;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;

public class VanillaLootConditionFactory implements LootConditionFactory
{
    public VanillaLootConditionFactory(LootTweakerContext context)
    {
        // Leave constructor like this, may need context in future
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
}
