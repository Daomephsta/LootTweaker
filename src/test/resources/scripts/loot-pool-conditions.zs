import loottweaker.LootTweaker;
import loottweaker.LootTable;
import loottweaker.LootConditionFactory;

val conditions = lootTweakerApi.conditions;

lootTweakerApi.tweakLootTable("loottweaker:bar", function(bar)
{
    val baz = bar.getPool("baz");
    baz.addConditionsHelper([conditions.killedByPlayer()]);
    baz.removeAllConditions();
    baz.addConditionsJson([{"condition": "minecraft:killed_by_player"}]);
});