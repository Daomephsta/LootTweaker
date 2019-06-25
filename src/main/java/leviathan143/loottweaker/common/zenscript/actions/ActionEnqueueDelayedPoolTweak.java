package leviathan143.loottweaker.common.zenscript.actions;

import crafttweaker.IAction;
import leviathan143.loottweaker.common.lib.IDelayedTweak;
import leviathan143.loottweaker.common.zenscript.ZenLootPoolWrapper;
import net.minecraft.world.storage.loot.LootPool;

public abstract class ActionEnqueueDelayedPoolTweak implements IAction, IDelayedTweak<LootPool, ZenLootPoolWrapper>
{
	protected ZenLootPoolWrapper wrapper;

	public ActionEnqueueDelayedPoolTweak(ZenLootPoolWrapper wrapper)
	{
		this.wrapper = wrapper;
	}

	@Override
	public void apply()
	{
		wrapper.addDelayedTweak(this);
	}
}