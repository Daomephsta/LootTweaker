package io.github.daomephsta.loottweaker.test.entry;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Random;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import crafttweaker.api.data.IData;
import crafttweaker.api.minecraft.CraftTweakerMC;
import io.github.daomephsta.loottweaker.test.ThrowingErrorHandler;
import io.github.daomephsta.loottweaker.test.ThrowingErrorHandler.LootTweakerException;
import io.github.daomephsta.loottweaker.test.util.DataMapBuilder;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.LTConfig;
import leviathan143.loottweaker.common.lib.LootConditions;
import leviathan143.loottweaker.common.zenscript.impl.entry.FunctionEffects;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.*;

public class FunctionEffectsTests
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void metadata()
    {
        FunctionEffects effects = new FunctionEffects(Items.DYE).initialise(
            () -> "test", Arrays.asList(setMetadata(1, 3)), new ThrowingErrorHandler());

        assertThat(effects.getMinMeta()).isEqualTo(1);
        assertThat(effects.getMaxMeta()).isEqualTo(3);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void conditionalMetadata()
    {
        Assertions.assertThatThrownBy(() ->
        {
            new FunctionEffects(Items.DYE).initialise(() -> "test",
                Arrays.asList(setMetadata(1, 2, new KilledByPlayer(false))),
                new ThrowingErrorHandler());
        })
        .isInstanceOf(LootTweakerException.class)
        .hasMessage("test has a conditional 'set_data' function. Cannot compute metadata.");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void multiMetadata()
    {
        Assertions.assertThatThrownBy(() ->
        {
            new FunctionEffects(Items.DYE).initialise(() -> "test",
                Arrays.asList(setMetadata(1, 2), setMetadata(3, 4)),
                new ThrowingErrorHandler());
        })
        .isInstanceOf(LootTweakerException.class)
        .hasMessage("test has multiple 'set_data' functions. Cannot compute metadata.");
    }

    private SetMetadata setMetadata(int minIn, int maxIn, LootCondition... conditions)
    {
        return new SetMetadata(conditions, new RandomValueRange(minIn, maxIn));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void damage()
    {
        FunctionEffects effects = new FunctionEffects(Items.FISHING_ROD).initialise(
            () -> "test", Arrays.asList(setDamage(0.25F, 0.5F)), new ThrowingErrorHandler());

        assertThat(effects.getMinDamagePercent()).isEqualTo(0.25F);
        assertThat(effects.getMinDamageAmount()).isEqualTo(48);
        assertThat(effects.getMaxDamagePercent()).isEqualTo(0.5F);
        assertThat(effects.getMaxDamageAmount()).isEqualTo(32);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void conditionalDamage()
    {
        Assertions.assertThatThrownBy(() ->
        {
            new FunctionEffects(Items.DYE).initialise(() -> "test",
                Arrays.asList(setDamage(0.25F, 0.5F, new KilledByPlayer(false))),
                new ThrowingErrorHandler());
        })
        .isInstanceOf(LootTweakerException.class)
        .hasMessage("test has a conditional 'set_damage' function. Cannot compute damage.");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void multiDamage()
    {
        Assertions.assertThatThrownBy(() ->
        {
            new FunctionEffects(Items.DYE).initialise(() -> "test",
                Arrays.asList(setDamage(0.25F, 0.5F), setDamage(0.1F, 0.2F)),
                new ThrowingErrorHandler());
        })
        .isInstanceOf(LootTweakerException.class)
        .hasMessage("test has multiple 'set_damage' functions. Cannot compute damage.");
    }

    private SetDamage setDamage(float minIn, float maxIn, LootCondition... conditions)
    {
        return new SetDamage(conditions, new RandomValueRange(minIn, maxIn));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void nbt()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("foo", "bar");
        FunctionEffects effects = new FunctionEffects(Items.DYE).initialise(() -> "test",
            Arrays.asList(setNbt(nbt)), new ThrowingErrorHandler());

        IData expected = CraftTweakerMC.getIData(nbt);
        IData actual = effects.getNbt();
        assertThat(actual.equals(expected))
            .as("\nExpecting:\n <%s>\nto be equal to:\n <%s>\nbut was not.", actual, expected)
            .isTrue();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void conditionalNbt()
    {
        Assertions.assertThatThrownBy(() ->
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("foo", "bar");
            new FunctionEffects(Items.DYE).initialise(() -> "test",
                Arrays.asList(setNbt(nbt, new KilledByPlayer(false))),
                new ThrowingErrorHandler());
        })
        .isInstanceOf(LootTweakerException.class)
        .hasMessage("test has a conditional 'set_nbt' function. Cannot compute NBT.");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void multiNbt()
    {
        NBTTagCompound nbtA = new NBTTagCompound();
        nbtA.setString("foo", "bar");
        NBTTagCompound nbtB = new NBTTagCompound();
        nbtB.setString("baz", "qux");
        FunctionEffects effects = new FunctionEffects(Items.DYE).initialise(() -> "test",
            Arrays.asList(setNbt(nbtA), setNbt(nbtB)), new ThrowingErrorHandler());

        IData expected = new DataMapBuilder()
            .putString("foo", "bar")
            .putString("baz", "qux")
            .build();
        IData actual = effects.getNbt();
        assertThat(actual.equals(expected))
            .as("\nExpecting:\n <%s>\nto be equal to:\n <%s>\nbut was not.", actual, expected)
            .isTrue();
    }

    private SetNBT setNbt(NBTTagCompound nbt, LootCondition... conditions)
    {
        return new SetNBT(conditions, nbt);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void nonVanillaFunctions()
    {
        Assertions.assertThatThrownBy(() ->
        {
            new FunctionEffects(Items.DYE).initialise(() -> "test",
                Arrays.asList(new Test()), new ThrowingErrorHandler());
        })
        .isInstanceOf(LootTweakerException.class)
        .hasMessage("test has non-vanilla functions. Computed NBT, damage, and/or metadata may be inaccurate.");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void smelt()
    {
        FunctionEffects effects = new FunctionEffects(Items.BEEF).initialise(
            () -> "test", Arrays.asList(smeltLootFn()), new ThrowingErrorHandler());

        assertThat(effects.getItem()).isEqualTo(Items.COOKED_BEEF);
        assertThat(effects.getMinMeta()).isEqualTo(0);
        assertThat(effects.getMaxMeta()).isEqualTo(0);
        assertThat(effects.getMinDamageAmount()).isEqualTo(-1);
        assertThat(effects.getMaxDamageAmount()).isEqualTo(-1);
        assertThat(effects.getMinDamagePercent()).isEqualTo(-1.0F);
        assertThat(effects.getMaxDamagePercent()).isEqualTo(-1.0F);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void smeltRangedMeta()
    {
        Assertions.assertThatThrownBy(() ->
        {
            new FunctionEffects(Items.FISH).initialise(
                () -> "test", Arrays.asList(setMetadata(0, 1), smeltLootFn()),
                new ThrowingErrorHandler());
        })
        .isInstanceOf(LootTweakerException.class)
        .hasMessage("test has a 'smelt' function, but ranged damage/metadata. "
            + "Cannot compute NBT, damage/metadata, or item.");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void smeltFixedMeta()
    {
        FunctionEffects effects = new FunctionEffects(Items.FISH).initialise(
            () -> "test", Arrays.asList(setMetadata(1, 1), smeltLootFn()),
            new ThrowingErrorHandler());

        assertThat(effects.getItem()).isEqualTo(Items.COOKED_FISH);
        assertThat(effects.getMinMeta()).isEqualTo(1);
        assertThat(effects.getMaxMeta()).isEqualTo(1);
        assertThat(effects.getMinDamageAmount()).isEqualTo(-1);
        assertThat(effects.getMaxDamageAmount()).isEqualTo(-1);
        assertThat(effects.getMinDamagePercent()).isEqualTo(-1.0F);
        assertThat(effects.getMaxDamagePercent()).isEqualTo(-1.0F);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void smeltRangedDamage()
    {
        Assertions.assertThatThrownBy(() ->
        {
            new FunctionEffects(Items.IRON_AXE).initialise(
                () -> "test", Arrays.asList(setDamage(0.25F, 0.5F), smeltLootFn()),
                new ThrowingErrorHandler());
        })
        .isInstanceOf(LootTweakerException.class)
        .hasMessage("test has a 'smelt' function, but ranged damage/metadata. "
            + "Cannot compute NBT, damage/metadata, or item.");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void smeltFixedDamage()
    {
        FunctionEffects effects = new FunctionEffects(Items.IRON_AXE).initialise(
            () -> "test", Arrays.asList(setDamage(0.25F, 0.25F), smeltLootFn()),
            new ThrowingErrorHandler());

        assertThat(effects.getItem()).isEqualTo(Items.IRON_NUGGET);
        assertThat(effects.getMinMeta()).isEqualTo(0);
        assertThat(effects.getMaxMeta()).isEqualTo(0);
        assertThat(effects.getMinDamageAmount()).isEqualTo(-1);
        assertThat(effects.getMaxDamageAmount()).isEqualTo(-1);
        assertThat(effects.getMinDamagePercent()).isEqualTo(-1.0F);
        assertThat(effects.getMaxDamagePercent()).isEqualTo(-1.0F);
    }

    private Smelt smeltLootFn(LootCondition... conditions)
    {
        return new Smelt(conditions);
    }

    @BeforeAll
    public static void beforeAll()
    {
        LTConfig.packdevMode = true;
    }

    @AfterAll
    public static void afterAll()
    {
        LTConfig.packdevMode = false;
    }

    private static class Test extends LootFunction
    {
        protected Test()
        {
            super(LootConditions.NONE);
        }

        @Override
        public ItemStack apply(ItemStack stack, Random rand, LootContext context)
        {
            return stack;
        }
    }
}
