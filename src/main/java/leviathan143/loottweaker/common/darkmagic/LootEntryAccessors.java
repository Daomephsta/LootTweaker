package leviathan143.loottweaker.common.darkmagic;

import java.lang.invoke.MethodHandle;

import net.minecraft.world.storage.loot.LootEntry;

public class LootEntryAccessors extends AbstractAccessors
{
    private static final MethodHandle LootEntry$entryNameSetter;
    static
    {
        try
        {
            //Forge added field, so no SRG name
            LootEntry$entryNameSetter = createFieldSetter(LootEntry.class, "entryName");
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("Failed to initialize LootEntry accessor method handles", e);
        }
    }

    public static void setEntryName(LootEntry entry, String entryName)
    {
        try
        {
            LootEntry$entryNameSetter.invokeExact(entry, entryName);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not invoke LootEntry entry name setter method handle", e);
        }
    }
}
