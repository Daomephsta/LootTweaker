package leviathan143.loottweaker.common.mixin;

import javax.annotation.Nullable;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import leviathan143.loottweaker.common.duck.LootTweakerGeneratedFrom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants.NBT;

@Mixin(TileEntityLockableLoot.class)
public class TileEntityLockableLootMixin implements LootTweakerGeneratedFrom
{
    @Unique
    private ResourceLocation loottweaker_generatedFrom;
    @Shadow
    protected ResourceLocation lootTable;

    @Inject(method = "checkLootAndRead", at = @At("HEAD"))
    private void loottweaker$readNbt(NBTTagCompound nbt, CallbackInfoReturnable<Boolean> info)
    {
        if (nbt.hasKey(LootTweakerGeneratedFrom.NBT_KEY, NBT.TAG_STRING))
            this.loottweaker_generatedFrom = new ResourceLocation(nbt.getString(LootTweakerGeneratedFrom.NBT_KEY));
    }

    @Inject(method = "checkLootAndWrite", at = @At("HEAD"))
    private void loottweaker$writeNbt(NBTTagCompound nbt, CallbackInfoReturnable<Boolean> info)
    {
        if (loottweaker_generatedFrom != null)
            nbt.setString(LootTweakerGeneratedFrom.NBT_KEY, loottweaker_generatedFrom.toString());
    }

    @Inject(method = "fillWithLoot", at = @At(value = "FIELD",
        target = "lootTable:Lnet/minecraft/util/ResourceLocation;", opcode = Opcodes.PUTFIELD))
    private void loottweaker$setGeneratedFrom(@Nullable EntityPlayer player, CallbackInfo info)
    {
        this.loottweaker_generatedFrom = this.lootTable;
    }

    @Override
    public ResourceLocation loottweaker_getGeneratedFrom()
    {
        return loottweaker_generatedFrom;
    }
}
