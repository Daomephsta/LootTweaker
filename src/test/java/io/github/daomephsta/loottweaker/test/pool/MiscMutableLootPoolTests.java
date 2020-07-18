package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static leviathan143.loottweaker.common.darkmagic.LootPoolAccessors.getEntries;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.daomephsta.loottweaker.test.TestUtils;
import io.github.daomephsta.loottweaker.test.ThrowingErrorHandler.LootTweakerException;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.impl.MutableLootPool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;


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
}
