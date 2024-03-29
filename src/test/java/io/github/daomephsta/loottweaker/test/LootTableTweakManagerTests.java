package io.github.daomephsta.loottweaker.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.daomephsta.loottweaker.test.TestErrorHandler.LootTweakerException;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import leviathan143.loottweaker.common.zenscript.LootTableTweakManager;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import net.minecraft.util.ResourceLocation;


public class LootTableTweakManagerTests
{
    private final LootTweakerContext context = TestUtils.context();

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getTableCheckExisting()
    {
        ResourceLocation existingTableId = new ResourceLocation("loottweaker_test", "bar");
        LootTableTweakManager tableTweakManager = context.createLootTableTweakManager();
        tableTweakManager.getTable(existingTableId.toString());
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getTableCheckNonExistent()
    {
        ResourceLocation nonExistentTableId = new ResourceLocation("loottweaker_test", "non_existent_table");
        LootTableTweakManager tableTweakManager = context.createLootTableTweakManager();
        assertThatThrownBy(() -> tableTweakManager.getTable(nonExistentTableId.toString()))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("No loot table with name %s exists!", nonExistentTableId);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void tableWrapperCaching()
    {
        LootTableTweakManager tweakManager = context.createLootTableTweakManager();
        assertThat(tweakManager.getTable("loottweaker_test:foo")).isNotNull();
        assertThat(tweakManager.getTable("loottweaker_test:foo")).isEqualTo(tweakManager.getTable("loottweaker_test:foo"));
    }

    public void newTable()
    {
        LootTableTweakManager tableTweakManager = context.createLootTableTweakManager();
        assertThat(tableTweakManager.newTable("loottweaker_test:qux")).isNotNull();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void newTableCollision()
    {
        String existingTableId = "loottweaker_test:foo";
        LootTableTweakManager tableTweakManager = context.createLootTableTweakManager();
        assertThatThrownBy(() -> tableTweakManager.newTable(existingTableId))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("Table id '%s' already in use", existingTableId);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void newTableGettable()
    {
        LootTableTweakManager tableTweakManager = context.createLootTableTweakManager();
        String tableName = "loottweaker_test:qux";
        tableTweakManager.newTable(tableName);
        tableTweakManager.getTable(tableName);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void newTableWarnMinecraftNamespace()
    {
        LootTableTweakManager tableTweakManager = context.createLootTableTweakManager();
        String implicitMinecraftNamespace = "qux";
        String explicitMinecraftNamespace = "minecraft:quuz";
        assertThatThrownBy(() -> tableTweakManager.newTable(implicitMinecraftNamespace))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("Table id '%s' implicitly uses the minecraft namespace, this is discouraged",
                implicitMinecraftNamespace);
        assertThatThrownBy(() -> tableTweakManager.newTable(explicitMinecraftNamespace))
            .isInstanceOf(LootTweakerException.class)
            .hasMessage("Table id '%s' explicitly uses the minecraft namespace, this is discouraged",
                explicitMinecraftNamespace);
    }
}
