package leviathan143.loottweaker.common.loot.block;

import java.io.File;

import leviathan143.loottweaker.common.darkmagic.CommonMethodHandles;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.fml.common.Loader;

public class BlockLootTableManager extends LootTableManager 
{
	public BlockLootTableManager(File folder) 
	{
		super(folder);
		System.out.println(Loader.instance().getLoaderState() + ": BlockLootTableManager instantiated");
	}

	@Override
	public void reloadLootTables() 
	{
		System.out.println(Loader.instance().getLoaderState() + ": Reloading block loot tables");
		CommonMethodHandles.getRegisteredLootTables(this).invalidateAll();

		for (ResourceLocation resourcelocation : BlockLootHandler.getBlockLootTables())
		{
			this.getLootTableFromLocation(resourcelocation);
		}
	}

	public static File getBlockLootTableFilePath(ResourceLocation blockLootTable)
	{
		return new File(BlockLootHandler.BLOCK_LOOT_PATH, blockLootTable.getResourceDomain() + File.separator + blockLootTable.getResourcePath() + ".json");
	}
}
