package leviathan143.loottweaker.common.lib;

import java.util.HashSet;
import java.util.Set;

import leviathan143.loottweaker.common.mixin.LootPoolAccessors;
import leviathan143.loottweaker.common.mixin.LootTableAccessors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;

public class LootFixer
{
    public static LootTable fixTable(LootTable table, ResourceLocation tableId)
    {
    	LootNameFixer nameFixer = new LootNameFixer(tableId);
	    Set<String> usedNames = new HashSet<>();
	    for (LootPool pool : ((LootTableAccessors) table).getPools())
	    {
	        if (!usedNames.add(pool.getName()))
	            nameFixer.deduplicatePoolName(pool);
	        fixPool(pool, nameFixer);
	    }
	    return table;
    }

	private static void fixPool(LootPool pool, LootNameFixer nameFixer)
	{
        Set<String> usedNames = new HashSet<>();
        if (pool.getName().startsWith("custom#"))
        	nameFixer.fixCustomPoolName((LootPoolAccessors) pool);
        for (LootEntry entry : ((LootPoolAccessors) pool).getEntries())
        {
            if (!usedNames.add(entry.getEntryName()))
                nameFixer.deduplicateEntryName(pool.getName(), entry);
            else if (entry.getEntryName().startsWith("custom#"))
                nameFixer.fixCustomEntryName(entry);
        }
	}
}