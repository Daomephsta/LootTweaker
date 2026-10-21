import loottweaker.LootTweaker;
import loottweaker.Conditions;
import loottweaker.Functions;
import crafttweaker.item.IItemStack;
import crafttweaker.util.IRandom;
import loottweaker.LootContext;

Functions.enchantRandomly(["minecraft:thorns"]);
Functions.enchantWithLevels(11, 26, false);
Functions.lootingEnchantBonus(1, 2, 3);
Functions.setCount(1, 3);
Functions.setDamage(0.2, 0.8);
Functions.setMetadata(23, 45);
Functions.setNBT({"foo": "bar"});
Functions.smelt();
Functions.smelt().addConditions([Conditions.killedByPlayer()]);


val foo = LootTweaker.getTable("loottweaker_test:foo");
val foo_bar = foo.getPool("bar");
foo_bar.addItemEntry(<minecraft:stick>, 1, 0, 
[
    Functions.zenscript(function(stack as IItemStack, random as IRandom, context as LootContext) as IItemStack
    {
        return stack * random.nextInt(1, 64);
    })
], []);