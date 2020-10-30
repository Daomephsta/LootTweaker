package leviathan143.loottweaker.common.zenscript.api.iteration;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import crafttweaker.api.item.IItemStack;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.api.LootPoolRepresentation;
import leviathan143.loottweaker.common.zenscript.api.entry.LootConditionRepresentation;
import leviathan143.loottweaker.common.zenscript.api.entry.LootFunctionRepresentation;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootPoolIterator")
public class LootPoolIterator implements Iterator<LootPoolIterator>, LootPoolRepresentation
{
    private final Iterator<? extends LootPoolRepresentation> delegate;
    private final LootTweakerContext context;
    private LootPoolRepresentation currentPool;

    public LootPoolIterator(Iterator<? extends LootPoolRepresentation> delegate, LootTweakerContext context)
    {
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    public LootPoolIterator next()
    {
        try
        {
            this.currentPool = delegate.next();
        }
        catch (ConcurrentModificationException e)
        {
            context.getErrorHandler().error("Pools unsafely removed while iterating. To safely remove pools"
                + " from a table while iterating over it, call remove() on the pool (i.e. somePool.remove()).");
        }
        return this;
    }

    @Override
    public boolean hasNext()
    {
        return delegate.hasNext();
    }

    @Override
    @ZenMethod
    public void remove()
    {
        delegate.remove();
        CraftTweakerAPI.logInfo("Removed " + currentPool.describe());
    }

    @Override
    public void addItemEntry(IItemStack stack, String name)
    {
        currentPool.addItemEntry(stack, name);
    }

    @Override
    public void addItemEntry(IItemStack stack, int weight, String name)
    {
        currentPool.addItemEntry(stack, weight, name);
    }

    @Override
    public void addItemEntry(IItemStack stack, int weight, int quality, String name)
    {
        currentPool.addItemEntry(stack, weight, quality, name);
    }

    @Override
    public void addItemEntryJson(IItemStack stack, int weight, int quality, IData[] functions, IData[] conditions, String name)
    {
        currentPool.addItemEntryJson(stack, weight, quality, functions, conditions, name);
    }

    @Override
    public void addItemEntryHelper(IItemStack stack, int weight, int quality, LootFunctionRepresentation[] functions, LootConditionRepresentation[] conditions, String name)
    {
        currentPool.addItemEntryHelper(stack, weight, quality, functions, conditions, name);
    }

    @Override
    public void addLootTableEntry(String delegateTableId, String name)
    {
        currentPool.addLootTableEntry(delegateTableId, name);
    }

    @Override
    public void addLootTableEntry(String delegateTableId, int weight, String name)
    {
        currentPool.addLootTableEntry(delegateTableId, weight, name);
    }

    @Override
    public void addLootTableEntry(String delegateTableId, int weight, int quality, String name)
    {
        currentPool.addLootTableEntry(delegateTableId, weight, quality, name);
    }

    @Override
    public void addLootTableEntryJson(String delegateTableId, int weight, int quality, IData[] conditions, String name)
    {
        currentPool.addLootTableEntryJson(delegateTableId, weight, quality, conditions, name);
    }

    @Override
    public void addLootTableEntryHelper(String delegateTableId, int weight, int quality, LootConditionRepresentation[] conditions, String name)
    {
        currentPool.addLootTableEntryHelper(delegateTableId, weight, quality, conditions, name);
    }

    @Override
    public void addEmptyEntry(String name)
    {
        currentPool.addEmptyEntry(name);
    }

    @Override
    public void addEmptyEntry(int weight, String name)
    {
        currentPool.addEmptyEntry(weight, name);
    }

    @Override
    public void addEmptyEntry(int weight, int quality, String name)
    {
        currentPool.addEmptyEntry(weight, quality, name);
    }

    @Override
    public void addEmptyEntryJson(int weight, int quality, IData[] conditions, String name)
    {
        currentPool.addEmptyEntryJson(weight, quality, conditions, name);
    }

    @Override
    public void addEmptyEntryHelper(int weight, int quality, LootConditionRepresentation[] conditions, String name)
    {
        currentPool.addEmptyEntryHelper(weight, quality, conditions, name);
    }

    @Override
    public void removeEntry(String entryId)
    {
        currentPool.removeEntry(entryId);
    }

    @Override
    public void removeAllEntries()
    {
        currentPool.removeAllEntries();
    }

    @Override
    public void addConditionsJson(IData[] conditions)
    {
        currentPool.addConditionsJson(conditions);
    }

    @Override
    public void addConditionsHelper(LootConditionRepresentation[] conditions)
    {
        currentPool.addConditionsHelper(conditions);
    }

    @Override
    public void removeAllConditions()
    {
        currentPool.removeAllConditions();
    }

    @Override
    public void setRolls(float min, float max)
    {
        currentPool.setRolls(min, max);
    }

    @Override
    public void setBonusRolls(float min, float max)
    {
        currentPool.setBonusRolls(min, max);
    }

    @Override
    public String getName()
    {
        return currentPool.getName();
    }

    @Override
    public String describe()
    {
        return currentPool.describe();
    }

    @Override
    public Iterator<LootEntryIterator> iterator()
    {
        return currentPool.iterator();
    }

    @Override
    public void forEach(Consumer<? super LootEntryIterator> action)
    {
        currentPool.forEach(action);
    }

    @Override
    public Spliterator<LootEntryIterator> spliterator()
    {
        return currentPool.spliterator();
    }
}