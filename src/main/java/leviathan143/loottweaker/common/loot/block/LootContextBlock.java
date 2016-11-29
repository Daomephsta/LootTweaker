package leviathan143.loottweaker.common.loot.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

public class LootContextBlock extends LootContext 
{
	IBlockState lootedBlock;
	boolean silkTouch;
	int fortune;
	
	public LootContextBlock(WorldServer world, IBlockState lootedBlock, EntityPlayer player, float luck, int fortune, boolean silkTouch) 
	{
		super(luck, world, world.getLootTableManager(), null, player, null);
		this.lootedBlock = lootedBlock;
		this.fortune = fortune;
		this.silkTouch = silkTouch;
	}

	public static class Builder
	{
		WorldServer world;
		EntityPlayer player;
		IBlockState lootedBlock;
		boolean silkTouch;
		int fortune;
		float luck;
		
		public Builder(WorldServer world)
		{
			this.world = world;
		}
		
		public LootContextBlock.Builder withPlayer(EntityPlayer player)
		{
			this.player = player;
			return this;
		}
		
		public LootContextBlock.Builder withState(IBlockState state)
		{
			this.lootedBlock = state;
			return this;
		}
		
		public LootContextBlock.Builder withSilkTouch(boolean silkTouch)
		{
			this.silkTouch = silkTouch;
			return this;
		}
		
		public LootContextBlock.Builder withFortune(int fortune)
		{
			this.fortune = fortune;
			return this;
		}
		
		public LootContextBlock.Builder withLuck(float luck)
		{
			this.luck = luck;
			return this;
		}
		
		public LootContextBlock build() 
		{
			return new LootContextBlock(world, lootedBlock, player, luck, fortune, silkTouch);
		}
	}
}
