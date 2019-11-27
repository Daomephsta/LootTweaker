package io.github.daomephsta.loottweaker.test.pool;

import static io.github.daomephsta.loottweaker.test.TestUtils.iitemstack;
import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static io.github.daomephsta.loottweaker.test.assertion.LootTweakerAssertions.assertThat;

import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootPoolWrapper;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;

public class EntryNameGeneratorTests
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void identicalItems()
    {
        ResourceLocation fooId = new ResourceLocation("loottweaker", "foo");
        LootTable foo = loadTable(fooId);
        ZenLootPoolWrapper barTweaks = new ZenLootPoolWrapper("bar", fooId);
        barTweaks.addItemEntry(iitemstack(Items.DYE, 2), 5, null);
        barTweaks.addItemEntry(iitemstack(Items.DYE, 1), 2, null);
        barTweaks.tweak(foo);

        assertThat(foo.getPool("bar"))
            .hasEntry("minecraft:dye")
            .hasEntry("minecraft:dye-loottweaker#2");
    }
}
