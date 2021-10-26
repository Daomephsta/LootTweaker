package leviathan143.loottweaker.common.zenscript.factory;

import java.util.Map;

import leviathan143.loottweaker.common.zenscript.JsonToLootObject;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;

public class LootConditionFactoryImpl
{
	private final JsonToLootObject.Impl jsonToLootObject;

    public LootConditionFactoryImpl(LootTweakerContext context)
    {
        this.jsonToLootObject = new JsonToLootObject.Impl(context);
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

	public ZenLootConditionWrapper parse(Map<String, ?> json)
	{
	    return jsonToLootObject.asLootCondition(json);
	}

	public ZenLootConditionWrapper zenscript(ZenLambdaLootCondition.Delegate delegate)
	{
	    return new ZenLootConditionWrapper(new ZenLambdaLootCondition(delegate));
	}
}
