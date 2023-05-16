import loottweaker.LootTweaker;
import loottweaker.vanilla.loot.Conditions;

val foo = LootTweaker.getTable("loottweaker_test:foo");
val foo_bar = foo.getPool("bar");
foo_bar.addLootTableEntry("loottweaker_test:qux", 2, "corge_table");
//addLootTableEntryWithQuality
foo_bar.addLootTableEntry("loottweaker_test:qux", 2, 3, "grault_table");
//addLootTableEntryWithCondition
foo_bar.addLootTableEntry("loottweaker_test:qux", 2, 3, [Conditions.killedByPlayer()], "garply_helper_table");
//addLootTableEntry
foo_bar.addLootTableEntry("loottweaker_test:qux", 2, 3, [{"condition": "minecraft:killed_by_player"}], "waldo_json_table");
//addLootTableEntry Helper
foo_bar.addLootTableEntry("loottweaker_test:qux", 2, 3, [Conditions.killedByPlayer()], "apple_table");
//addLootTableEntry JSON
foo_bar.addLootTableEntry("loottweaker_test:qux", 2, 3, [{"condition": "minecraft:killed_by_player"}], "banana_table");