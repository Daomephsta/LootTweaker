package io.github.daomephsta.loottweaker.test;

import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.daomephsta.loottweaker.test.TestErrorHandler.LootTweakerException;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;

public class ZenLootTableWrapperTests
{
    private final LootTweakerContext context = TestUtils.context();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getExistingPool()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        ZenLootTableWrapper fooTweaks = context.wrapLootTable(fooId);
        assertThat(foo.getPool("bar")).isNotNull();
        fooTweaks.getPool("bar");
        fooTweaks.applyTweakers(foo);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getNonExistentPool()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        ZenLootTableWrapper fooTweaks = context.wrapLootTable(fooId);
        fooTweaks.getPool("quuz");
        assertThatThrownBy(() -> fooTweaks.applyTweakers(foo))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("No loot pool with name quuz exists in table %s!", fooId);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeExistingPool()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        ZenLootTableWrapper fooTweaks = context.wrapLootTable(fooId);
        assertThat(foo.getPool("bar")).isNotNull();
        fooTweaks.removePool("bar");
        fooTweaks.applyTweakers(foo);
        assertThat(foo.getPool("bar")).isNull();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeNonExistentPool()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        ZenLootTableWrapper fooTweaks = context.wrapLootTable(fooId);
        assertThat(foo.getPool("quuz")).isNull();
        fooTweaks.removePool("quuz");
        assertThatThrownBy(() -> fooTweaks.applyTweakers(foo))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("No loot pool with name quuz exists in table %s!", fooId);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addPool()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        ZenLootTableWrapper fooTweaks = context.wrapLootTable(fooId);
        assertThat(foo.getPool("qux")).isNull();
        fooTweaks.addPool("qux", 1, 2, 3, 4);
        fooTweaks.applyTweakers(foo);
        assertThat(foo.getPool("qux")).isNotNull();
        assertThat(foo.getPool("qux").getRolls()).extracting(RandomValueRange::getMin).isEqualTo(1.0F);
        assertThat(foo.getPool("qux").getRolls()).extracting(RandomValueRange::getMax).isEqualTo(2.0F);
        assertThat(foo.getPool("qux").getBonusRolls()).extracting(RandomValueRange::getMin).isEqualTo(3.0F);
        assertThat(foo.getPool("qux").getBonusRolls()).extracting(RandomValueRange::getMax).isEqualTo(4.0F);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void poolWrapperCaching()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        ZenLootTableWrapper fooTweaks = context.wrapLootTable(fooId);
        assertThat(fooTweaks.getPool("bar")).isNotNull();
        assertThat(fooTweaks.getPool("bar")).isEqualTo(fooTweaks.getPool("bar"));
    }
}
