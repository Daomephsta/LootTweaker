package leviathan143.loottweaker.common.zenscript;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import stanhebben.zenscript.annotations.ZenCaster;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenExpansion;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".JsonValue")
public class JsonValue
{
    private final Object wrapped;

    public JsonValue(Object wrapped)
    {
        this.wrapped = wrapped;
    }

    public JsonElement serialize(Type typeOfSrc, JsonSerializationContext context)
    {
        return context.serialize(wrapped);
    }

    @Override
    public String toString()
    {
        return wrapped.toString();
    }

    @ZenRegister
    @ZenExpansion("any[any]")
    public static class FromMap
    {
        @ZenCaster
        public static JsonValue fromMap(Map<String, Object> data)
        {
            return new JsonValue(data);
        }
    }

    @ZenRegister
    @ZenExpansion("any[]")
    public static class FromObjectArray
    {
        @ZenCaster
        public static JsonValue fromObjectArray(Object[] data)
        {
            return new JsonValue(data);
        }
    }

    @ZenRegister
    @ZenExpansion("byte[]")
    public static class FromByteArray
    {
        @ZenCaster
        public static JsonValue fromByteArray(byte[] data)
        {
            return new JsonValue(data);
        }
    }

    @ZenRegister
    @ZenExpansion("int[]")
    public static class FromIntArray
    {
        @ZenCaster
        public static JsonValue fromIntArray(int[] data)
        {
            return new JsonValue(data);
        }
    }

    @ZenRegister
    @ZenExpansion("string")
    public static class FromString
    {
        @ZenCaster
        public static JsonValue fromString(String data)
        {
            return new JsonValue(data);
        }
    }@ZenRegister
    @ZenExpansion("double")
    public static class FromDouble
    {
        @ZenCaster
        public static JsonValue fromDouble(double data)
        {
            return new JsonValue(data);
        }
    }

    @ZenRegister
    @ZenExpansion("float")
    public static class FromFloat
    {
        @ZenCaster
        public static JsonValue fromFloat(float data)
        {
            return new JsonValue(data);
        }
    }

    @ZenRegister
    @ZenExpansion("long")
    public static class FromLong
    {
        @ZenCaster
        public static JsonValue fromLong(long data)
        {
            return new JsonValue(data);
        }
    }

    @ZenRegister
    @ZenExpansion("int")
    public static class FromInt
    {
        @ZenCaster
        public static JsonValue fromInt(int data)
        {
            return new JsonValue(data);
        }
    }

    @ZenRegister
    @ZenExpansion("short")
    public static class FromShort
    {
        @ZenCaster
        public static JsonValue fromShort(short data)
        {
            return new JsonValue(data);
        }
    }

    @ZenRegister
    @ZenExpansion("byte")
    public static class FromByte
    {
        @ZenCaster
        public static JsonValue fromByte(byte data)
        {
            return new JsonValue(data);
        }
    }

    @ZenRegister
    @ZenExpansion("bool")
    public static class FromBool
    {
        @ZenCaster
        public static JsonValue fromBool(boolean data)
        {
            return new JsonValue(data);
        }
    }
}