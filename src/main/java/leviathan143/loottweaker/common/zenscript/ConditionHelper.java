package leviathan143.loottweaker.common.zenscript;

import leviathan143.loottweaker.common.LootTweakerMain.Constants;
import net.minecraft.world.storage.loot.conditions.*;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(Constants.MODID + ".vanilla.loot.Conditions")
public class ConditionHelper
{
    @ZenMethod
    public static ZenLootConditionWrapper randomChance(float chance)
    {
	return new ZenLootConditionWrapper(new RandomChance(chance));
    }
    
    @ZenMethod
    public static ZenLootConditionWrapper randomChanceWithLooting(float chance, float lootingMult)
    {
	return new ZenLootConditionWrapper(new RandomChanceWithLooting(chance, lootingMult));
    }
    
    @ZenMethod
    public static ZenLootConditionWrapper killedByPlayer()
    {
	return new ZenLootConditionWrapper(new KilledByPlayer(false));
    }
    
    @ZenMethod
    public static ZenLootConditionWrapper killedByNonPlayer()
    {
	return new ZenLootConditionWrapper(new KilledByPlayer(true));
    }
}
