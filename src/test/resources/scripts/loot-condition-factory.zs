import loottweaker.vanilla.loot.Conditions;

Conditions.randomChance(0.21);
Conditions.randomChanceWithLooting(0.35, 1.2);
Conditions.killedByPlayer();
Conditions.killedByNonPlayer();
Conditions.parse({"condition": "minecraft:killed_by_player"});