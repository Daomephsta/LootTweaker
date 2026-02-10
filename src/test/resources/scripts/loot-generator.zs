import loottweaker.LootTweaker;
import crafttweaker.event.EntityLivingJumpEvent;
import crafttweaker.player.IPlayer;
import crafttweaker.damage.IDamageSource;

val simpleDungeon = LootTweaker.getTable("minecraft:chests/simple_dungeon");

events.onEntityLivingJump(function(event as EntityLivingJumpEvent) {
	val world = event.entity.world;
	if (world.remote || !(event.entity instanceof IPlayer))
	{
		return;
	}
	val player as IPlayer = event.entity;
	val lootGenerator = LootTweaker.createLootGenerator(world)
		.luck(42.0)
		.lootedEntity(event.entity)
		.player(player)
		.damageSource(IDamageSource.MAGIC());
	for item in lootGenerator.generate("minecraft:entities/cow") {
		player.dropItem(item);
	}
	for item in lootGenerator.generate(simpleDungeon.id) {
		player.dropItem(item);
	}
});