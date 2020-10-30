import loottweaker.LootTweaker;
import loottweaker.LootTable;

lootTweakerApi.tweakLootTable("loottweaker:bar", function(bar)
{
    val baz = bar.getPool("baz");
    baz.removeEntry("qux");
    baz.removeAllEntries();
    baz.setRolls(2, 3);
    baz.setBonusRolls(1, 2);
});

lootTweakerApi.tweakLootTable("loottweaker:baz", function(baz)
{
    val foo = baz.getPool("foo");
    for entry in foo
    {
        entry.remove();
    }
});