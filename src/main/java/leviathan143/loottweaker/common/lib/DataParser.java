package leviathan143.loottweaker.common.lib;

import java.util.Optional;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import crafttweaker.api.data.IData;

public class DataParser
{
    private final Gson gsonInstance;
    private final Consumer<JsonSyntaxException> errorHandler;

    public DataParser(Gson gsonInstance, Consumer<JsonSyntaxException> errorHandler)
    {
        this.gsonInstance = gsonInstance;
        this.errorHandler = errorHandler;
    }

    public <T> Optional<T> parse(IData data, Class<T> clazz)
    {
        try
        {
            return Optional.of(gsonInstance.fromJson(JsonConverter.from(data), clazz));
        }
        catch (JsonSyntaxException e)
        {
            errorHandler.accept(e);
            return Optional.empty();
        }
    }
}
