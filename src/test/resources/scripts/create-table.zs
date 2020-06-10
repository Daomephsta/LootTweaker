import loottweaker.LootTweaker;

val fooBar = LootTweaker.newTable("foo:test/bar");
val fooBarMain = fooBar.addPool("main", 2, 2, 0, 0);
fooBarMain.addItemEntry(<minecraft:stick>, 10);