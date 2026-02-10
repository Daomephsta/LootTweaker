package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestUtils.iitemstack;
import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;

import com.google.common.collect.ImmutableMap;

import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.DataString;
import crafttweaker.api.data.IData;
import daomephsta.loot_shared.zenscript.api.ZenLootCondition;
import daomephsta.loot_shared.zenscript.api.ZenLootFunction;
import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.loottweaker.test.mixin.condition.TestKilledByPlayerAccessors;
import io.github.daomephsta.loottweaker.test.mixin.function.TestSetCountAccessors;
import io.github.daomephsta.loottweaker.test.mixin.function.TestSetDamageAccessors;
import io.github.daomephsta.loottweaker.test.mixin.function.TestSetMetadataAccessors;
import io.github.daomephsta.loottweaker.test.mixin.function.TestSetNBTAccessors;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.LootTableTweakManagerImpl;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.factory.StaticLootConditionFactory;
import leviathan143.loottweaker.common.zenscript.factory.StaticLootFunctionFactory;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootPoolWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;


public class ItemEntryAdditionTests
{
    private final LootTweakerContext context = TestUtils.context();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntry()
    {
        LootTableTweakManagerImpl tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addItemEntry(iitemstack(Items.APPLE), 2, "qux");

        LootTable foo = tweakManager.withEdits(loadTable(fooId), fooId);
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.APPLE)
            .hasNoLootFunctions();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithQuality()
    {
        LootTableTweakManagerImpl tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addItemEntry(iitemstack(Items.APPLE), 2, 3, "qux");

        LootTable foo = tweakManager.withEdits(loadTable(fooId), fooId);
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(3)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.APPLE)
            .hasNoLootFunctions();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithCondition()
    {
        LootTableTweakManagerImpl tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addItemEntry(iitemstack(Items.BAKED_POTATO), 2, 3, new ZenLootFunction[0],
            new ZenLootCondition[] { StaticLootConditionFactory.killedByPlayer() }, "qux");

        LootTable foo = tweakManager.withEdits(loadTable(fooId), fooId);
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(3)
            .hasMatchingCondition(
                condition -> condition instanceof KilledByPlayer && !((TestKilledByPlayerAccessors) condition).isInverse(),
                "KilledByPlayer()")
            .asItemEntry()
            .spawnsItem(Items.BAKED_POTATO)
            .hasNoLootFunctions();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetCount()
    {
        LootTableTweakManagerImpl tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addItemEntry(iitemstack(Items.ARROW, 3), 2, "qux");

        LootTable foo = tweakManager.withEdits(loadTable(fooId), fooId);
        int expectedCount = 3;
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.ARROW)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetCount)
                {
                    RandomValueRange countRange = ((TestSetCountAccessors) function).getCountRange();
                    return countRange.getMin() == expectedCount && countRange.getMax() == expectedCount;
                }
                return false;
            }, "SetCount(%d)", expectedCount);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetCount()
    {
        LootTableTweakManagerImpl tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addItemEntry(iitemstack(Items.ARROW), 2, 1,
            new ZenLootFunction[] { StaticLootFunctionFactory.setCount(3, 3) }, new ZenLootCondition[0],
            "qux");

        LootTable foo = tweakManager.withEdits(loadTable(fooId), fooId);
        int expectedCount = 3;
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.ARROW)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetCount)
                {
                    RandomValueRange countRange = ((TestSetCountAccessors) function).getCountRange();
                    return countRange.getMin() == expectedCount && countRange.getMax() == expectedCount;
                }
                return false;
            }, "SetCount(%d)", expectedCount);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetDamage()
    {
        LootTableTweakManagerImpl tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        @SuppressWarnings("deprecation")
        int damage = Items.BOW.getMaxDamage() / 2;
        //set empty tag to work around weird Mojang code where items without NBT are undamageable
        barTweaks.addItemEntry(iitemstack(Items.BOW, 1, damage).withTag(DataMap.EMPTY, true), 2, "qux");

        LootTable foo = tweakManager.withEdits(loadTable(fooId), fooId);
        float expectedDamage = 0.5F;
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.BOW)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetDamage)
                {
                    RandomValueRange damageRange = ((TestSetDamageAccessors) function).getDamageRange();
                    return damageRange.getMin() == expectedDamage && damageRange.getMax() == expectedDamage;
                }
                return false;
            }, "SetDamage(%f)", expectedDamage);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetDamage()
    {
        LootTableTweakManagerImpl tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        //set empty tag to work around weird Mojang code where items without NBT are undamageable
        barTweaks.addItemEntry(iitemstack(Items.BOW), 2, 1,
            new ZenLootFunction[] { StaticLootFunctionFactory.setDamage(0.5F, 0.5F) },
            new ZenLootCondition[0], "qux");

        LootTable foo = tweakManager.withEdits(loadTable(fooId), fooId);
        float expectedDamage = 0.5F;
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.BOW)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetDamage)
                {
                    RandomValueRange damageRange = ((TestSetDamageAccessors) function).getDamageRange();
                    return damageRange.getMin() == expectedDamage && damageRange.getMax() == expectedDamage;
                }
                return false;
            }, "SetDamage(%f)", expectedDamage);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetMetadata()
    {
        LootTableTweakManagerImpl tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addItemEntry(iitemstack(Items.DYE, 1, 8), 2, "qux");

        LootTable foo = tweakManager.withEdits(loadTable(fooId), fooId);
        int expectedMetadata = 8;
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.DYE)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetMetadata)
                {
                    RandomValueRange metaRange = ((TestSetMetadataAccessors) function).getMetaRange();
                    return metaRange.getMin() == expectedMetadata && metaRange.getMax() == expectedMetadata;
                }
                return false;
            }, "SetMetadata(%d)", expectedMetadata);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetMetadata()
    {
        LootTableTweakManagerImpl tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addItemEntry(iitemstack(Items.DYE), 2, 1,
            new ZenLootFunction[] { StaticLootFunctionFactory.setMetadata(8, 8) }, new ZenLootCondition[0],
            "qux");

        LootTable foo = tweakManager.withEdits(loadTable(fooId), fooId);
        int expectedMetadata = 8;
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.DYE)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetMetadata)
                {
                    RandomValueRange metaRange = ((TestSetMetadataAccessors) function).getMetaRange();
                    return metaRange.getMin() == expectedMetadata && metaRange.getMax() == expectedMetadata;
                }
                return false;
            }, "SetMetadata(%d)", expectedMetadata);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetNBT()
    {
        LootTableTweakManagerImpl tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addItemEntry(iitemstack(Items.BREAD).withDisplayName("Super Bread"), 2, "qux");

        LootTable foo = tweakManager.withEdits(loadTable(fooId), fooId);
        NBTTagCompound expectedTag = new NBTTagCompound();
        {
            NBTTagCompound display = new NBTTagCompound();
            display.setString("Name", "Super Bread");
            expectedTag.setTag("display", display);
        }
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.BREAD)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetNBT)
                    return expectedTag.equals(((TestSetNBTAccessors) function).getTag());
                return false;
            }, "SetNBT(%s)", expectedTag);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetNBT()
    {
        LootTableTweakManagerImpl tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        IData displayData = new DataMap(
            ImmutableMap.<String, IData>builder().put("Name", new DataString("Super Bread")).build(), true);
        IData nbtData = new DataMap(ImmutableMap.<String, IData>builder().put("display", displayData).build(),
            true);
        barTweaks.addItemEntry(iitemstack(Items.BREAD), 2, 1,
            new ZenLootFunction[] { StaticLootFunctionFactory.setNBT(nbtData) }, new ZenLootCondition[0],
            "qux");

        LootTable foo = tweakManager.withEdits(loadTable(fooId), fooId);
        NBTTagCompound expectedTag = new NBTTagCompound();
        {
            NBTTagCompound display = new NBTTagCompound();
            display.setString("Name", "Super Bread");
            expectedTag.setTag("display", display);
        }
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.BREAD)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetNBT)
                    return expectedTag.equals(((TestSetNBTAccessors) function).getTag());
                return false;
            }, "SetNBT(%s)", expectedTag);
    }
}