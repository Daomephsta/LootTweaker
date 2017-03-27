package leviathan143.loottweaker.common.lib;

import java.lang.reflect.*;

import com.google.gson.*;

import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

//All this to fix a single typo in Mojang's code
public class PatchedLootEntrySerialiser extends LootEntry.Serializer
{   
    private static Field lootEntry$weight = ReflectionHelper.findField(LootEntry.class, "c", "field_186364_c", "weight");
    private static Field lootEntry$quality = ReflectionHelper.findField(LootEntry.class, "d", "field_186365_d", "quality");
    private static Field lootEntry$conditions = ReflectionHelper.findField(LootEntry.class, "e", "field_186366_e", "conditions");
    private static Method lootEntry$serialize = ReflectionHelper.findMethod(LootEntry.class, null, new String[] {"a", "func_186362_a", "serialize"}, JsonObject.class, JsonSerializationContext.class);
    
    @Override
    public JsonElement serialize(LootEntry p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
    {
	int weight = -1;
	int quality = -1;
	LootCondition[] conditions = null;
	try
	{
	    weight = lootEntry$weight.getInt(p_serialize_1_);
	    quality = lootEntry$quality.getInt(p_serialize_1_);
	    conditions = (LootCondition[]) lootEntry$conditions.get(p_serialize_1_);
	}
	catch (IllegalArgumentException | IllegalAccessException e)
	{
	    e.printStackTrace();
	}
	
        JsonObject jsonobject = new JsonObject();
        if (p_serialize_1_.getEntryName() != null && !p_serialize_1_.getEntryName().startsWith("custom#"))
            jsonobject.addProperty("entryName", p_serialize_1_.getEntryName());
        jsonobject.addProperty("weight", (Number)Integer.valueOf(weight));
        jsonobject.addProperty("quality", (Number)Integer.valueOf(quality));

        if (conditions.length > 0)
        {
            jsonobject.add("conditions", p_serialize_3_.serialize(conditions));
        }

        String type = net.minecraftforge.common.ForgeHooks.getLootEntryType(p_serialize_1_);
        if (type != null) jsonobject.addProperty("type", type);
        else
        if (p_serialize_1_ instanceof LootEntryItem)
        {
            jsonobject.addProperty("type", "item");
        }
        else if (p_serialize_1_ instanceof LootEntryTable)
        {
            jsonobject.addProperty("type", "loot_table");
        }
        else
        {
            if (!(p_serialize_1_ instanceof LootEntryEmpty))
            {
                throw new IllegalArgumentException("Don\'t know how to serialize " + p_serialize_1_);
            }

            jsonobject.addProperty("type", "empty");
        }
        
        try
	{
	    lootEntry$serialize.invoke(p_serialize_1_, jsonobject, p_serialize_3_);
	}
	catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
	{
	    e.printStackTrace();
	}
        return jsonobject;
    }
}
