package leviathan143.loottweaker.common.zenscript.wrapper;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".vanilla.loot.LootCondition")
public class ZenLootConditionWrapper
{
    public static final ZenLootConditionWrapper INVALID = new ZenLootConditionWrapper(null);
    
	public final LootCondition condition;

	public ZenLootConditionWrapper(LootCondition condition)
	{
		this.condition = condition;
	}
	
	public LootCondition unwrap()
    {
        return condition;
    }
    
    public boolean isValid()
    {
        return condition != null;
    }
}
