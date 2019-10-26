package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestLootConditionAccessors.isInverted;
import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootPoolWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;

public class MiscZenLootPoolWrapperTests
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addConditions()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.addConditionsHelper(new ZenLootConditionWrapper[] {LootConditionFactory.killedByPlayer()});
        barTweaks.tweak(bar);
        
        assertThat(bar).hasMatchingCondition(condition -> 
            condition instanceof KilledByPlayer && !isInverted((KilledByPlayer) condition), 
        "KilledByPlayer()");
    }
    
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeEntry()
    {
        ResourceLocation barId = new ResourceLocation("loottweaker", "bar");
        LootTable bar = loadTable(barId);
        LootPool baz = bar.getPool("baz");
        
        assertThat(baz.getEntry("qux")).isNotNull();
        ZenLootPoolWrapper bazTweaks = new ZenLootPoolWrapper("baz", barId);
        bazTweaks.removeEntry("qux");
        bazTweaks.tweak(baz);
        assertThat(baz.getEntry("qux")).isNull();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setRolls()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.setRolls(2.0F, 5.0F);
        barTweaks.tweak(bar);
        
        assertThat(bar.getRolls()).extracting(RandomValueRange::getMin).isEqualTo(2.0F);
        assertThat(bar.getRolls()).extracting(RandomValueRange::getMax).isEqualTo(5.0F);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setBonusRolls()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool bar = foo.getPool("bar");
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper(bar.getName(), fooId);
        barTweaks.setBonusRolls(1.0F, 3.0F);
        barTweaks.tweak(bar);
        
        assertThat(bar.getBonusRolls()).extracting(RandomValueRange::getMin).isEqualTo(1.0F);
        assertThat(bar.getBonusRolls()).extracting(RandomValueRange::getMax).isEqualTo(3.0F);
    }
}
