import loottweaker.LootTweaker;
import loottweaker.vanilla.loot.Conditions;
import loottweaker.vanilla.loot.Functions;

val foo = LootTweaker.getTable("loottweaker_test:foo");
val foo_bar = foo.getPool("bar");
//addEmptyEntry
foo_bar.addEmptyEntry(2, "corge_empty");
//addEmptyEntryWithQuality
foo_bar.addEmptyEntry(2, 3, "grault_empty");
//addEmptyEntryWithCondition
<<<<<<< HEAD
foo_bar.addEmptyEntry(2, 3, [Conditions.killedByPlayer()], "garply__empty");
=======
foo_bar.addEmptyEntry(2, 3, [Conditions.killedByPlayer()], "garply_helper_empty");
//addEmptyEntry
foo_bar.addEmptyEntry(2, 3, [{"condition": "minecraft:killed_by_player"}], "waldo_json_empty");
>>>>>>> 57bf677 (Replace MethodHandles with mixins in src/test/java)
//addEmptyEntry Helper
foo_bar.addEmptyEntry(2, 3, [Conditions.killedByPlayer()], "garply_empty");
//addEmptyEntry JSON
foo_bar.addEmptyEntry(2, 3, [{"condition": "minecraft:killed_by_player"}], "waldo_empty");