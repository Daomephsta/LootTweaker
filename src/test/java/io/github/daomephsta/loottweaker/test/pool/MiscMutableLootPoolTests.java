package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;
import static leviathan143.loottweaker.common.darkmagic.LootPoolAccessors.getEntries;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import com.google.common.collect.Sets;

import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.loottweaker.test.ThrowingErrorHandler.LootTweakerException;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.api.entry.LootEntryRepresentation;
import leviathan143.loottweaker.common.zenscript.api.iteration.LootIterator;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.impl.MutableLootPool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;


public class MiscMutableLootPoolTests
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeExistingEntry()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation barId = new ResourceLocation("loottweaker", "bar");
        LootTable bar = loadTable(barId);
        LootPool bazOriginal = bar.getPool("baz");

        assertThat(bazOriginal.getEntry("qux")).isNotNull();
        MutableLootPool mutableBaz = new MutableLootPool(bazOriginal, barId, context);
        mutableBaz.removeEntry("qux");
        LootPool bazNew = mutableBaz.toImmutable();
        assertThat(bazNew.getEntry("qux")).isNull();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeNonExistentEntry()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation barId = new ResourceLocation("loottweaker", "bar");
        LootTable bar = loadTable(barId);
        LootPool bazOriginal = bar.getPool("baz");

        assertThat(bazOriginal.getEntry("quuz")).isNull();
        MutableLootPool bazTweaks = new MutableLootPool(bazOriginal, barId, context);
        assertThatThrownBy(() -> bazTweaks.removeEntry("quuz"))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("No entry with id 'quuz' exists in pool 'baz' of table 'loottweaker:bar'");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeAllEntries()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation barId = new ResourceLocation("loottweaker", "bar");
        LootTable bar = loadTable(barId);
        LootPool bazOriginal = bar.getPool("baz");
        MutableLootPool mutableBaz = new MutableLootPool(bazOriginal, barId, context);
        assertThat(getEntries(bazOriginal)).isNotEmpty();
        mutableBaz.removeAllEntries();
        LootPool bazNew = mutableBaz.toImmutable();
        assertThat(getEntries(bazNew)).isEmpty();
    }


    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setRolls()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool barOriginal = foo.getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.setRolls(2.0F, 5.0F);

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew.getRolls())
            .extracting(RandomValueRange::getMin)
            .isEqualTo(2.0F);
        assertThat(barNew.getRolls())
            .extracting(RandomValueRange::getMax)
            .isEqualTo(5.0F);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setBonusRolls()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        LootPool barOriginal = foo.getPool("bar");
        MutableLootPool mutableBar = new MutableLootPool(barOriginal, fooId, context);
        mutableBar.setBonusRolls(1.0F, 3.0F);

        LootPool barNew = mutableBar.toImmutable();
        assertThat(barNew.getBonusRolls())
            .extracting(RandomValueRange::getMin)
            .isEqualTo(1.0F);
        assertThat(barNew.getBonusRolls())
            .extracting(RandomValueRange::getMax)
            .isEqualTo(3.0F);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void iterator()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation bazId = new ResourceLocation("loottweaker", "baz");
        LootTable baz = loadTable(bazId);
        LootPool fooOriginal = baz.getPool("foo");
        MutableLootPool mutableFoo = new MutableLootPool(fooOriginal, bazId, context);
        Set<String> expectedNames = Sets.newHashSet("qux", "quuz", "corge");
        Set<String> unexpectedNames = Sets.newHashSet();
        for (LootEntryRepresentation pool : mutableFoo.entriesIterator())
        {
            if (!expectedNames.remove(pool.getName()))
                unexpectedNames.add(pool.getName());
        }
        assertThat(expectedNames.isEmpty() && unexpectedNames.isEmpty())
            .describedAs("Iteration test failed. Unexpected: %s Missing: %s",
                unexpectedNames, expectedNames)
            .isTrue();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void iteratorRemove()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation bazId = new ResourceLocation("loottweaker", "baz");
        LootTable baz = loadTable(bazId);
        LootPool fooOriginal = baz.getPool("foo");
        MutableLootPool mutableFoo = new MutableLootPool(fooOriginal, bazId, context);
        assertThat(fooOriginal).hasEntries("qux", "quuz", "corge");
        LootIterator<?, LootEntryRepresentation> entriesIterator = mutableFoo.entriesIterator();
        for (LootEntryRepresentation entry : entriesIterator)
        {
            if (entry.getName().startsWith("q"))
                entriesIterator.remove();
        }
        LootPool bazNew = mutableFoo.toImmutable();
        assertThat(bazNew)
            .hasEntry("corge")
            .doesNotHaveEntries("qux", "quuz");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void iteratorRemoveCME()
    {
        LootTweakerContext context = TestUtils.createContext();
        ResourceLocation bazId = new ResourceLocation("loottweaker", "baz");
        LootTable baz = loadTable(bazId);
        LootPool fooOriginal = baz.getPool("foo");
        MutableLootPool mutableFoo = new MutableLootPool(fooOriginal, bazId, context);
        assertThat(fooOriginal).hasEntries("qux", "quuz", "corge");
        assertThatThrownBy(() ->
        {
            for (LootEntryRepresentation entry : mutableFoo.entriesIterator())
            {
                if (entry.getName().startsWith("q"))
                    mutableFoo.removeEntry(entry.getName());
            }
        })
        .isInstanceOf(LootTweakerException.class)
        .hasMessageStartingWith("entry 'quuz' of pool 'foo' of table 'loottweaker:baz' unsafely removed");
    }
}
