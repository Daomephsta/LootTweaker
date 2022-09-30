package leviathan143.loottweaker.common.lib;

import net.minecraft.util.ResourceLocation;


/**
 * The "path" to a specific loot pool
 * 
 * @author Daomephsta
 */
public class QualifiedPoolIdentifier
{
    private final ResourceLocation tableId;
    private final String poolId;

    public QualifiedPoolIdentifier(ResourceLocation tableId, String poolId)
    {
        this.tableId = tableId;
        this.poolId = poolId;
    }

    public ResourceLocation getTableId()
    {
        return tableId;
    }

    public String getPoolId()
    {
        return poolId;
    }

    @Override
    public String toString()
    {
        return String.format("pool '%s' of table '%s'", poolId, tableId);
    }
}
