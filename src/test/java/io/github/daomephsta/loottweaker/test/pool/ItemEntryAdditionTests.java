package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestLootConditionAccessors.isInverted;
import static io.github.daomephsta.loottweaker.test.TestLootFunctionAccessors.getCountRange;
import static io.github.daomephsta.loottweaker.test.TestLootFunctionAccessors.getDamageRange;
import static io.github.daomephsta.loottweaker.test.TestLootFunctionAccessors.getMetaRange;
import static io.github.daomephsta.loottweaker.test.TestLootFunctionAccessors.getTag;
import static io.github.daomephsta.loottweaker.test.TestUtils.iitemstack;
import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;

import com.google.common.collect.ImmutableMap;

import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.DataString;
import crafttweaker.api.data.IData;
import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.loottweaker.test.util.DataMapBuilder;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.api.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.impl.MutableLootPool;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;


public class ItemEntryAdditionTests
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntry()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addItemEntry(iitemstack(Items.APPLE), "qux");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("qux")
            .hasWeight(1)
            .hasQuality(0)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.APPLE)
            .hasNoLootFunctions();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithWeight()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addItemEntry(iitemstack(Items.APPLE), 2, "qux");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(0)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.APPLE)
            .hasNoLootFunctions();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithQuality()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addItemEntry(iitemstack(Items.APPLE), 2, 3, "qux");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(3)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.APPLE)
            .hasNoLootFunctions();
    }

    /*@SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithCondition()
    {
        LootTweakerContext context = TestUtils.createContext();
        LootConditionFactory conditionFactory = context.lootSystem().getLootConditionFactory();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addItemEntryHelper(iitemstack(Items.BAKED_POTATO), 2, 3, new ZenLootFunctionWrapper[0], new ZenLootConditionWrapper[] { LootConditionFactory.killedByPlayer() }, "qux");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("qux").hasWeight(2).hasQuality(3).hasMatchingCondition(condition -> condition instanceof KilledByPlayer && !isInverted((KilledByPlayer) condition), "KilledByPlayer()").asItemEntry()
            .spawnsItem(Items.BAKED_POTATO).hasNoLootFunctions();
    }*/

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryJson()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addItemEntryJson(iitemstack(Items.BAKED_POTATO), 2, 3, new IData[0], new IData[] { new DataMapBuilder().putString("condition", "minecraft:killed_by_player").build() }, "qux");

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(3)
            .hasMatchingCondition(condition -> condition instanceof KilledByPlayer
                && !isInverted((KilledByPlayer) condition), "KilledByPlayer()")
            .asItemEntry()
            .spawnsItem(Items.BAKED_POTATO)
            .hasNoLootFunctions();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetCount()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addItemEntry(iitemstack(Items.ARROW, 3), 2, "qux");

        int expectedCount = 3;
        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(0)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.ARROW)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetCount)
                {
                    RandomValueRange countRange = getCountRange((SetCount) function);
                    return countRange.getMin() == expectedCount && countRange.getMax() == expectedCount;
                }
                return false;
            }, "SetCount(%d)", expectedCount);
    }

    /*@SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetCount()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addItemEntryHelper(iitemstack(Items.ARROW), 2, 1, new ZenLootFunctionWrapper[] { LootFunctionFactory.setCount(3, 3) }, new ZenLootConditionWrapper[0], "qux");

        int expectedCount = 3;
        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.ARROW)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetCount)
                {
                    RandomValueRange countRange = getCountRange((SetCount) function);
                    return countRange.getMin() == expectedCount && countRange.getMax() == expectedCount;
                }
                return false;
            }, "SetCount(%d)", expectedCount);
    }*/

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetDamage()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);

        @SuppressWarnings("deprecation")
        int damage = Items.BOW.getMaxDamage() / 2;
        //set empty tag to work around weird Mojang code where items without NBT are undamageable
        mutableBar.addItemEntry(iitemstack(Items.BOW, 1, damage).withTag(DataMap.EMPTY, true), 2, "qux");

        LootPool barNew = mutableBar.toImmutable();
        float expectedDamage = 0.5F;
        assertThat(barNew).extractEntry("qux").hasWeight(2).hasNoLootConditions().asItemEntry().spawnsItem(Items.BOW).hasMatchingFunction(function ->
        {
            if (function instanceof SetDamage)
            {
                RandomValueRange damageRange = getDamageRange((SetDamage) function);
                return damageRange.getMin() == expectedDamage && damageRange.getMax() == expectedDamage;
            }
            return false;
        }, "SetDamage(%f)", expectedDamage);
    }

    /*@SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetDamage()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addItemEntryHelper(iitemstack(Items.BOW), 2, 1, new ZenLootFunctionWrapper[] { LootFunctionFactory.setDamage(0.5F, 0.5F) }, new ZenLootConditionWrapper[0], "qux");

        LootPool barNew = mutableBar.toImmutable();
        float expectedDamage = 0.5F;
        assertThat(barNew).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.BOW)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetDamage)
                {
                    RandomValueRange damageRange = getDamageRange((SetDamage) function);
                    return damageRange.getMin() == expectedDamage && damageRange.getMax() == expectedDamage;
                }
                return false;
            }, "SetDamage(%f)", expectedDamage);
    }*/

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetMetadata()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addItemEntry(iitemstack(Items.DYE, 1, 8), 2, "qux");

        int expectedMetadata = 8;
        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(0)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.DYE)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetMetadata)
                {
                    RandomValueRange metaRange = getMetaRange((SetMetadata) function);
                    return metaRange.getMin() == expectedMetadata && metaRange.getMax() == expectedMetadata;
                }
                return false;
            }, "SetMetadata(%d)", expectedMetadata);
    }

    /*@SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetMetadata()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addItemEntryHelper(iitemstack(Items.DYE), 2, 1, new ZenLootFunctionWrapper[] { LootFunctionFactory.setMetadata(8, 8) }, new ZenLootConditionWrapper[0], "qux");

        int expectedMetadata = 8;
        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.DYE)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetMetadata)
                {
                    RandomValueRange metaRange = getMetaRange((SetMetadata) function);
                    return metaRange.getMin() == expectedMetadata && metaRange.getMax() == expectedMetadata;
                }
                return false;
            }, "SetMetadata(%d)", expectedMetadata);
    }*/

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetNBT()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.addItemEntry(iitemstack(Items.BREAD).withDisplayName("Super Bread"), 2, "qux");

        NBTTagCompound expectedTag = new NBTTagCompound();
        {
            NBTTagCompound display = new NBTTagCompound();
            display.setString("Name", "Super Bread");
            expectedTag.setTag("display", display);
        }
        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("qux").hasWeight(2).hasNoLootConditions().asItemEntry().spawnsItem(Items.BREAD).hasMatchingFunction(function ->
        {
            if (function instanceof SetNBT) return expectedTag.equals(getTag((SetNBT) function));
            return false;
        }, "SetNBT(%s)", expectedTag);
    }

    /*@SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetNBT()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootPool barOriginal = loadTable(fooId).getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        IData displayData = new DataMap(ImmutableMap.<String, IData>builder().put("Name", new DataString("Super Bread")).build(), true);
        IData nbtData = new DataMap(ImmutableMap.<String, IData>builder().put("display", displayData).build(), true);
        mutableBar.addItemEntryHelper(iitemstack(Items.BREAD), 2, 1, new ZenLootFunctionWrapper[] { LootFunctionFactory.setNBT(nbtData) }, new ZenLootConditionWrapper[0], "qux");

        NBTTagCompound expectedTag = new NBTTagCompound();
        {
            NBTTagCompound display = new NBTTagCompound();
            display.setString("Name", "Super Bread");
            expectedTag.setTag("display", display);
        }
        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.BREAD)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetNBT) return expectedTag.equals(getTag((SetNBT) function));
                return false;
            }, "SetNBT(%s)", expectedTag);
    }*/
}
