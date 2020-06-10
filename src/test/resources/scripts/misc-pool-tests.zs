import loottweaker.LootTweaker;
import loottweaker.vanilla.loot.Conditions;

val foo = LootTweaker.getTable("loottweaker:foo");
val foo_bar = foo.getPool("bar");
val bar = LootTweaker.getTable("loottweaker:bar");
val bar_baz = bar.getPool("baz");
//addConditions
foo_bar.addConditionsHelper([Conditions.killedByPlayer()]);
//removeExistingEntry
bar_baz.removeEntry("qux");
//clearConditions
bar_baz.clearConditions();
//clearEntries
bar_baz.clearEntries();
//setRolls
foo_bar.setRolls(2.0, 5.0);
//setBonusRolls
foo_bar.setBonusRolls(1.0, 3.0);