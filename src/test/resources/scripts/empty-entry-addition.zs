import loottweaker.LootConditionFactory;

lootTweakerApi.tweakLootTable("loottweaker:foo", function(foo)
{
    val foo_bar = foo.getPool("bar");
    //addEmptyEntry
    foo_bar.addEmptyEntry("gru_empty");
    //addEmptyEntryWithWeight
    foo_bar.addEmptyEntry(2, "corge_empty");
    //addEmptyEntryWithQuality
    foo_bar.addEmptyEntry(2, 3, "grault_empty");
    //addEmptyEntryHelper
    foo_bar.addEmptyEntryHelper(2, 3, [lootTweakerApi.conditions.killedByPlayer()], "garply_empty");
    //addEmptyEntryJson
    foo_bar.addEmptyEntryJson(2, 3, [{"condition": "minecraft:killed_by_player"}], "waldo_empty");
});