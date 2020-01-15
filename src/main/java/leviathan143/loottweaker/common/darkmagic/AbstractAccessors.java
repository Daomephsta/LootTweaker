package leviathan143.loottweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class AbstractAccessors
{
    protected static MethodHandle createFieldGetter(Class<?> clazz, String fieldMappedName) throws IllegalAccessException, NoSuchFieldException, SecurityException
    {
        Field f = clazz.getDeclaredField(fieldMappedName);
        f.setAccessible(true);
        return MethodHandles.lookup().unreflectGetter(f);
    }

	protected static MethodHandle createFieldGetter(Class<?> clazz, String fieldSrgName, String fieldMappedName) throws IllegalAccessException, NoSuchFieldException, SecurityException
	{
		return MethodHandles.lookup().unreflectGetter(findField(clazz, fieldSrgName, fieldMappedName));
	}

    protected static MethodHandle createFieldSetter(Class<?> clazz, String fieldMappedName) throws IllegalAccessException, NoSuchFieldException, SecurityException
    {
        Field f = clazz.getDeclaredField(fieldMappedName);
        f.setAccessible(true);
        return MethodHandles.lookup().unreflectSetter(f);
    }

    protected static MethodHandle createFieldSetter(Class<?> clazz, String fieldSrgName, String fieldMappedName) throws IllegalAccessException, NoSuchFieldException, SecurityException
	{
		return MethodHandles.lookup().unreflectSetter(findField(clazz, fieldSrgName, fieldMappedName));
	}

    private static Field findField(Class<?> clazz, String fieldSrgName, String fieldMappedName) throws NoSuchFieldException, SecurityException
    {
        Field f;
        try
        {
            f = clazz.getDeclaredField(fieldSrgName);
        }
        catch (NoSuchFieldException e)
        {
            f = clazz.getDeclaredField(fieldMappedName);
        }
        f.setAccessible(true);
        return f;
    }

    protected static MethodHandle createMethodInvoker(Class<?> clazz, String mappedName, Class<?>... parameterTypes) throws IllegalAccessException, NoSuchMethodException, SecurityException
    {
        Method m = clazz.getDeclaredMethod(mappedName, parameterTypes);
        m.setAccessible(true);
        return MethodHandles.lookup().unreflect(m);
    }

	protected static MethodHandle createMethodInvoker(Class<?> clazz, String srgName, String mappedName, Class<?>... parameterTypes) throws IllegalAccessException, NoSuchMethodException, SecurityException
	{
		return MethodHandles.lookup().unreflect(findMethod(clazz, srgName, mappedName, parameterTypes));
	}

	private static Method findMethod(Class<?> clazz, String srgName, String mappedName, Class<?>[] parameterTypes) throws NoSuchMethodException, SecurityException
	{
	    Method m;
	    try
	    {
	        m = clazz.getDeclaredMethod(srgName, parameterTypes);
	    }
	    catch (NoSuchMethodException e)
	    {
	        m = clazz.getDeclaredMethod(mappedName, parameterTypes);
	    }
	    m.setAccessible(true);
	    return m;
	}
}
