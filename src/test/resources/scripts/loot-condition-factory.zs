import loottweaker.LootTweaker;
import loottweaker.Conditions;
import crafttweaker.util.IRandom;
import loottweaker.LootContext;


Conditions.randomChance(0.21);
Conditions.randomChanceWithLooting(0.35, 1.2);
Conditions.killedByPlayer();
Conditions.killedByNonPlayer();


val foo = LootTweaker.getTable("loottweaker_test:foo");
val foo_bar = foo.getPool("bar");
foo_bar.addItemEntry(<minecraft:stick>, 1, 0, 
[], 
[
	Conditions.zenscript(function(random as IRandom, context as LootContext) as bool
	{
		return random.nextBoolean();
	})
]);