lootTweakerApi.createLootTable("foo:test/bar", function(bar)
{
    val main = bar.addPool("main", 2, 2);
    main.addItemEntry(<minecraft:stick>, 10);
});