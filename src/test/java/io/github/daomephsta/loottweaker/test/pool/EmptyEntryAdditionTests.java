package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestLootConditionAccessors.isInverted;
import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;

import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootPoolWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;


public class EmptyEntryAdditionTests
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntry() 
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.addEmptyEntry(2, "corge");
        barTweaks.tweak(bar);
        
        assertThat(bar)
            .extractEntry("corge")
            .hasWeight(2)
            .hasNoLootConditions()
            .isEmptyEntry();
    }
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntryWithQuality() 
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.addEmptyEntry(2, 3, "corge");
        barTweaks.tweak(bar);
        
        assertThat(bar)
            .extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasNoLootConditions()
            .isEmptyEntry();
    }
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntryWithCondition() 
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.addEmptyEntryHelper(2, 3, 
            new ZenLootConditionWrapper[] {LootConditionFactory.killedByPlayer()}, 
            "corge");
        barTweaks.tweak(bar);
        
        assertThat(bar)
            .extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasMatchingCondition(condition -> 
                condition instanceof KilledByPlayer && !isInverted((KilledByPlayer) condition), 
            "KilledByPlayer()")
            .isEmptyEntry();
    }
}