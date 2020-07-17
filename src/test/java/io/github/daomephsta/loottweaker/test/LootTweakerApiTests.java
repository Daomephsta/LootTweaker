package io.github.daomephsta.loottweaker.test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import crafttweaker.util.IEventHandler;
import io.github.daomephsta.loottweaker.test.ThrowingErrorHandler.LootTweakerException;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.api.LootSystemInterface;
import leviathan143.loottweaker.common.zenscript.api.LootTableLoadCraftTweakerEvent;
import leviathan143.loottweaker.common.zenscript.api.LootSystemInterface.LootTableConsumer;
import leviathan143.loottweaker.common.zenscript.api.LootTableRepresentation;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerApi;
import net.minecraft.util.ResourceLocation;

public class LootTweakerApiTests
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void tweakExistingTable()
    {
        LootSystemInterface api = new LootTweakerApi(TestUtils.createContext());
        ResourceLocation existingTableId = new ResourceLocation("loottweaker", "bar");
        api.tweakLootTable(existingTableId.toString(), table -> {});
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void tweakNonExistentTable()
    {
        LootSystemInterface api = new LootTweakerApi(TestUtils.createContext());
        ResourceLocation nonExistentTableId = new ResourceLocation("loottweaker", "non_existent_table");
        assertThatThrownBy(() -> api.tweakLootTable(nonExistentTableId.toString(), table -> {}))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("No loot table with id %s exists!", nonExistentTableId);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void onTableLoad()
    {
        LootTweakerApi api = new LootTweakerApi(TestUtils.createContext());
        ResourceLocation existingTableId = new ResourceLocation("loottweaker", "bar");
        ExecutionChecker executionChecker = new ExecutionChecker();
        api.onLootTableLoad(executionChecker);
        api.processLootTable(existingTableId, TestUtils.loadTable(existingTableId));
        assertThat(executionChecker.executed)
            .as("Test handler was never called")
            .isTrue();
    }

    private static class ExecutionChecker implements LootTableConsumer, IEventHandler<LootTableLoadCraftTweakerEvent>
    {
        boolean executed = false;

        @Override
        public void handle(LootTableLoadCraftTweakerEvent event)
        {
            apply(event.getTable());
        }

        @Override
        public void apply(LootTableRepresentation lootTable)
        {
            executed = true;
        }
    }
}
