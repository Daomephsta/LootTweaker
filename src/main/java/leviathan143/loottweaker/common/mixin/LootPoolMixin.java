package leviathan143.loottweaker.common.mixin;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.duck.LootLoadingContext;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

@Mixin(LootPool.class)
public class LootPoolMixin
{
    @Unique
    private static final Logger LOOT_TWEAKER_SANITY_LOGGER = LogManager.getLogger(LootTweaker.MODID + ".sanity_checks");

    @Inject(method = "<init>", at = @At("TAIL"))
    public void loottweaker$uniqueEntryNames(LootEntry[] entries, LootCondition[] conditions,
        RandomValueRange rolls, RandomValueRange bonusRolls, String name, CallbackInfo info)
    {
        Set<String> usedNames = new HashSet<>();
        if (name.startsWith("custom#"))
        {
            name = "loottweaker_fixed_pool_" + LootLoadingContext.get().getPoolDiscriminator();
            LOOT_TWEAKER_SANITY_LOGGER.error(
                "Pool with custom flag found in non-custom table '{}'. Renamed to '{}'.\n" +
                "Report this to the loot adder.", LootLoadingContext.get().tableId, name);
            ((LootPoolAccessors) (LootPool) (Object) this).setName(name);
        }
        for (LootEntry entry : entries)
        {
            if (!usedNames.add(entry.getEntryName()))
            {
                String error = String.format("Duplicate entry name '%s' in pool '%s' of table '{}'",
                    entry.getEntryName(), name, LootLoadingContext.get().tableId);
                if (!(boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
                {
                    String newName = entry.getEntryName() + LootLoadingContext.get().getEntryDiscriminator();
                    LOOT_TWEAKER_SANITY_LOGGER.error(
                        "{}. Duplicate added as '{}'.\n" +
                        "Report this to the loot adder.", error, newName);
                    ((LootEntryAccessors) entry).setName(newName);
                }
                else
                    throw new IllegalArgumentException(error);
            }
            else if (entry.getEntryName().startsWith("custom#"))
            {
                String newName = "loottweaker_fixed_entry_" + LootLoadingContext.get().getEntryDiscriminator();
                LOOT_TWEAKER_SANITY_LOGGER.error(
                    "Entry with custom flag found in non-custom table '{}'. Renamed to '{}'.\n" +
                    "Report this to the loot adder.", LootLoadingContext.get().tableId, newName);
                ((LootEntryAccessors) entry).setName(newName);
            }
        }
    }
}
