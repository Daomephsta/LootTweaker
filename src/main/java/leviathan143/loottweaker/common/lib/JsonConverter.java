package leviathan143.loottweaker.common.lib;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.*;

import crafttweaker.api.data.*;

public class JsonConverter implements IDataConverter<JsonElement>
{
	private static final JsonConverter INSTANCE = new JsonConverter();
	private static final Gson SERIALISER = new GsonBuilder()
        .registerTypeAdapter(DataBool.class, serialiser(src -> new JsonPrimitive(src.asBool())))
        .registerTypeAdapter(DataByte.class, serialiser(src -> new JsonPrimitive(src.asByte())))
        .registerTypeAdapter(DataShort.class, serialiser(src -> new JsonPrimitive(src.asShort())))
        .registerTypeAdapter(DataInt.class, serialiser(src -> new JsonPrimitive(src.asInt())))
        .registerTypeAdapter(DataLong.class, serialiser(src -> new JsonPrimitive(src.asLong())))
        .registerTypeAdapter(DataFloat.class, serialiser(src -> new JsonPrimitive(src.asFloat())))
        .registerTypeAdapter(DataDouble.class, serialiser(src -> new JsonPrimitive(src.asDouble())))
        .registerTypeAdapter(DataString.class, serialiser(src -> new JsonPrimitive(src.asString())))
        .registerTypeAdapter(DataByteArray.class, contextSerialiser(IData::asByteArray))
        .registerTypeAdapter(DataIntArray.class, contextSerialiser(IData::asIntArray))
        .registerTypeAdapter(DataList.class, contextSerialiser(IData::asList))
        .registerTypeAdapter(DataMap.class, contextSerialiser(IData::asMap))
        .create();

	private static JsonSerializer<IData> serialiser(Function<IData, JsonElement> serialiser)
	{
	    return (src, srcType, context) -> serialiser.apply(src);
	}

    private static JsonSerializer<IData> contextSerialiser(Function<IData, Object> serialiser)
    {
        return (src, srcType, context) -> context.serialize(serialiser.apply(src));
    }

	public static JsonElement from(IData data)
	{
		return data.convert(INSTANCE);
	}

	@Override
	public JsonElement fromBool(boolean bool)
	{
		return new JsonPrimitive(bool);
	}

	@Override
	public JsonElement fromByte(byte b)
	{
		return new JsonPrimitive(b);
	}

	@Override
	public JsonElement fromByteArray(byte[] b)
	{
		return SERIALISER.toJsonTree(b);
	}

	@Override
	public JsonElement fromDouble(double d)
	{
		return new JsonPrimitive(d);
	}

	@Override
	public JsonElement fromFloat(float f)
	{
		return new JsonPrimitive(f);
	}

	@Override
	public JsonElement fromInt(int i)
	{
		return new JsonPrimitive(i);
	}

	@Override
	public JsonElement fromIntArray(int[] i)
	{
		return SERIALISER.toJsonTree(i);
	}

	@Override
	public JsonElement fromList(List<IData> list)
	{
		return SERIALISER.toJsonTree(list);
	}

	@Override
	public JsonElement fromLong(long l)
	{
		return new JsonPrimitive(l);
	}

	@Override
	public JsonElement fromMap(Map<String, IData> map)
	{
		return SERIALISER.toJsonTree(map);
	}

	@Override
	public JsonElement fromShort(short s)
	{
		return new JsonPrimitive(s);
	}

	@Override
	public JsonElement fromString(String str)
	{
		return new JsonPrimitive(str);
	}
}
