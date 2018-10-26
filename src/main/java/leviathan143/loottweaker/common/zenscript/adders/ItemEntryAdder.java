package leviathan143.loottweaker.common.zenscript.adders;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.JsonElement;

import crafttweaker.api.data.IData;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import leviathan143.loottweaker.common.lib.DataToJSONConverter;
import leviathan143.loottweaker.common.lib.LootUtils;
import leviathan143.loottweaker.common.zenscript.ZenLootFunctionWrapper;
import leviathan143.loottweaker.common.zenscript.ZenLootPoolWrapper;
import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.functions.LootFunction;
import stanhebben.zenscript.annotations.ZenMethod;

public class ItemEntryAdder extends AbstractEntryAdder<LootEntryItem>
{
	private final IItemStack stack;
	private LootFunction[] functions = {};
	
	public ItemEntryAdder(ZenLootPoolWrapper wrapper, IItemStack stack)
	{
		super(wrapper);
		this.stack = stack;
	}
	
	@ZenMethod
	public ItemEntryAdder functionsJson(IData[] functions)
	{
		JsonElement[] json = Arrays.stream(functions).map(DataToJSONConverter::from).toArray(JsonElement[]::new);
		ArrayUtils.addAll(this.functions, LootUtils.parseFunctions(json));
		LootUtils.addStackFunctions(stack, this.functions);
		return this;
	}
	
	@ZenMethod
	public ItemEntryAdder functionsHelper(ZenLootFunctionWrapper[] functions)
	{
		this.functions = ArrayUtils.addAll(this.functions, LootUtils.parseFunctions(functions));
		LootUtils.addStackFunctions(stack, this.functions);
		return this;
	}
	
	@Override
	protected String getDefaultName()
	{
		return CraftTweakerMC.getItemStack(stack).getItem().getRegistryName().toString();
	}

	@Override
	protected LootEntryItem createLootEntry()
	{
		Item item = CraftTweakerMC.getItemStack(stack).getItem();
		return new LootEntryItem(item, weight, quality, functions, conditions.toArray(LootUtils.NO_CONDITIONS), name);
	}
}
