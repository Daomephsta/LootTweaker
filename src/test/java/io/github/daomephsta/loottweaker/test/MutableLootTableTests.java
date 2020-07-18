package io.github.daomephsta.loottweaker.test;

import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.SoftAssertions;

import io.github.daomephsta.loottweaker.test.ThrowingErrorHandler.LootTweakerException;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.darkmagic.LootTableAccessors;
import leviathan143.loottweaker.common.zenscript.api.LootPoolRepresentation;
import leviathan143.loottweaker.common.zenscript.api.LootSystemInterface;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.impl.MutableLootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;


public class MutableLootTableTests
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeExistingPool()
    {
        LootTweakerContext context = TestUtils.createContext();
        LootSystemInterface lootSystem = context.lootSystem();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        MutableLootTable mutableFoo = mutableLootTable(fooId, context);
        LootTable fooOriginal = loadTable(fooId);
        assertThat(fooOriginal.getPool("bar")).isNotNull();
        mutableFoo.removePool("bar");
        LootTable fooNew = mutableFoo.toImmutable();
        assertThat(fooNew.getPool("bar")).isNull();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeNonExistentPool()
    {
        LootTweakerContext context = TestUtils.createContext();
        LootSystemInterface lootSystem = context.lootSystem();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        MutableLootTable mutableFoo = mutableLootTable(fooId, context);
        LootTable fooOriginal = loadTable(fooId);
        assertThat(fooOriginal.getPool("quuz")).isNull();
        assertThatThrownBy(() -> mutableFoo.removePool("quuz"))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("No pool with id 'quuz' exists in table '%s'", fooId);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeAllPools()
    {
        LootTweakerContext context = TestUtils.createContext();
        LootSystemInterface lootSystem = context.lootSystem();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        MutableLootTable mutableFoo = mutableLootTable(fooId, context);
        LootTable fooOriginal = loadTable(fooId);
        assertThat(LootTableAccessors.getPools(fooOriginal)).isNotEmpty();
        mutableFoo.removeAllPools();
        LootTable fooNew = mutableFoo.toImmutable();
        assertThat(LootTableAccessors.getPools(fooNew)).isEmpty();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getExistingPool()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        MutableLootTable mutableFoo = mutableLootTable(fooId, context);
        assertThat(mutableFoo.getPool("bar")).isNotNull();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getNonExistentPool()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        MutableLootTable mutableFoo = mutableLootTable(fooId, context);
        assertThatThrownBy(() -> mutableFoo.getPool("quuz"))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("No loot pool with id 'quuz' exists in table '%s'", fooId);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void nonExistentPoolIsNonNull()
    {
        LootTweakerContext context = new LootTweakerContext(new SuppressingErrorHandler());
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        MutableLootTable mutableFoo = mutableLootTable(fooId, context);
        assertThat(mutableFoo.getPool("quuz"))
            .withFailMessage("getPool() returned null for an invalid pool id. This will cause NPEs.")
            .isNotNull();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void poolCachingGetGet()
    {
        LootTweakerContext context = TestUtils.createContext();
        MutableLootTable mutableFoo = mutableLootTable(new ResourceLocation("loottweaker", "foo"), context);
        assertThat(mutableFoo.getPool("bar")).isNotNull();
        assertThat(mutableFoo.getPool("bar"))
            .withFailMessage("Different invocations of getPool() returned different objects for the pool named 'bar'")
            .isEqualTo(mutableFoo.getPool("bar"));

    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void poolCachingAddGet()
    {
        LootTweakerContext context = TestUtils.createContext();
        MutableLootTable mutableFoo = mutableLootTable(new ResourceLocation("loottweaker", "foo"), context);
        LootPoolRepresentation qux = mutableFoo.addPool("qux", 1, 1, 0, 0);
        assertThat(mutableFoo.getPool("qux"))
            .withFailMessage("Wrapper returned by addPool() for added pool 'qux' was not returned by subsequent invocation of getPool()")
            .isEqualTo(qux);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addPoolWithoutBonusRolls()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        MutableLootTable mutableFoo = mutableLootTable(fooId, context);
        LootTable fooOriginal = loadTable(fooId);
        assertThat(fooOriginal.getPool("qux")).isNull();
        mutableFoo.addPool("qux", 1, 2);
        LootTable fooNew = mutableFoo.toImmutable();
        LootPool qux = fooNew.getPool("qux");
        assertThat(qux).isNotNull();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(qux.getRolls()).extracting(RandomValueRange::getMin).isEqualTo(1.0F);
        softly.assertThat(qux.getRolls()).extracting(RandomValueRange::getMax).isEqualTo(2.0F);
        softly.assertThat(qux.getBonusRolls()).extracting(RandomValueRange::getMin).isEqualTo(0.0F);
        softly.assertThat(qux.getBonusRolls()).extracting(RandomValueRange::getMax).isEqualTo(0.0F);
        softly.assertAll();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addPoolWithBonusRolls()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        MutableLootTable mutableFoo = mutableLootTable(fooId, context);
        LootTable fooOriginal = loadTable(fooId);
        assertThat(fooOriginal.getPool("qux")).isNull();
        mutableFoo.addPool("qux", 1, 2, 3, 4);
        LootTable fooNew = mutableFoo.toImmutable();
        LootPool qux = fooNew.getPool("qux");
        assertThat(qux).isNotNull();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(qux.getRolls()).extracting(RandomValueRange::getMin).isEqualTo(1.0F);
        softly.assertThat(qux.getRolls()).extracting(RandomValueRange::getMax).isEqualTo(2.0F);
        softly.assertThat(qux.getBonusRolls()).extracting(RandomValueRange::getMin).isEqualTo(3.0F);
        softly.assertThat(qux.getBonusRolls()).extracting(RandomValueRange::getMax).isEqualTo(4.0F);
        softly.assertAll();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addPoolNameCollision()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        MutableLootTable mutableFoo = mutableLootTable(fooId, context);
        LootTable fooOriginal = loadTable(fooId);
        assertThat(fooOriginal.getPool("bar")).isNotNull();
        assertThatThrownBy(() -> mutableFoo.addPool("bar", 1, 2, 3, 4))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("Cannot add pool 'bar' to table '%s'. Pool names must be unique within their table.", fooId);
    }

    private MutableLootTable mutableLootTable(ResourceLocation tableId, LootTweakerContext context)
    {
        return new MutableLootTable(loadTable(tableId), tableId, context);
    }
}
