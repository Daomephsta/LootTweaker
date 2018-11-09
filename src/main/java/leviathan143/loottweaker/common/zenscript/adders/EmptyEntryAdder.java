package leviathan143.loottweaker.common.zenscript.adders;

import leviathan143.loottweaker.common.lib.LootUtils;
import leviathan143.loottweaker.common.zenscript.ZenLootPoolWrapper;
import net.minecraft.world.storage.loot.LootEntryEmpty;

public class EmptyEntryAdder extends AbstractEntryAdder<LootEntryEmpty>
{
	public EmptyEntryAdder(ZenLootPoolWrapper wrapper)
	{
		super(wrapper);
	}

	@Override
	protected String getDefaultName()
	{
		return "empty";
	}

	@Override
	protected LootEntryEmpty createLootEntry()
	{
		return new LootEntryEmpty(weight, quality, conditions.toArray(LootUtils.NO_CONDITIONS), name);
	}
}
