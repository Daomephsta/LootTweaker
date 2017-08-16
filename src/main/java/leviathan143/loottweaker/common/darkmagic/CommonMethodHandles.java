package leviathan143.loottweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;

import leviathan143.loottweaker.common.LootTweakerMain;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class CommonMethodHandles 
{
	public static MethodHandle lootTable$poolsGetter, lootPool$poolConditionsGetter, lootPool$lootEntriesGetter
	, lootTableManager$GSON_INSTANCEGetter, lootTableManager$registeredLootTablesGetter, entityLiving$getLootTable;
	
	static
	{
		try 
		{
			Field f;
			Method m;
			f = ReflectionHelper.findField(LootTable.class, "c", "field_186466_c", "pools");
			lootTable$poolsGetter = MethodHandles.lookup().unreflectGetter(f);
			
			f = ReflectionHelper.findField(LootPool.class, "b", "field_186454_b", "poolConditions");
			lootPool$poolConditionsGetter = MethodHandles.lookup().unreflectGetter(f);
			
			f = ReflectionHelper.findField(LootPool.class, "a", "field_186453_a", "lootEntries");
			lootPool$lootEntriesGetter = MethodHandles.lookup().unreflectGetter(f);
			
			f = ReflectionHelper.findField(LootTableManager.class, "b", "field_186526_b", "GSON_INSTANCE");
			lootTableManager$GSON_INSTANCEGetter = MethodHandles.lookup().unreflectGetter(f);
			
			f = ReflectionHelper.findField(LootTableManager.class, "c", "field_186527_c" , "registeredLootTables");
			lootTableManager$registeredLootTablesGetter = MethodHandles.lookup().unreflectGetter(f);
			
			m = ReflectionHelper.findMethod(EntityLiving.class, "getLootTable", /*"J"*/"func_184647_J");
			entityLiving$getLootTable = MethodHandles.lookup().unreflect(m);
		} 
		catch (IllegalAccessException e) 
		{
			LootTweakerMain.logger.log(Level.ERROR, "Failed to initialize MethodHandles!");
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
	
	public static List<LootEntry> getEntriesFromPool(LootPool pool)
	{
		try 
		{
			return (List<LootEntry>) lootPool$lootEntriesGetter.invokeExact(pool);
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
	
	public static LoadingCache<ResourceLocation, LootTable> getRegisteredLootTables(LootTableManager manager)
	{
		try 
		{
			return (LoadingCache<ResourceLocation, LootTable>) lootTableManager$registeredLootTablesGetter.invoke(manager);
		}
		catch (Throwable t) 
		{
			t.printStackTrace();
			throw new RuntimeException(t);
		}
	}
}
