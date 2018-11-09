package leviathan143.loottweaker.common.zenscript.actions;

import leviathan143.loottweaker.common.zenscript.ZenLootPoolWrapper;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class AddLootEntry extends UndoableDelayedPoolTweak
{
	private LootEntry entry;

	public AddLootEntry(ZenLootPoolWrapper wrapper, LootEntry entry)
	{
		super(wrapper);
		this.entry = entry;
	}

	@Override
	public void applyTweak(LootPool pool, ZenLootPoolWrapper zenWrapper)
	{
		if (pool.getEntry(entry.getEntryName()) != null)
		{
			int counter = 1;
			String baseName = entry.getEntryName();
			String name = baseName;
			while (pool.getEntry(name) != null)
			{
				name = baseName + "-lt#" + ++counter;
			}
			ObfuscationReflectionHelper.setPrivateValue(LootEntry.class, entry, name, "entryName");
		}
		pool.addEntry(entry);
	}

	@Override
	public String describe()
	{
		return String.format("Adding entry %s to pool %s", entry.getEntryName(), wrapper.getPool().getName());
	}
}