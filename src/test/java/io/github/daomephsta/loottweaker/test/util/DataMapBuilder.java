package io.github.daomephsta.loottweaker.test.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crafttweaker.api.data.*;

public class DataMapBuilder
{
    private final Map<String, IData> data = new HashMap<>();

    public DataMapBuilder putBool(String key, boolean value)
    {
        data.put(key, new DataBool(value));
        return this;
    }

    public DataMapBuilder putByte(String key, byte value)
    {
        data.put(key, new DataByte(value));
        return this;
    }

    public DataMapBuilder putByteArray(String key, byte... value)
    {
        data.put(key, new DataByteArray(value, true));
        return this;
    }

    public DataMapBuilder putDouble(String key, double value)
    {
        data.put(key, new DataDouble(value));
        return this;
    }

    public DataMapBuilder putFloat(String key, float value)
    {
        data.put(key, new DataFloat(value));
        return this;
    }

    public DataMapBuilder putInt(String key, int value)
    {
        data.put(key, new DataInt(value));
        return this;
    }

    public DataMapBuilder putIntArray(String key, int... value)
    {
        data.put(key, new DataIntArray(value, true));
        return this;
    }

    public DataMapBuilder putList(String key, List<IData> value)
    {
        data.put(key, new DataList(value, true));
        return this;
    }

    public DataMapBuilder putMap(String key, DataMapBuilder valueBuilder)
    {
        data.put(key, valueBuilder.build());
        return this;
    }

    public DataMapBuilder putLong(String key, long value)
    {
        data.put(key, new DataLong(value));
        return this;
    }

    public DataMapBuilder putShort(String key, short value)
    {
        data.put(key, new DataShort(value));
        return this;
    }

    public DataMapBuilder putString(String key, String value)
    {
        data.put(key, new DataString(value));
        return this;
    }

    public DataMap build()
    {
        return new DataMap(data, true);
    }
}
