package leviathan143.loottweaker.common.zenscript;


import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass(Constants.MODID + ".vanilla.loot.LootCondition")
public class ZenLootConditionWrapper
{
    public final LootCondition condition;
    
    public ZenLootConditionWrapper(LootCondition condition)
    {
	this.condition = condition;
    }
}
