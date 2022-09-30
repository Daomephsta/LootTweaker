package io.github.daomephsta.loottweaker.test.util;

import java.util.ArrayList;
import java.util.List;

import crafttweaker.api.data.*;


public class DataListBuilder
{
    private final List<IData> data = new ArrayList<IData>();

    public DataListBuilder putBool(String key, boolean value)
    {
        data.add(new DataBool(value));
        return this;
    }

    public DataListBuilder putByte(String key, byte value)
    {
        data.add(new DataByte(value));
        return this;
    }

    public DataListBuilder putByteArray(String key, byte... value)
    {
        data.add(new DataByteArray(value, true));
        return this;
    }

    public DataListBuilder putDouble(String key, double value)
    {
        data.add(new DataDouble(value));
        return this;
    }

    public DataListBuilder putFloat(String key, float value)
    {
        data.add(new DataFloat(value));
        return this;
    }

    public DataListBuilder putInt(String key, int value)
    {
        data.add(new DataInt(value));
        return this;
    }

    public DataListBuilder putIntArray(String key, int... value)
    {
        data.add(new DataIntArray(value, true));
        return this;
    }

    public DataListBuilder putList(String key, DataListBuilder valueBuilder)
    {
        data.add(valueBuilder.build());
        return this;
    }

    public DataListBuilder putMap(String key, DataMapBuilder valueBuilder)
    {
        data.add(valueBuilder.build());
        return this;
    }

    public DataListBuilder putLong(String key, long value)
    {
        data.add(new DataLong(value));
        return this;
    }

    public DataListBuilder putShort(String key, short value)
    {
        data.add(new DataShort(value));
        return this;
    }

    public DataListBuilder putString(String key, String value)
    {
        data.add(new DataString(value));
        return this;
    }

    public DataList build()
    {
        return new DataList(data, true);
    }
}
