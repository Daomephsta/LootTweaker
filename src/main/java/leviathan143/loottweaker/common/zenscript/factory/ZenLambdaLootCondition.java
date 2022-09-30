package leviathan143.loottweaker.common.zenscript.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.util.IRandom;
import crafttweaker.mc1120.util.MCRandom;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import stanhebben.zenscript.annotations.ZenClass;


public class ZenLambdaLootCondition implements LootCondition
{
    public static final LootCondition.Serializer<ZenLambdaLootCondition> SERIALISER = new Serialiser();
    private static final List<ZenLambdaLootCondition> INSTANCES = new ArrayList<>();
    private final Delegate delegate;
    private final int id;

    public ZenLambdaLootCondition(Delegate delegate)
    {
        this.delegate = delegate;
        INSTANCES.add(this);
        this.id = INSTANCES.size() - 1;
    }

    @Override
    public boolean testCondition(Random rand, LootContext context)
    {
        return delegate.test(new MCRandom(rand), new ZenLootContext(context));
    }

    @ZenRegister
    @ZenClass(LootTweaker.MODID + ".CustomLootCondition")
    @FunctionalInterface
    public interface Delegate
    {
        public boolean test(IRandom rand, ZenLootContext context);
    }

    private static class Serialiser extends LootCondition.Serializer<ZenLambdaLootCondition>
    {
        protected Serialiser()
        {
            super(new ResourceLocation(LootTweaker.MODID, "zen_lambda"), ZenLambdaLootCondition.class);
        }

        @Override
        public void serialize(JsonObject object, ZenLambdaLootCondition function, JsonSerializationContext context)
        {
            object.addProperty("lambda_id", function.id);
        }

        @Override
        public ZenLambdaLootCondition deserialize(JsonObject json, JsonDeserializationContext context)
        {
            return INSTANCES.get(JsonUtils.getInt(json, "lambda_id"));
        }
    }
}
