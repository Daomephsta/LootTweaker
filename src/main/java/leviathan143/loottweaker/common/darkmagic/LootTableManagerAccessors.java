package leviathan143.loottweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;

import com.google.gson.Gson;

import net.minecraft.world.storage.loot.LootTableManager;


public class LootTableManagerAccessors extends AbstractAccessors
{
    private static final MethodHandle lootTableManager$GSON_INSTANCEGetter;

    static
    {
        try
        {
            lootTableManager$GSON_INSTANCEGetter = createFieldGetter(LootTableManager.class, "field_186526_b",
                "GSON_INSTANCE");
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to initialize LootTableManager accessor method handles", t);
        }
    }

    public static Gson getGsonInstance()
    {
        try
        {
            return (Gson) lootTableManager$GSON_INSTANCEGetter.invokeExact();
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Could not invoke loot table serialiser getter method handle", t);
        }
    }
}
