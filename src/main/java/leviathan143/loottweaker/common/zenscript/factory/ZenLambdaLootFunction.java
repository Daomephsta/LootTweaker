package leviathan143.loottweaker.common.zenscript.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.util.IRandom;
import crafttweaker.mc1120.util.MCRandom;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import stanhebben.zenscript.annotations.ZenClass;


public class ZenLambdaLootFunction extends LootFunction
{
    public static final LootFunction.Serializer<ZenLambdaLootFunction> SERIALISER = new Serialiser();
    private static final List<ZenLambdaLootFunction> INSTANCES = new ArrayList<>();
    private final Delegate delegate;
    private final int id;

    public ZenLambdaLootFunction(Delegate delegate, LootCondition[] conditions)
    {
        super(conditions);
        this.delegate = delegate;
        INSTANCES.add(this);
        this.id = INSTANCES.size() - 1;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context)
    {
        IItemStack stackIn = CraftTweakerMC.getIItemStack(stack),
            stackOut = delegate.apply(stackIn, new MCRandom(rand), new ZenLootContext(context));
        return CraftTweakerMC.getItemStack(stackOut);
    }

    @ZenRegister
    @ZenClass(LootTweaker.MODID + ".CustomLootFunction")
    @FunctionalInterface
    public interface Delegate
    {
        public IItemStack apply(IItemStack stack, IRandom rand, ZenLootContext context);
    }

    private static class Serialiser extends LootFunction.Serializer<ZenLambdaLootFunction>
    {
        protected Serialiser()
        {
            super(new ResourceLocation(LootTweaker.MODID, "zen_lambda"), ZenLambdaLootFunction.class);
        }

        @Override
        public void serialize(JsonObject object, ZenLambdaLootFunction function, JsonSerializationContext context)
        {
            object.addProperty("lambda_id", function.id);
        }

        @Override
        public ZenLambdaLootFunction deserialize(JsonObject object, JsonDeserializationContext context,
            LootCondition[] conditions)
        {
            return INSTANCES.get(JsonUtils.getInt(object, "lambda_id"));
        }
    }
}
