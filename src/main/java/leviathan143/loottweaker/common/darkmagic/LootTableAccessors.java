package leviathan143.loottweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;
import java.util.List;

import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;

public class LootTableAccessors extends AbstractAccessors
{
	private static final MethodHandle lootTable$poolsGetter;

	static
	{
		try
		{
			lootTable$poolsGetter = createFieldGetter(LootTable.class, "field_186466_c", "pools");
		}
		catch (Throwable t)
		{
			throw new RuntimeException("Failed to initialize LootTable accessor method handles", t);
		}
	}

	public static List<LootPool> getPools(LootTable table)
	{
		try
		{
			return (List<LootPool>) lootTable$poolsGetter.invokeExact(table);
		}
		catch (Throwable t)
		{
			throw new RuntimeException("Could not invoke loot table pool getter method handle", t);
		}
	}
}
