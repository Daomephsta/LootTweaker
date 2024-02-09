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
foo_bar.addEmptyEntry(2, 3, [Conditions.killedByPlayer()], "garply__empty");
//addEmptyEntry Helper
foo_bar.addEmptyEntry(2, 3, [Conditions.killedByPlayer()], "garply_empty");
//addEmptyEntry JSON
foo_bar.addEmptyEntry(2, 3, [{"condition": "minecraft:killed_by_player"}], "waldo_empty");