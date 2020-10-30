package leviathan143.loottweaker.common.lib;

import net.minecraft.util.ResourceLocation;

/**
 * The "path" to a specific loot entry
 * @author Daomephsta
 */
public class QualifiedEntryIdentifier
{
    private final QualifiedPoolIdentifier poolId;
    private String entryName;

    public QualifiedEntryIdentifier(QualifiedPoolIdentifier poolId, String entryName)
    {
        this.poolId = poolId;
        this.entryName = entryName;
    }

    public QualifiedPoolIdentifier getQualifiedPoolId()
    {
        return poolId;
    }

    public ResourceLocation getTableId()
    {
        return poolId.getTableId();
    }

    public String getPoolId()
    {
        return poolId.getPoolId();
    }

    public void setPoolId(String poolId)
    {
        this.poolId.setPoolId(poolId);
    }

    public String getEntryName()
    {
        return entryName;
    }

    public void setEntryName(String entryName)
    {
        this.entryName = entryName;
    }

    @Override
    public String toString()
    {
        return String.format("entry '%s' of %s", entryName, poolId);
    }
}
