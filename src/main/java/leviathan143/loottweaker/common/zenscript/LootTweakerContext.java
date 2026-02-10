package leviathan143.loottweaker.common.zenscript;

import daomephsta.loot_shared.ErrorHandler;
import daomephsta.loot_shared.zenscript.api.factory.LootConditionFactory;
import daomephsta.loot_shared.zenscript.api.factory.LootFunctionFactory;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootPoolWrapper;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper;
import net.minecraft.util.ResourceLocation;


public class LootTweakerContext
{
    private final ErrorHandler errorHandler;

    public LootTweakerContext(ErrorHandler errorHandler)
    {
        this.errorHandler = errorHandler;
    }

    public ErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

    public LootTableTweakManagerImpl createLootTableTweakManager()
    {
        return new LootTableTweakManagerImpl(this);
    }

    public LootConditionFactory createLootConditionFactory()
    {
        return new LootConditionFactory();
    }

    public LootFunctionFactory createLootFunctionFactory()
    {
        return new LootFunctionFactory(this.getErrorHandler());
    }

    public ZenLootTableWrapper wrapLootTable(ResourceLocation id)
    {
        return new ZenLootTableWrapper(this, id);
    }

    public ZenLootPoolWrapper wrapPool(ResourceLocation parentId, String poolName)
    {
        return new ZenLootPoolWrapper(this, parentId, poolName);
    }
}
