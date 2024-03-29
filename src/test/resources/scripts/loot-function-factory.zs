import loottweaker.LootTweaker;
import loottweaker.vanilla.loot.Conditions;
import loottweaker.vanilla.loot.Functions;

Functions.enchantRandomly(["minecraft:thorns"]);
Functions.enchantWithLevels(11, 26, false);
Functions.lootingEnchantBonus(1, 2, 3);
Functions.setCount(1, 3);
Functions.setDamage(0.2, 0.8);
Functions.setMetadata(23, 45);
Functions.setNBT({"foo": "bar"});
Functions.smelt();
Functions.parse({"function": "minecraft:furnace_smelt"});
Functions.smelt().addConditions([Conditions.killedByPlayer()]);


val foo = LootTweaker.getTable("loottweaker_test:foo");
val foo_bar = foo.getPool("bar");
foo_bar.addItemEntry(<minecraft:stick>, 1, 0, 
[
    Functions.zenscript(function(stack, random, context)
    {
        return stack * random.nextInt(1, 64);
    })
], []);