package io.github.daomephsta.loottweaker.test;

import static io.github.daomephsta.loottweaker.test.TestUtils.loadTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.daomephsta.loottweaker.test.TestErrorHandler.LootTweakerException;
import io.github.daomephsta.loottweaker.test.dagger.DaggerTestLootTableTweakManagerProvider;
import io.github.daomephsta.loottweaker.test.dagger.TestLootTableTweakManagerProvider;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.LootTableTweakManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class LootTableTweakManagerTests
{
    private final TestLootTableTweakManagerProvider dagger = DaggerTestLootTableTweakManagerProvider.create();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getTableCheckRegistered()
    {
        ResourceLocation registeredTableId =
            ensureRegistered(new ResourceLocation("loottweaker", "registered_table"));
        assertThat(registeredTableId)
            .withFailMessage("Expected %s to be registered", registeredTableId)
            .isIn(LootTableList.getAll());
        LootTableTweakManager tableTweakManager = dagger.provide();
        tableTweakManager.getTable(registeredTableId.toString());
        tableTweakManager.tweakTable(registeredTableId, loadTable(registeredTableId));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getTableCheckUnregistered()
    {
        ResourceLocation unregisteredTableId = new ResourceLocation("loottweaker", "unregistered_table");
        assertThat(unregisteredTableId)
            .withFailMessage("Expected %s to be unregistered", unregisteredTableId)
            .isNotIn(LootTableList.getAll());
        LootTableTweakManager tableTweakManager = dagger.provide();
        tableTweakManager.getTable(unregisteredTableId.toString());
        assertThatThrownBy(() -> tableTweakManager.tweakTable(unregisteredTableId, loadTable(unregisteredTableId)))
            .isInstanceOf(LootTweakerException.class)
            .extracting(Throwable::getMessage)
            .isEqualTo("No loot table with name " + unregisteredTableId + " exists!");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getTableUncheckedRegistered()
    {
        ResourceLocation registeredTableId =
            ensureRegistered(new ResourceLocation("loottweaker", "registered_table"));
        assertThat(registeredTableId)
            .withFailMessage("Expected %s to be registered", registeredTableId)
            .isIn(LootTableList.getAll());
        LootTableTweakManager tableTweakManager = dagger.provide();
        tableTweakManager.getTableUnchecked(registeredTableId.toString());
        tableTweakManager.tweakTable(registeredTableId, loadTable(registeredTableId));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getTableUncheckedUnregistered()
    {
        ResourceLocation unregisteredTableId = new ResourceLocation("loottweaker", "unregistered_table");
        assertThat(unregisteredTableId)
            .withFailMessage("Expected %s to be unregistered", unregisteredTableId)
            .isNotIn(LootTableList.getAll());
        LootTableTweakManager tableTweakManager = dagger.provide();
        tableTweakManager.getTableUnchecked(unregisteredTableId.toString());
        tableTweakManager.tweakTable(unregisteredTableId, loadTable(unregisteredTableId));
    }

    private static ResourceLocation ensureRegistered(ResourceLocation id)
    {
        if (!LootTableList.getAll().contains(id))
            LootTableList.register(id);
        return id;
    }
}
