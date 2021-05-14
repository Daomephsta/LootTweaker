import loottweaker.LootTweaker;
import loottweaker.LootTable;
import loottweaker.LootConditionFactory;

val conditions = lootTweakerApi.conditions;

lootTweakerApi.tweakLootTable("loottweaker:bar", function(bar)
{
    val baz = bar.getPool("baz");
    baz.addConditions([conditions.killedByPlayer()]);
    baz.removeAllConditions();
    baz.addConditions([{"condition": "minecraft:killed_by_player"}]);
});