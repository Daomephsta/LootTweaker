package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestLootConditionAccessors.isInverted;
import static io.github.daomephsta.loottweaker.test.TestUtils.iitemstack;
import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;
import static leviathan143.loottweaker.common.darkmagic.LootPoolAccessors.getConditions;
import static leviathan143.loottweaker.common.darkmagic.LootPoolAccessors.getEntries;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.daomephsta.loottweaker.test.TestErrorHandler.LootTweakerException;
import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.LootTableTweakManager;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.factory.LootConditionFactory;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootConditionWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootEntryWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootPoolWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;

public class MiscZenLootPoolWrapperTests
{
    private final LootTweakerContext context = TestUtils.context();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addConditions()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.addConditionsHelper(new ZenLootConditionWrapper[] {LootConditionFactory.killedByPlayer()});

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        assertThat(foo.getPool("bar")).hasMatchingCondition(condition ->
            condition instanceof KilledByPlayer && !isInverted((KilledByPlayer) condition),
        "KilledByPlayer()");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getExistingEntry()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation barId = new ResourceLocation("loottweaker", "bar");
        ZenLootTableWrapper barTweaks = tweakManager.getTable(barId.toString());
        LootTable barOriginal = loadTable(barId);
        assertThat(barOriginal.getPool("baz").getEntry("qux")).isNotNull();
        ZenLootPoolWrapper bazTweaks = barTweaks.getPool("baz");
        assertThat(bazTweaks.getEntry("qux")).isNotNull();
        tweakManager.tweakTable(barId, barOriginal);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getNonExistentEntry()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation barId = new ResourceLocation("loottweaker", "bar");
        ZenLootTableWrapper barTweaks = tweakManager.getTable(barId.toString());
        LootTable barOriginal = loadTable(barId);
        assertThat(barOriginal.getPool("baz").getEntry("quuz")).isNull();
        ZenLootPoolWrapper bazTweaks = barTweaks.getPool("baz");
        bazTweaks.getEntry("quuz");
        assertThatThrownBy(() -> tweakManager.tweakTable(barId, barOriginal))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("No entry with name quuz exists in pool 'baz' of table 'loottweaker:bar'");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeExistingEntry()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation barId = new ResourceLocation("loottweaker", "bar");
        ZenLootTableWrapper barTweaks = tweakManager.getTable(barId.toString());
        LootTable barOriginal = loadTable(barId);
        assertThat(barOriginal.getPool("baz").getEntry("qux")).isNotNull();
        ZenLootPoolWrapper bazTweaks = barTweaks.getPool("baz");
        bazTweaks.removeEntry("qux");
        LootTable barNew = tweakManager.tweakTable(barId, barOriginal);
        assertThat(barNew.getPool("baz").getEntry("qux")).isNull();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeNonExistentEntry()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation barId = new ResourceLocation("loottweaker", "bar");
        ZenLootTableWrapper barTweaks = tweakManager.getTable(barId.toString());
        LootTable barOriginal = loadTable(barId);
        assertThat(barOriginal.getPool("baz").getEntry("quuz")).isNull();
        ZenLootPoolWrapper bazTweaks = barTweaks.getPool("baz");
        bazTweaks.removeEntry("quuz");
        assertThatThrownBy(() -> tweakManager.tweakTable(barId, barOriginal))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("No entry with name 'quuz' exists in pool 'baz' of table 'loottweaker:bar'");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void clearConditions()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation barId = new ResourceLocation("loottweaker", "bar");
        ZenLootTableWrapper barTweaks = tweakManager.getTable(barId.toString());
        LootTable barOriginal = loadTable(barId);
        assertThat(getConditions(barOriginal.getPool("baz"))).isNotEmpty();
        ZenLootPoolWrapper bazTweaks = barTweaks.getPool("baz");
        bazTweaks.clearConditions();
        LootTable barNew = tweakManager.tweakTable(barId, barOriginal);
        assertThat(getConditions(barNew.getPool("baz"))).isEmpty();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void clearEntries()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation barId = new ResourceLocation("loottweaker", "bar");
        ZenLootTableWrapper barTweaks = tweakManager.getTable(barId.toString());
        LootTable barOriginal = loadTable(barId);
        assertThat(getEntries(barOriginal.getPool("baz"))).isNotEmpty();
        ZenLootPoolWrapper bazTweaks = barTweaks.getPool("baz");
        bazTweaks.clearEntries();
        LootTable barNew = tweakManager.tweakTable(barId, barOriginal);
        assertThat(getEntries(barNew.getPool("baz"))).isEmpty();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setRolls()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.setRolls(2.0F, 5.0F);

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        LootPool bar = foo.getPool("bar");
        assertThat(bar.getRolls()).extracting(RandomValueRange::getMin).isEqualTo(2.0F);
        assertThat(bar.getRolls()).extracting(RandomValueRange::getMax).isEqualTo(5.0F);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setBonusRolls()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        ZenLootTableWrapper fooTweaks = tweakManager.getTable(fooId.toString());
        ZenLootPoolWrapper barTweaks = fooTweaks.getPool("bar");
        barTweaks.setBonusRolls(1.0F, 3.0F);

        LootTable foo = tweakManager.tweakTable(fooId, loadTable(fooId));
        LootPool bar = foo.getPool("bar");
        assertThat(bar.getBonusRolls()).extracting(RandomValueRange::getMin).isEqualTo(1.0F);
        assertThat(bar.getBonusRolls()).extracting(RandomValueRange::getMax).isEqualTo(3.0F);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void entryWrapperCaching()
    {
        ZenLootPoolWrapper bazTweaks = context.wrapPool(new ResourceLocation("loottweaker", "bar"), "baz");
        assertThat(bazTweaks.getEntry("qux")).isNotNull();
        assertThat(bazTweaks.getEntry("qux"))
            .withFailMessage("Different invocations of getEntry() returned different objects for the entry named 'qux'")
            .isEqualTo(bazTweaks.getEntry("qux"));

        ZenLootEntryWrapper quuz = bazTweaks.addEmptyEntry(5, "quuz");
        assertThat(bazTweaks.getEntry("quuz"))
            .withFailMessage("Wrapper returned by addEmptyEntry() for added entry 'quuz' was not returned by subsequent invocation of getEntry()")
            .isEqualTo(quuz);

        ZenLootEntryWrapper corge = bazTweaks.addItemEntry(iitemstack(Items.COAL), 5, "corge");
        assertThat(bazTweaks.getEntry("corge"))
            .withFailMessage("Wrapper returned by addEmptyEntry() for added entry 'corge' was not returned by subsequent invocation of getEntry()")
            .isEqualTo(corge);

        ZenLootEntryWrapper garply = bazTweaks.addLootTableEntry("loottweaker:foo", 5, "garply");
        assertThat(bazTweaks.getEntry("garply"))
            .withFailMessage("Wrapper returned by addEmptyEntry() for added entry 'garply' was not returned by subsequent invocation of getEntry()")
            .isEqualTo(garply);
    }
}
