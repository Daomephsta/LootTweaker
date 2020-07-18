lootTweakerApi.tweakLootTable("loottweaker:foo", function(foo)
{
    val foo_bar = foo.getPool("bar");
    //addLootTableEntry
    foo_bar.addLootTableEntry("loottweaker:qux", "gru_table");
    //addLootTableEntryWithWeight
    foo_bar.addLootTableEntry("loottweaker:qux", 2, "corge_table");
    //addLootTableEntryWithQuality
    foo_bar.addLootTableEntry("loottweaker:qux", 2, 3, "grault_table");
    //addLootTableEntryWithCondition
    //foo_bar.addLootTableEntryHelper("loottweaker:qux", 2, 3, [Conditions.killedByPlayer()], "garply_table");
    //addLootTableEntryJson
    foo_bar.addLootTableEntryJson("loottweaker:qux", 2, 3, [{"condition": "minecraft:killed_by_player"}], "waldo_table");
});