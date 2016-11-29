package leviathan143.loottweaker.common.loot;

import java.util.Collection;
import java.util.Random;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import leviathan143.loottweaker.common.LootUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;

/**Used instead of {@code LootEntry} for loot entries that need to be removed**/
public class LootEntryPendingRemoval extends LootEntry 
{
	public LootEntryPendingRemoval(String entryName) 
	{
		super(0, 0, LootUtils.NO_CONDITIONS, entryName);
	}

	@Override
	public void addLoot(Collection<ItemStack> stacks, Random rand, LootContext context) {}

	@Override
	protected void serialize(JsonObject json, JsonSerializationContext context) {}

}
