package io.github.daomephsta.loottweaker.test;

import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.daomephsta.loottweaker.test.TestErrorHandler.LootTweakerException;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.LootTableTweakManager;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;

public class LootTableTweakManagerTests
{
    private final LootTweakerContext context = TestUtils.context();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getTableCheckExisting()
    {
        ResourceLocation existingTableId = new ResourceLocation("loottweaker", "bar");
        LootTableTweakManager tableTweakManager = context.createLootTableTweakManager();
        tableTweakManager.getTable(existingTableId.toString());
        tableTweakManager.tweakTable(existingTableId, loadTable(existingTableId));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getTableCheckNonExistent()
    {
        ResourceLocation nonExistentTableId = new ResourceLocation("loottweaker", "non_existent_table");
        LootTableTweakManager tableTweakManager = context.createLootTableTweakManager();
        tableTweakManager.getTable(nonExistentTableId.toString());
        assertThatThrownBy(() -> tableTweakManager.tweakTable(nonExistentTableId, LootTable.EMPTY_LOOT_TABLE))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("No loot table with name %s exists!", nonExistentTableId);
    }
}
