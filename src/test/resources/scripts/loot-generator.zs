import loottweaker.LootTweaker;
import loottweaker.LootGenerator;
import crafttweaker.event.EntityLivingJumpEvent;
import crafttweaker.player.IPlayer;
import crafttweaker.damage.IDamageSource;

val simpleDungeon = LootTweaker.getTable("minecraft:chests/simple_dungeon");

events.onEntityLivingJump(function(event as EntityLivingJumpEvent) {
	print(event.entity.definition.id);
}); 