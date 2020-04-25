package leviathan143.loottweaker.common.lib;

import net.minecraft.util.ResourceLocation;

/**
 * The "path" to a specific loot entry
 * @author Daomephsta
 */
public class QualifiedEntryIdentifier
{
    private final QualifiedPoolIdentifier parent;
    private final String entryId;

    public QualifiedEntryIdentifier(ResourceLocation tableId, String poolId, String entryId)
    {
        this.parent = new QualifiedPoolIdentifier(tableId, poolId);
        this.entryId = entryId;
    }

    public QualifiedEntryIdentifier(QualifiedPoolIdentifier parent, String entryId)
    {
        this.parent = parent;
        this.entryId = entryId;
    }

    public ResourceLocation getTableId()
    {
        return parent.getTableId();
    }

    public String getPoolId()
    {
        return parent.getPoolId();
    }

    public String getEntryId()
    {
        return entryId;
    }

    @Override
    public String toString()
    {
        return String.format("entry '%s' in %s", entryId, parent);
    }
}
