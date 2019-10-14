package leviathan143.loottweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public abstract class AbstractAccessors 
{
	protected static MethodHandle createFieldGetter(Class<?> clazz, String fieldSrgName) throws IllegalAccessException 
	{
		return MethodHandles.lookup().unreflectGetter(ObfuscationReflectionHelper.findField(clazz, fieldSrgName));
	}
	
	protected static MethodHandle createFieldSetter(Class<?> clazz, String fieldSrgName) throws IllegalAccessException 
	{
		return MethodHandles.lookup().unreflectSetter(ObfuscationReflectionHelper.findField(clazz, fieldSrgName));
	}
	
	protected static MethodHandle createMethodInvoker(Class<?> clazz, String srgName, Class<?> returnType, Class<?>... parameterTypes) throws IllegalAccessException 
	{
		return MethodHandles.lookup().unreflect(ObfuscationReflectionHelper.findMethod(clazz, srgName, returnType, parameterTypes));
	}
}
