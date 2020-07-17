import loottweaker.LootTweaker;
import loottweaker.LootTable;

lootTweakerApi.tweakLootTable("loottweaker:foo", function(foo)
{
    val bar = foo.getPool("bar");
    foo.removePool("bar");
    foo.removeAllPools();
});