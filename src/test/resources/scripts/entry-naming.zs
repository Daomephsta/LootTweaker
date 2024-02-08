import loottweaker.LootTweaker;
import loottweaker.vanilla.loot.Conditions;
import loottweaker.vanilla.loot.Functions;

val foo = LootTweaker.getTable("loottweaker_test:foo");
val foo_bar = foo.getPool("bar");
//identicalItems
foo_bar.addItemEntry(<minecraft:dye> * 2, 5, null);
foo_bar.addItemEntry(<minecraft:dye>, 2, null);
//customNamedItemEntry
foo_bar.addItemEntry(<minecraft:dye> * 2, 5, "corge_naming");
foo_bar.addItemEntry(<minecraft:dye>, 2, null);
//identicalTableReferences
foo_bar.addLootTableEntry("loottweaker_test:bar", 5, null);
foo_bar.addLootTableEntry("loottweaker_test:bar", 2, null);
//customNamedTableReference
foo_bar.addLootTableEntry("loottweaker_test:bar", 5, "grault_naming");
foo_bar.addLootTableEntry("loottweaker_test:bar", 2, null);
//multipleEmpties
foo_bar.addEmptyEntry(5, null);
foo_bar.addEmptyEntry(2, null);
//customNamedEmpty
foo_bar.addEmptyEntry(5, "garply_naming");
foo_bar.addEmptyEntry(2, null);