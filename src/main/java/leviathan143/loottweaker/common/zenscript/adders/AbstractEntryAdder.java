package leviathan143.loottweaker.common.zenscript.adders;

import java.util.*;

import com.google.gson.JsonElement;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.lib.DataToJSONConverter;
import leviathan143.loottweaker.common.lib.LootUtils;
import leviathan143.loottweaker.common.zenscript.ZenLootConditionWrapper;
import leviathan143.loottweaker.common.zenscript.ZenLootPoolWrapper;
import leviathan143.loottweaker.common.zenscript.actions.AddLootEntry;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import stanhebben.zenscript.annotations.ZenMethod;

public abstract class AbstractEntryAdder<T extends LootEntry>
{
	private ZenLootPoolWrapper wrapper;
	protected int weight;
	protected int quality;
	protected Collection<LootCondition> conditions = new ArrayList<>();
	protected String name;
	
	public AbstractEntryAdder(ZenLootPoolWrapper wrapper)
	{
		this.wrapper = wrapper;
		this.name = getDefaultName();
	}
	
	protected abstract String getDefaultName();

	@ZenMethod
	public AbstractEntryAdder<T> weight(int weight) 
	{
		this.weight = weight;
		return this;
	}
	
	@ZenMethod
	public AbstractEntryAdder<T> quality(int quality)
	{
		this.quality = quality;
		return this;
	}
	
	@ZenMethod
	public AbstractEntryAdder<T> conditionsJson(IData[] conditions)
	{
		JsonElement[] json = Arrays.stream(conditions).map(DataToJSONConverter::from).toArray(JsonElement[]::new);
		Collections.addAll(this.conditions, LootUtils.parseConditions(json));
		return this;
	}
	
	@ZenMethod
	public AbstractEntryAdder<T> conditionsHelper(ZenLootConditionWrapper[] conditions)
	{
		Collections.addAll(this.conditions, LootUtils.parseConditions(conditions));
		return this;
	}
	
	@ZenMethod
	public AbstractEntryAdder<T> name(String name)
	{
		this.name = name;
		return this;
	}
	
	@ZenMethod
	public void add()
	{
		CraftTweakerAPI.apply(new AddLootEntry(wrapper, createLootEntry()));
	}

	protected abstract T createLootEntry();
}
