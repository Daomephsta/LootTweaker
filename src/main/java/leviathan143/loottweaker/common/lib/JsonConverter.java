package leviathan143.loottweaker.common.lib;

import java.util.List;
import java.util.Map;

import com.google.gson.*;

import crafttweaker.api.data.*;

public class JsonConverter implements IDataConverter<JsonElement>
{
	private static final JsonConverter INSTANCE = new JsonConverter();
	private static final Gson SERIALISER = new GsonBuilder()
			.registerTypeAdapter(DataBool.class, (JsonSerializer<IData>) (src, srcType, context) -> new JsonPrimitive(src.asBool()))
			.registerTypeAdapter(DataByte.class, (JsonSerializer<IData>) (src, srcType, context) -> new JsonPrimitive(src.asByte()))
			.registerTypeAdapter(DataShort.class, (JsonSerializer<IData>) (src, srcType, context) -> new JsonPrimitive(src.asShort()))
			.registerTypeAdapter(DataInt.class, (JsonSerializer<IData>) (src, srcType, context) -> new JsonPrimitive(src.asInt()))
			.registerTypeAdapter(DataLong.class, (JsonSerializer<IData>) (src, srcType, context) -> new JsonPrimitive(src.asLong()))
			.registerTypeAdapter(DataFloat.class, (JsonSerializer<IData>) (src, srcType, context) -> new JsonPrimitive(src.asFloat()))
			.registerTypeAdapter(DataDouble.class, (JsonSerializer<IData>) (src, srcType, context) -> new JsonPrimitive(src.asDouble()))
			.registerTypeAdapter(DataString.class, (JsonSerializer<IData>) (src, srcType, context) -> new JsonPrimitive(src.asString()))
			.registerTypeAdapter(DataByteArray.class, (JsonSerializer<IData>) (src, srcType, context) -> context.serialize(src.asByteArray()))
			.registerTypeAdapter(DataIntArray.class, (JsonSerializer<IData>) (src, srcType, context) -> context.serialize(src.asIntArray()))
			.registerTypeAdapter(DataList.class, (JsonSerializer<IData>) (src, srcType, context) -> context.serialize(src.asList()))
			.registerTypeAdapter(DataMap.class, (JsonSerializer<IData>) (src, srcType, context) -> context.serialize(src.asMap()))
			.create();
	
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
