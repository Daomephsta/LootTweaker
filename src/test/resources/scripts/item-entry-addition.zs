val conditions = lootTweakerApi.conditions;
val functions = lootTweakerApi.functions;

lootTweakerApi.tweakLootTable("loottweaker:foo", function(foo)
{
    val foo_bar = foo.getPool("bar");
    //addItemEntry
    foo_bar.addItemEntry(<minecraft:apple>, "gru");
    //addItemEntryWithWeight
    foo_bar.addItemEntry(<minecraft:apple>, 2, "qux");
    //addItemEntryWithQuality
    foo_bar.addItemEntry(<minecraft:apple>, 2, 3, "quuz");
    //addItemEntryWithCondition
    foo_bar.addItemEntry(<minecraft:baked_potato>, 2, 3, [], [conditions.killedByPlayer()], "corge");
    //addItemEntry JSON
    foo_bar.addItemEntry(<minecraft:baked_potato>, 2, 3, 
        [], [{"condition": "minecraft:killed_by_player"}], "grault");
    //addItemEntryWithImplicitSetCount
    foo_bar.addItemEntry(<minecraft:arrow> * 3, 2, "garply");
    //addItemEntryWithExplicitSetCount
    foo_bar.addItemEntry(<minecraft:arrow>, 2, 1, [functions.setCount(3, 3)], [], "waldo");
    //addItemEntryWithImplicitSetDamage
    foo_bar.addItemEntry(<minecraft:bow:32>, 2, "fred");
    //addItemEntryWithExplicitSetDamage
    foo_bar.addItemEntry(<minecraft:bow>, 2, 1, [functions.setDamage(0.5, 0.5)], [], "plugh");
    //addItemEntryWithImplicitSetMetadata
    foo_bar.addItemEntry(<minecraft:dye:8>, 2, "thud");
    //addItemEntryWithExplicitSetMetadata
    foo_bar.addItemEntry(<minecraft:dye>, 2, 1, [functions.setMetadata(8, 8)], [], "grox");
    //addItemEntryWithImplicitSetNBT
    foo_bar.addItemEntry(<minecraft:bread>.withDisplayName("Super Bread"), 2, "warg");
    //addItemEntryWithExplicitSetNBT
    foo_bar.addItemEntry(<minecraft:bread>, 2, 1,
        [functions.setNBT({"display": {"Name": "Super Bread"}})], [], "nerf");
});