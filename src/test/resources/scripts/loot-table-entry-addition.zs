import loottweaker.LootConditionFactory;
import loottweaker.LootCondition;
import crafttweaker.data.IData;

val conditions = lootTweakerApi.conditions;

lootTweakerApi.tweakLootTable("loottweaker:foo", function(foo)
{
    val foo_bar = foo.getPool("bar");
    //addLootTableEntry
    foo_bar.addLootTableEntry("loottweaker:qux", "gru_table");
    //addLootTableEntryWithWeight
    foo_bar.addLootTableEntry("loottweaker:qux", 2, "corge_table");
    //addLootTableEntryWithQuality
    foo_bar.addLootTableEntry("loottweaker:qux", 2, 3, "grault_table");
    //addLootTableEntry Helper
    foo_bar.addLootTableEntry("loottweaker:qux", 2, 3, [conditions.killedByPlayer()], "garply_table");
    //addLootTableEntry JSON
    foo_bar.addLootTableEntry("loottweaker:qux", 2, 3, [{"condition": "minecraft:killed_by_player"}], "waldo_table");
});