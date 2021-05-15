import loottweaker.LootTweaker;
import loottweaker.vanilla.loot.Conditions;

val foo = LootTweaker.getTable("loottweaker:foo");
val foo_bar = foo.getPool("bar");
foo_bar.addLootTableEntry("loottweaker:qux", 2, "corge_table");
//addLootTableEntryWithQuality
foo_bar.addLootTableEntry("loottweaker:qux", 2, 3, "grault_table");
//addLootTableEntryWithCondition
foo_bar.addLootTableEntryHelper("loottweaker:qux", 2, 3, [Conditions.killedByPlayer()], "garply_helper_table");
//addLootTableEntryJson
foo_bar.addLootTableEntryJson("loottweaker:qux", 2, 3, [{"condition": "minecraft:killed_by_player"}], "waldo_json_table");
//addLootTableEntry Helper
foo_bar.addLootTableEntry("loottweaker:qux", 2, 3, [Conditions.killedByPlayer()], "apple_table");
//addLootTableEntry JSON
foo_bar.addLootTableEntry("loottweaker:qux", 2, 3, [{"condition": "minecraft:killed_by_player"}], "banana_table");