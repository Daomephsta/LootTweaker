import loottweaker.LootTweaker;
import loottweaker.vanilla.loot.Conditions;

Conditions.randomChance(0.21);
Conditions.randomChanceWithLooting(0.35, 1.2);
Conditions.killedByPlayer();
Conditions.killedByNonPlayer();
Conditions.parse({"condition": "minecraft:killed_by_player"});


val foo = LootTweaker.getTable("loottweaker_test:foo");
val foo_bar = foo.getPool("bar");
foo_bar.addItemEntry(<minecraft:stick>, 1, 0, 
[], 
[
	Conditions.zenscript(function(random, context)
	{
		return random.nextBoolean();
	})
]);