package leviathan143.loottweaker.common.zenscript;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.world.IWorld;
import daomephsta.loot_shared.zenscript.api.LootGenerator;
import daomephsta.loot_shared.zenscript.impl.MutableLootTable;
import leviathan143.loottweaker.common.LTConfig;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.util.ResourceLocation;


public class LootTableTweakManager extends daomephsta.loot_shared.LootTableTweakManager
{
    private final Map<ResourceLocation, ZenLootTableWrapper> tweakedTables = new HashMap<>();
    private final Map<ResourceLocation, ZenLootTableWrapper> tableBuilders = new HashMap<>();
    private final LootTweakerContext context;

    LootTableTweakManager(LootTweakerContext context)
    {
    	super(context.getErrorHandler());
        this.context = context;
    }

    public ZenLootTableWrapper getTable(String tableName)
    {
        return getTableInternal(tableName);
    }

    public ZenLootTableWrapper getTableUnchecked(String tableName)
    {
        return getTable(tableName);
    }

    private ZenLootTableWrapper getTableInternal(String tableName)
    {
        ResourceLocation tableId = new ResourceLocation(tableName);
        if (tableBuilders.containsKey(tableId)) return tableBuilders.get(tableId);
        ZenLootTableWrapper wrapper = tweakedTables.get(tableId);
        if (wrapper == null)
        {
            wrapper = context.wrapLootTable(tableId);
            if (!wrapper.isValid())
                context.getErrorHandler().error("No loot table with name %s exists!", tableName);
            else
                tweakedTables.put(tableId, wrapper);
        }
        return wrapper;
    }

    public ZenLootTableWrapper newTable(String id)
    {
        ResourceLocation tableId = new ResourceLocation(id);
        if (!validateNewTableName(id, LootTweaker.MODID, LTConfig.warnings.newTableMinecraftNamespace))
        {
            // Return something non-null. This won't do anything because it'll never be applied.
            return context.wrapLootTable(tableId);
        }
        ZenLootTableWrapper builder = context.wrapLootTable(tableId);
        tableBuilders.put(tableId, builder);
        CraftTweakerAPI.logInfo("Created new table '" + id + "'");
        return builder;
    }

	public LootGenerator createLootGenerator(IWorld world) 
	{
		return LootGenerator.create(world, context.getErrorHandler());
	}

	@Override
	public Iterator<MutableLootTable> yieldNewTables() 
	{
		return tableBuilders.values().stream()
				.map(builder -> 
				{
					MutableLootTable mutable = new MutableLootTable(builder.getId(), new HashMap<>(), context.getErrorHandler());
		            builder.applyTweakers(mutable);
		            return mutable;
				})
				.iterator();
	}

	@Override
	public void applyEdits(MutableLootTable table) 
	{
		tweakedTables.get(table.getId()).applyTweakers(table);
	}
	
	@Override
	public Collection<ResourceLocation> getEditedTableIds() 
	{
		return tweakedTables.keySet();
	}
	
	@Override
	public Collection<ResourceLocation> getNewTableIds() 
	{
		return tableBuilders.keySet();
	}
}
