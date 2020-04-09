package leviathan143.loottweaker.common.lib;

import java.util.List;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

public interface DeepClone<Self>
{
    public Self deepClone();

    public static <E> List<E> list(List<E> list, IntFunction<List<E>> listCreator, UnaryOperator<E> elementDeepCloner)
    {
        List<E> clone = listCreator.apply(list.size());
        for (int i = 0; i < list.size(); i++)
            clone.set(i, elementDeepCloner.apply(list.get(i)));
        return clone;
    }
}
