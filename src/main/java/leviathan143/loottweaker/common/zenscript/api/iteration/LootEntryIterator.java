package leviathan143.loottweaker.common.zenscript.api.iteration;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.api.entry.LootEntryRepresentation;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootEntryIterator")
public class LootEntryIterator implements Iterator<LootEntryIterator>, LootEntryRepresentation
{
    private final Iterator<? extends LootEntryRepresentation> delegate;
    private final LootTweakerContext context;
    private LootEntryRepresentation currentEntry;

    public LootEntryIterator(Iterator<? extends LootEntryRepresentation> delegate, LootTweakerContext context)
    {
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    public LootEntryIterator next()
    {
        try
        {
            this.currentEntry = delegate.next();
        }
        catch (ConcurrentModificationException e)
        {
            context.getErrorHandler().error("Entries unsafely removed while iterating. To safely remove entries"
                + " from a pool while iterating over it, call remove() on the entry (i.e. someEntry.remove()).");
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
        CraftTweakerAPI.logInfo("Removed " + currentEntry.describe());
    }

    @Override
    public String getName()
    {
        return currentEntry.getName();
    }

    @Override
    public String describe()
    {
        return currentEntry.describe();
    }
}