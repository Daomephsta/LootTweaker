package leviathan143.loottweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.List;

import com.google.gson.Gson;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class CommonMethodHandles
{
	private static final MethodHandle lootTable$poolsGetter, lootPool$poolConditionsGetter,
			lootTableManager$GSON_INSTANCEGetter, entityLiving$getLootTable;

	static
	{
		try
		{
		    lootTable$poolsGetter = MethodHandles.lookup().unreflectGetter(
		        ObfuscationReflectionHelper.findField(LootTable.class, "field_186466_c"));

			lootPool$poolConditionsGetter = MethodHandles.lookup().unreflectGetter(
			    ObfuscationReflectionHelper.findField(LootPool.class, "field_186454_b"));

			lootTableManager$GSON_INSTANCEGetter = MethodHandles.lookup().unreflectGetter(
			    ObfuscationReflectionHelper.findField(LootTableManager.class, "field_186526_b"));

			entityLiving$getLootTable = MethodHandles.lookup().unreflect(
			    ObfuscationReflectionHelper.findMethod(EntityLiving.class, "func_184647_J", ResourceLocation.class));
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException("Failed to initialize MethodHandles!", e);
		}
	}

	public static List<LootPool> getPoolsFromTable(LootTable table)
	{
		try
		{
			return (List<LootPool>) lootTable$poolsGetter.invokeExact(table);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			throw new RuntimeException(t);
		}
	}

	public static List<LootCondition> getConditionsFromPool(LootPool pool)
	{
		try
		{
			return (List<LootCondition>) lootPool$poolConditionsGetter.invokeExact(pool);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			throw new RuntimeException(t);
		}
	}

	public static ResourceLocation getEntityLootTable(EntityLiving living)
	{
		try
		{
			return (ResourceLocation) entityLiving$getLootTable.invokeExact(living);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			throw new RuntimeException(t);
		}
	}

	public static Gson getLootTableGSON()
	{
		try
		{
			return (Gson) lootTableManager$GSON_INSTANCEGetter.invokeExact();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			throw new RuntimeException(t);
		}
	}
}
