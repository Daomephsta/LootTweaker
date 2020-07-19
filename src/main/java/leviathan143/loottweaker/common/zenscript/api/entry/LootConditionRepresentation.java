package leviathan143.loottweaker.common.zenscript.api.entry;

import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweaker;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootCondition")
public interface LootConditionRepresentation
{
    public boolean isValid();

    public LootCondition toImmutable();
}
