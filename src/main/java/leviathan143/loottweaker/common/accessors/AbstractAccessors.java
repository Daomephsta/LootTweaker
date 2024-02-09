package leviathan143.loottweaker.common.accessors;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;


public abstract class AbstractAccessors
{
    public static class FieldReflector
    {
        private final Class<?> owner;
        private final String name;
        private boolean remap = true;

        private FieldReflector(Class<?> owner, String name)
        {
            this.owner = owner;
            this.name = name;
        }

        public FieldReflector remap(boolean remap)
        {
            this.remap = remap;
            return this;
        }

        private Field find() throws NoSuchFieldException
        {
            Field f;
            if (remap)
                f = ObfuscationReflectionHelper.findField(owner, name);
            else
                f = owner.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        }

        public MethodHandle getter() throws IllegalAccessException, NoSuchFieldException, SecurityException
        {
            return MethodHandles.lookup().unreflectGetter(find());
        }

        public MethodHandle setter() throws IllegalAccessException, NoSuchFieldException, SecurityException
        {
            return MethodHandles.lookup().unreflectSetter(find());
        }
    }

    protected static FieldReflector field(Class<?> owner, String name)
    {
        return new FieldReflector(owner, name);
    }

    public static class MethodReflector
    {
        private final Class<?> owner;
        private final String name;
        private final Class<?>[] parameterTypes;
        private Class<?> returnType = Void.TYPE;
        private boolean remap = true;

        private MethodReflector(Class<?> owner, String name, Class<?>... parameterTypes)
        {
            this.owner = owner;
            this.name = name;
            this.parameterTypes = parameterTypes;
        }

        public MethodReflector remap(boolean remap)
        {
            this.remap = remap;
            return this;
        }

        public MethodReflector returnType(Class<?> returnType)
        {
            this.returnType = returnType;
            return this;
        }

        private Method find() throws NoSuchMethodException
        {
            Method m;
            if (remap)
                m = ObfuscationReflectionHelper.findMethod(owner, name, returnType, parameterTypes);
            else
                m = owner.getDeclaredMethod(name, parameterTypes);
            m.setAccessible(true);
            return m;
        }

        public MethodHandle invoker() throws IllegalAccessException, NoSuchMethodException, SecurityException
        {
            return MethodHandles.lookup().unreflect(find());
        }
    }

    protected static MethodReflector method(Class<?> owner, String name, Class<?>... parameterTypes)
    {
        return new MethodReflector(owner, name, parameterTypes);
    }
}
