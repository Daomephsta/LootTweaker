package leviathan143.loottweaker.common.zenscript.api.iteration;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import com.google.common.base.Supplier;

import leviathan143.loottweaker.common.ErrorHandler;
import stanhebben.zenscript.annotations.ZenMethod;

public class LootIterator<L extends M, M> implements Iterable<M>, Iterator<M>
{
    private final Iterator<L> delegate;
    private final ErrorHandler errorHandler;
    private final Supplier<String> message;
    private L current;

    public LootIterator(Iterator<L> delegate, ErrorHandler errorHandler, Supplier<String> message)
    {
        this.delegate = delegate;
        this.errorHandler = errorHandler;
        this.message = message;
    }

    @Override
    public M next()
    {
        try
        {
            this.current = delegate.next();
        }
        catch (ConcurrentModificationException e)
        {
            errorHandler.error(message.get());
        }
        return current;
    }

    @Override
    public boolean hasNext()
    {
        return delegate.hasNext();
    }

    @ZenMethod("removeCurrent")
    @Override
    public void remove()
    {
        delegate.remove();
    }

    @Override
    public Iterator<M> iterator()
    {
        return this;
    }
}