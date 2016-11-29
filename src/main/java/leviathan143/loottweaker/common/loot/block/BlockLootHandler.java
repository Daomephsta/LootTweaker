package leviathan143.loottweaker.common.loot.block;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;

public class BlockLootHandler implements IResourceManagerReloadListener
{
	private static BlockLootTableManager blockLootTableManager;
	public static final File BLOCK_LOOT_PATH;
	private static final Set<ResourceLocation> BLOCK_LOOT_TABLES = new HashSet<ResourceLocation>();
	
	static
	{
		BLOCK_LOOT_PATH = new File("config" + File.separator + Constants.MODID + File.separator + "blockLoot" + File.separator + "assets");
		BLOCK_LOOT_PATH.mkdirs();
	}
	
	public static BlockLootTableManager getBlockLootTableManager() 
	{
		return blockLootTableManager;
	}
	
	public static void initBlockLootTableManager() 
	{
		blockLootTableManager = new BlockLootTableManager(BLOCK_LOOT_PATH);
	}
	
	public static void registerTable(ResourceLocation tableLoc)
	{
		if(!BLOCK_LOOT_TABLES.add(tableLoc))
			LootTweakerMain.logger.warn(String.format("Loot table %s is already registered", tableLoc));
	}
	
	public static Set<ResourceLocation> getBlockLootTables() 
	{
		return BLOCK_LOOT_TABLES;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) 
	{
		//Don't bother reloading unless the mod is available
		if(Loader.instance().hasReachedState(LoaderState.AVAILABLE))
			blockLootTableManager.reloadLootTables();
	}
}
