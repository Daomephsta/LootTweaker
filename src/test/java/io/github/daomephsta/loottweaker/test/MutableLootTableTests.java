package io.github.daomephsta.loottweaker.test;

import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.daomephsta.loottweaker.test.TestErrorHandler.LootTweakerException;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.api.LootSystemInterface;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import leviathan143.loottweaker.common.zenscript.impl.MutableLootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;


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

    private MutableLootTable mutableLootTable(ResourceLocation tableId, LootTweakerContext context)
    {
        return new MutableLootTable(loadTable(tableId), tableId, context);
    }
}
