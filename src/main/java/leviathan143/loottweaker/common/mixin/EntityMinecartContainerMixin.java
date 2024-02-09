package leviathan143.loottweaker.common.mixin;

import javax.annotation.Nullable;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import leviathan143.loottweaker.common.duck.LootTweakerGeneratedFrom;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants.NBT;

@Mixin(EntityMinecartContainer.class)
public class EntityMinecartContainerMixin implements LootTweakerGeneratedFrom
{
    @Unique
    private ResourceLocation loottweaker_generatedFrom;
    @Shadow
    private ResourceLocation lootTable;

    @Inject(method = "readEntityFromNBT", at = @At("HEAD"))
    private void loottweaker$readNbt(NBTTagCompound nbt, CallbackInfo info)
    {
        if (nbt.hasKey(LootTweakerGeneratedFrom.NBT_KEY, NBT.TAG_STRING))
            this.loottweaker_generatedFrom = new ResourceLocation(nbt.getString(LootTweakerGeneratedFrom.NBT_KEY));
    }

    @Inject(method = "writeEntityToNBT", at = @At("HEAD"))
    private void loottweaker$writeNbt(NBTTagCompound nbt, CallbackInfo info)
    {
        if (loottweaker_generatedFrom != null)
            nbt.setString(LootTweakerGeneratedFrom.NBT_KEY, loottweaker_generatedFrom.toString());
    }

    @Inject(method = "addLoot", at = @At(value = "FIELD",
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
