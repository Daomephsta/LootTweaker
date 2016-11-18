package leviathan143.droptweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.base.Throwables;
import com.google.gson.Gson;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class CommonMethodHandles 
{
	public static MethodHandle lootTable$poolsGetter, lootPool$poolConditionsGetter, lootPool$lootEntriesGetter, lootTableManager$GSON_INSTANCE, entityLiving$getLootTable;
	
	static
	{
		try 
		{
			//TODO add obfuscated names
			Field f;
			Method m;
			f = ReflectionHelper.findField(LootTable.class, "pools");
			lootTable$poolsGetter = MethodHandles.lookup().unreflectGetter(f);
			
			f = ReflectionHelper.findField(LootPool.class, "poolConditions");
			lootPool$poolConditionsGetter = MethodHandles.lookup().unreflectGetter(f);
			
			f = ReflectionHelper.findField(LootPool.class, "lootEntries");
			lootPool$lootEntriesGetter = MethodHandles.lookup().unreflectGetter(f);
			
			f = ReflectionHelper.findField(LootTableManager.class, "GSON_INSTANCE");
			lootTableManager$GSON_INSTANCE = MethodHandles.lookup().unreflectGetter(f);
			
			m = ReflectionHelper.findMethod(EntityLiving.class, null, new String[] {"getLootTable"});
			entityLiving$getLootTable = MethodHandles.lookup().unreflect(m);
		} 
		catch (IllegalAccessException e) 
		{
			FMLLog.log(Level.ERROR, "Failed to initialize MethodHandles!");
			e.printStackTrace();
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
			throw Throwables.propagate(t);
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
			throw Throwables.propagate(t);
		}
	}
	
	public static List<LootEntry> getEntriesFromPool(LootPool pool)
	{
		try 
		{
			return (List<LootEntry>) lootPool$lootEntriesGetter.invokeExact(pool);
		}
		catch (Throwable t) 
		{
			t.printStackTrace();
			throw Throwables.propagate(t);
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
			throw Throwables.propagate(t);
		}
	}
	
	public static Gson getLootConditionGSON()
	{
		try 
		{
			return (Gson) lootTableManager$GSON_INSTANCE.invokeExact();
		}
		catch (Throwable t) 
		{
			t.printStackTrace();
			throw Throwables.propagate(t);
		}
	}
}
