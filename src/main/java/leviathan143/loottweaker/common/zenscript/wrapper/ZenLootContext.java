package leviathan143.loottweaker.common.zenscript.wrapper;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IWorld;
import leviathan143.loottweaker.common.LootTweaker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.storage.loot.LootContext;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootContext")
public class ZenLootContext
{
    private final LootContext delegate;

    public ZenLootContext(LootContext delegate)
    {
        this.delegate = delegate;
    }

    @ZenGetter
    public IEntity lootedEntity()
    {
        return CraftTweakerMC.getIEntity(delegate.getLootedEntity());
    }

    @ZenGetter
    public IPlayer killerPlayer()
    {
        return CraftTweakerMC.getIPlayer((EntityPlayer) delegate.getKillerPlayer());
    }

    @ZenGetter
    public IEntity killer()
    {
        return CraftTweakerMC.getIEntity(delegate.getKiller());
    }

    @ZenGetter
    public float luck()
    {
        return delegate.getLuck();
    }

    @ZenGetter
    public IWorld world()
    {
        return CraftTweakerMC.getIWorld(delegate.getWorld());
    }

    @ZenGetter
    public int lootingModifier()
    {
        return delegate.getLootingModifier();
    }
}
