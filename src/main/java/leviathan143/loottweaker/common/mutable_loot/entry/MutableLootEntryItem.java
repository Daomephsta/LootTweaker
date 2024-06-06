package leviathan143.loottweaker.common.mutable_loot.entry;

import java.util.List;

import com.google.common.collect.Lists;

import daomephsta.loot_shared.mixin.LootEntryItemAccessors;
import daomephsta.loot_shared.utility.loot.LootConditions;
import daomephsta.loot_shared.utility.loot.LootFunctions;
import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;


public class MutableLootEntryItem extends AbstractMutableLootEntry
{
    private Item item;
    private List<LootFunction> functions;

    MutableLootEntryItem(LootEntryItem entry)
    {
        super(entry);
        this.item = ((LootEntryItemAccessors) entry).getItem();
        this.functions = Lists.newArrayList(LootFunctions.get((LootEntryItem) ((LootEntryItemAccessors) entry)));
    }

    public MutableLootEntryItem(String name, int weight, int quality, List<LootCondition> conditions, Item item,
        List<LootFunction> functions)
    {
        super(name, weight, quality, conditions);
        this.item = item;
        this.functions = functions;
    }

    @Override
    public MutableLootEntryItem deepClone()
    {
        return new MutableLootEntryItem(getName(), getWeight(), getQuality(),
            LootConditions.deepClone(getConditions()), item, LootFunctions.deepClone(functions));
    }

    @Override
    public LootEntryItem toImmutable()
    {
        return new LootEntryItem(item, getWeight(), getQuality(), functions.toArray(LootFunctions.NONE),
            getConditions().toArray(LootConditions.NONE), getName());
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
