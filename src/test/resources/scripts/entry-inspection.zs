import loottweaker.LootTableLoadEvent;

lootTweakerApi.onLootTableLoad(function(event as LootTableLoadEvent)
{
    for pool in event.table.poolsIterator()
    {
        val entriesIter = pool.entriesIterator();
        for entry in entriesIter
        {
            if (entry.isItemEntry())
            {
                val itemE = entry.asItemEntry();
                val i = itemE.itemId;
                val miM = itemE.minMeta;
                val maM = itemE.maxMeta;
                val miDA = itemE.minDamageAmount;
                val maDA = itemE.maxDamageAmount;
                val miDP = itemE.minDamagePercent;
                val maDP = itemE.maxDamagePercent;
                val n = itemE.nbt;
            }
            else if (entry.isTableEntry())
            {
                val tableE = entry.asTableEntry();   
                val tI = tableE.tableId;
            }
            val n = entry.name;
            val w = entry.weight;
            val q = entry.quality;
        }
    }
});