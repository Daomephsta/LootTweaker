package leviathan143.loottweaker.common.mutable_loot.entry;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import leviathan143.loottweaker.common.darkmagic.LootEntryItemAccessors;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class MutableLootEntryItem extends MutableLootEntry<MutableLootEntryItem, LootEntryItem>
{
    private Item item;
    private List<LootFunction> functions;

    MutableLootEntryItem(LootEntryItem entry)
    {
        super(entry);
        this.item = LootEntryItemAccessors.getItem(entry);
        this.functions = Lists.newArrayList(LootEntryItemAccessors.getFunctions(entry));
    }

    public MutableLootEntryItem(String name, int weight, int quality, List<LootCondition> conditions, Item item, List<LootFunction> functions)
    {
        super(name, weight, quality, conditions);
        this.item = item;
        this.functions = functions;
    }

    @Override
    public MutableLootEntryItem deepClone()
    {
        return new MutableLootEntryItem(getName(), getWeight(), getQuality(), deepCloneConditions(), item, deepCloneFunctions());
    }

    private List<LootFunction> deepCloneFunctions()
    {
        List<LootFunction> clone = new ArrayList<>(functions.size());
        for (int i = 0; i < functions.size(); i++)
            clone.set(i, deepCloneFunction(functions.get(i)));
        return clone;
    }

    private LootFunction deepCloneFunction(LootFunction lootFunction)
    {
        Gson lootTableGson = LootTableManagerAccessors.getGsonInstance();
        JsonElement json = lootTableGson.toJsonTree(lootFunction);
        return lootTableGson.fromJson(json, LootFunction.class);
    }

    @Override
    public LootEntryItem toImmutable()
    {
        return new LootEntryItem(item, getWeight(), getQuality(), functions.toArray(new LootFunction[0]),
            getConditions().toArray(new LootCondition[0]), getName());
    }

    public Item getItem()
    {
        return item;
    }

    public void setItem(Item item)
    {
        this.item = item;
    }

    public List<LootFunction> getFunctions()
    {
        return functions;
    }

    public void setFunctions(List<LootFunction> functions)
    {
        this.functions = functions;
    }

    public void addFunction(LootFunction function)
    {
        functions.add(function);
    }

    public void clearFunctions()
    {
        functions.clear();
    }
}
