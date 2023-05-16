package leviathan143.loottweaker.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;


@Mixin(LootEntry.class)
public interface LootEntryAccessors
{
    @Accessor
    public int getWeight();

    @Accessor
    public void setWeight(int weight);

    @Accessor
    public int getQuality();

    @Accessor
    public void setQuality(int quality);

    @Accessor("conditions")
    public LootCondition[] getConditionsUnsafe();

    @Accessor
    public void setConditions(LootCondition[] conditions);

    @Accessor(value = "entryName", remap = false)
    public void setName(String name);

    @Invoker
    public void callSerialize(JsonObject json, JsonSerializationContext context);
}
