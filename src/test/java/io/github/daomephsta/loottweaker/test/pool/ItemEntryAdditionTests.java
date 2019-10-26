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
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.factory.LootFunctionFactory;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootFunctionWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootPoolWrapper;
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
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.addItemEntry(iitemstack(Items.APPLE), 2, "qux");
        barTweaks.tweak(bar);
        
        assertThat(bar)
            .extractEntry("qux")
            .hasWeight(2)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.APPLE)
            .hasNoLootFunctions();
    }
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithQuality() 
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.addItemEntry(iitemstack(Items.APPLE), 2, 3, "qux");
        barTweaks.tweak(bar);
        
        assertThat(bar)
            .extractEntry("qux")
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
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.addItemEntryHelper(iitemstack(Items.BAKED_POTATO), 2, 3, 
            new ZenLootFunctionWrapper[0], 
            new ZenLootConditionWrapper[] {LootConditionFactory.killedByPlayer()}, 
            "qux");
        barTweaks.tweak(bar);
        
        assertThat(bar)
            .extractEntry("qux")
            .hasWeight(2)
            .hasQuality(3)
            .hasMatchingCondition(condition -> 
                condition instanceof KilledByPlayer && !isInverted((KilledByPlayer) condition), 
            "KilledByPlayer()")
            .asItemEntry()
            .spawnsItem(Items.BAKED_POTATO)
            .hasNoLootFunctions();
    }
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetCount() 
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.addItemEntry(iitemstack(Items.ARROW, 3), 2, "qux");
        barTweaks.tweak(bar);

        int expectedCount = 3;
        assertThat(bar)
            .extractEntry("qux")
            .hasWeight(2)
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
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetCount() 
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.addItemEntryHelper(iitemstack(Items.ARROW), 2, 1, 
            new ZenLootFunctionWrapper[] {LootFunctionFactory.setCount(3, 3)}, 
            new ZenLootConditionWrapper[0], 
            "qux");
        barTweaks.tweak(bar);
        
        int expectedCount = 3;
        assertThat(bar)
            .extractEntry("qux")
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
    }
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetDamage() 
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        @SuppressWarnings("deprecation")
        int damage = Items.BOW.getMaxDamage() / 2;
        //set empty tag to work around weird Mojang code where items without NBT are undamageable
        barTweaks.addItemEntry(iitemstack(Items.BOW, 1, damage).withTag(DataMap.EMPTY, true), 2, "qux");
        barTweaks.tweak(bar);
        
        float expectedDamage = 0.5F;
        assertThat(bar)
            .extractEntry("qux")
            .hasWeight(2)
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
    }
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetDamage() 
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        //set empty tag to work around weird Mojang code where items without NBT are undamageable
        barTweaks.addItemEntryHelper(iitemstack(Items.BOW), 2, 1,
            new ZenLootFunctionWrapper[] {LootFunctionFactory.setDamage(0.5F, 0.5F)}, 
            new ZenLootConditionWrapper[0], 
            "qux");
        barTweaks.tweak(bar);
        
        float expectedDamage = 0.5F;
        assertThat(bar)
            .extractEntry("qux")
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
    }
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetMetadata() 
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.addItemEntry(iitemstack(Items.DYE, 1, 8), 2, "qux");
        barTweaks.tweak(bar);
        
        int expectedMetadata = 8;
        assertThat(bar)
            .extractEntry("qux")
            .hasWeight(2)
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
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetMetadata() 
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.addItemEntryHelper(iitemstack(Items.DYE), 2, 1,
            new ZenLootFunctionWrapper[] {LootFunctionFactory.setMetadata(8, 8)}, 
            new ZenLootConditionWrapper[0], 
            "qux");
        barTweaks.tweak(bar);
        
        int expectedMetadata = 8;
        assertThat(bar)
            .extractEntry("qux")
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
    }
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetNBT() 
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.addItemEntry(iitemstack(Items.BREAD).withDisplayName("Super Bread"), 2, "qux");
        barTweaks.tweak(bar);
        
        NBTTagCompound expectedTag = new NBTTagCompound();
        {
            NBTTagCompound display = new NBTTagCompound();
            display.setString("Name", "Super Bread");
            expectedTag.setTag("display", display);
        }
        assertThat(bar)
            .extractEntry("qux")
            .hasWeight(2)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.BREAD)
            .hasMatchingFunction(function -> 
            {
                if (function instanceof SetNBT)
                    return expectedTag.equals(getTag((SetNBT) function));
                return false;
            }, "SetNBT(%s)", expectedTag);
    }
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetNBT() 
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        IData displayData = new DataMap(ImmutableMap.<String, IData>builder()
            .put("Name", new DataString("Super Bread")).build(), true);
        IData nbtData = new DataMap(ImmutableMap.<String, IData>builder()
            .put("display", displayData).build(), true);
        barTweaks.addItemEntryHelper(iitemstack(Items.BREAD), 2, 1,
            new ZenLootFunctionWrapper[] {LootFunctionFactory.setNBT(nbtData)}, 
            new ZenLootConditionWrapper[0], 
            "qux");
        barTweaks.tweak(bar);

        NBTTagCompound expectedTag = new NBTTagCompound();
        {
            NBTTagCompound display = new NBTTagCompound();
            display.setString("Name", "Super Bread");
            expectedTag.setTag("display", display);
        }
        assertThat(bar)
            .extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.BREAD)
            .hasMatchingFunction(function -> 
            {
                if (function instanceof SetNBT)
                    return expectedTag.equals(getTag((SetNBT) function));
                return false;
            }, "SetNBT(%s)", expectedTag);
    }
}