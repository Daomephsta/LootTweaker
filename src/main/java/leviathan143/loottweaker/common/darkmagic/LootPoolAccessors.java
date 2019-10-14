package leviathan143.loottweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;
import java.util.List;

import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootPoolAccessors extends AbstractAccessors
{
	private static final MethodHandle lootPool$poolConditionsGetter;

	static
	{
		try
		{
			lootPool$poolConditionsGetter = createFieldGetter(LootPool.class, "field_186454_b");
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException("Failed to initialize LootPool accessor method handles", e);
		}
	}
	
	public static List<LootCondition> getConditions(LootPool pool)
	{
		try
		{
			return (List<LootCondition>) lootPool$poolConditionsGetter.invokeExact(pool);
		}
		catch (Throwable t)
		{
			throw new RuntimeException("Could not invoke loot table pool conditions getter method handle", t);
		}
	}
}
