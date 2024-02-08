package io.github.daomephsta.loottweaker.test;

import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.daomephsta.loottweaker.test.TestErrorHandler.LootTweakerException;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.LootTableTweakManager;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootPoolWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;


public class ZenLootTableWrapperTests
{
    private final LootTweakerContext context = TestUtils.context();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getExistingPool()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        LootTable fooOriginal = loadTable(fooId);
        assertThat(fooOriginal.getPool("bar")).isNotNull();
        fooTweaks.getPool("bar");
        tweakManager.tweakTable(fooId, fooOriginal);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getNonExistentPool()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        LootTable fooOriginal = loadTable(fooId);
        fooTweaks.getPool("quuz");
        assertThatThrownBy(() -> tweakManager.tweakTable(fooId, fooOriginal))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("No loot pool with name quuz exists in table %s!", fooId);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeExistingPool()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        LootTable fooOriginal = loadTable(fooId);
        assertThat(fooOriginal.getPool("bar")).isNotNull();
        fooTweaks.removePool("bar");
        LootTable fooNew = tweakManager.tweakTable(fooId, fooOriginal);
        assertThat(fooNew.getPool("bar")).isNull();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeNonExistentPool()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        LootTable fooOriginal = loadTable(fooId);
        assertThat(fooOriginal.getPool("quuz")).isNull();
        fooTweaks.removePool("quuz");
        assertThatThrownBy(() -> tweakManager.tweakTable(fooId, fooOriginal))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("No loot pool with name quuz exists in table %s!", fooId);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addPool()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker_test", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        LootTable fooOriginal = loadTable(fooId);
        assertThat(fooOriginal.getPool("qux")).isNull();
        fooTweaks.addPool("qux", 1, 2, 3, 4);
        LootTable fooNew = tweakManager.tweakTable(fooId, fooOriginal);
        LootPool qux = fooNew.getPool("qux");
        assertThat(qux).isNotNull();
        assertThat(qux.getRolls()).extracting(RandomValueRange::getMin).isEqualTo(1.0F);
        assertThat(qux.getRolls()).extracting(RandomValueRange::getMax).isEqualTo(2.0F);
        assertThat(qux.getBonusRolls()).extracting(RandomValueRange::getMin).isEqualTo(3.0F);
        assertThat(qux.getBonusRolls()).extracting(RandomValueRange::getMax).isEqualTo(4.0F);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void poolWrapperCaching()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ZenLootTableWrapper fooTweaks = tweakManager.getTable("loottweaker_test:foo");
        assertThat(fooTweaks.getPool("bar")).isNotNull();
        assertThat(fooTweaks.getPool("bar"))
            .withFailMessage(
                "Different invocations of getPool() returned different objects for the pool named 'bar'")
            .isEqualTo(fooTweaks.getPool("bar"));
        ZenLootPoolWrapper qux = fooTweaks.addPool("qux", 1, 1, 0, 0);
        assertThat(fooTweaks.getPool("qux")).withFailMessage(
            "Wrapper returned by addPool() for added pool 'qux' was not returned by subsequent invocation of getPool()")
            .isEqualTo(qux);
    }
}
