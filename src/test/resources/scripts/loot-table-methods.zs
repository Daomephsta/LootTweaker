import loottweaker.LootTweaker;
import loottweaker.LootTable;

lootTweakerApi.tweakLootTable("loottweaker:foo", function(foo)
{
    foo.removePool("bar");
    foo.removeAllPools();
});