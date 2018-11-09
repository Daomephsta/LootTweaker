package leviathan143.loottweaker.common.zenscript.adders;

import leviathan143.loottweaker.common.lib.LootUtils;
import leviathan143.loottweaker.common.zenscript.ZenLootPoolWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryTable;

public class LootTableEntryAdder extends AbstractEntryAdder<LootEntryTable>
{
	private final ResourceLocation table;
	
	public LootTableEntryAdder(ZenLootPoolWrapper wrapper, ResourceLocation table)
	{
		super(wrapper);
		this.table = table;
	}

	@Override
	protected String getDefaultName()
	{
		return table.toString();
	}

	@Override
	protected LootEntryTable createLootEntry()
	{
		return new LootEntryTable(table, weight, quality, conditions.toArray(LootUtils.NO_CONDITIONS), name);
	}
}
