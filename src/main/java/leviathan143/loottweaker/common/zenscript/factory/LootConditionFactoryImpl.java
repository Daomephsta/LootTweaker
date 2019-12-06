package leviathan143.loottweaker.common.zenscript.factory;

import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.DataParser;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;

public class LootConditionFactoryImpl
{
    private final DataParser loggingParser;

	public LootConditionFactoryImpl(ErrorHandler errorHandler)
    {
        this.loggingParser = new DataParser(LootTableManagerAccessors.getGsonInstance(), e -> errorHandler.handle(e.getMessage()));
    }

    public ZenLootConditionWrapper randomChance(float chance)
	{
	    return new ZenLootConditionWrapper(new RandomChance(chance));
	}

	public ZenLootConditionWrapper randomChanceWithLooting(float chance, float lootingMultiplier)
	{
	    return new ZenLootConditionWrapper(new RandomChanceWithLooting(chance, lootingMultiplier));
	}

	public ZenLootConditionWrapper killedByPlayer()
	{
	    return new ZenLootConditionWrapper(new KilledByPlayer(false));
	}

	public ZenLootConditionWrapper killedByNonPlayer()
	{
	    return new ZenLootConditionWrapper(new KilledByPlayer(true));
	}

	public ZenLootConditionWrapper parse(IData json)
	{
	    return loggingParser.parse(json, LootCondition.class).map(ZenLootConditionWrapper::new).orElse(ZenLootConditionWrapper.INVALID);
	}
}
