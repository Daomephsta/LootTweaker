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
import stanhebben.zenscript.annotations.ZenMethod;


@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootContext")
public class ZenLootContext
{
    private final LootContext delegate;

    public ZenLootContext(LootContext delegate)
    {
        this.delegate = delegate;
    }

    @ZenMethod
    public IEntity lootedEntity()
    {
        return CraftTweakerMC.getIEntity(delegate.getLootedEntity());
    }

    @ZenMethod
    public IPlayer killerPlayer()
    {
        return CraftTweakerMC.getIPlayer((EntityPlayer) delegate.getKillerPlayer());
    }

    @ZenMethod
    public IEntity killer()
    {
        return CraftTweakerMC.getIEntity(delegate.getKiller());
    }

    @ZenMethod
    public float luck()
    {
        return delegate.getLuck();
    }

    @ZenMethod
    public IWorld world()
    {
        return CraftTweakerMC.getIWorld(delegate.getWorld());
    }

    @ZenMethod
    public int lootingModifier()
    {
        return delegate.getLootingModifier();
    }
}
