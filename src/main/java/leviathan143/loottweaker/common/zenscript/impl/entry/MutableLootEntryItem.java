package leviathan143.loottweaker.common.zenscript.impl.entry;

import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.darkmagic.LootEntryItemAccessors;
import leviathan143.loottweaker.common.lib.LootConditions;
import leviathan143.loottweaker.common.lib.LootFunctions;
import leviathan143.loottweaker.common.lib.QualifiedEntryIdentifier;
import leviathan143.loottweaker.common.lib.QualifiedPoolIdentifier;
import leviathan143.loottweaker.common.zenscript.api.entry.LootEntryItemRepresentation;
import leviathan143.loottweaker.common.zenscript.api.entry.LootEntryTableRepresentation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class MutableLootEntryItem extends AbstractMutableLootEntry implements LootEntryItemRepresentation
{
    private List<LootFunction> functions;
    private FunctionEffects functionEffects;

    MutableLootEntryItem(LootEntryItem entry, QualifiedPoolIdentifier qualifiedId, ErrorHandler errorHandler)
    {
        super(entry, qualifiedId, errorHandler);
        this.functions = Lists.newArrayList(LootEntryItemAccessors.getFunctions(entry));
        this.functionEffects = new FunctionEffects(LootEntryItemAccessors.getItem(entry));
    }

    public MutableLootEntryItem(QualifiedEntryIdentifier qualifiedId, int weight, int quality, List<LootCondition> conditions, Item item, List<LootFunction> functions, ErrorHandler errorHandler)
    {
        super(qualifiedId, weight, quality, conditions, errorHandler);
        this.functionEffects = new FunctionEffects(item);
        this.functions = functions;
    }

    @Override
    public MutableLootEntryItem deepClone()
    {
        return new MutableLootEntryItem(getQualifiedId(), weight(), quality(),
            LootConditions.deepClone(getConditions()), functionEffects.getItem(),
            LootFunctions.deepClone(functions), errorHandler);
    }

    @Override
    public LootEntryItem toImmutable()
    {
        return new LootEntryItem(functionEffects.getItem(), weight(), quality(),
            functions.toArray(LootFunctions.NONE), getConditions().toArray(LootConditions.NONE),
            name());
    }

    @Override
    public String itemId()
    {
        return functionEffects.getItem().getRegistryName().toString();
    }

    @Override
    public int minimumMetadata()
    {
        functionEffects.initialise(this, functions, errorHandler);
        return functionEffects.getMinMeta();
    }

    @Override
    public int maximumMetadata()
    {
        functionEffects.initialise(this, functions, errorHandler);
        return functionEffects.getMaxMeta();
    }

    @Override
    public int minimumDamageAmount()
    {
        functionEffects.initialise(this, functions, errorHandler);
        return functionEffects.getMinDamageAmount();
    }

    @Override
    public int maximumDamageAmount()
    {
        functionEffects.initialise(this, functions, errorHandler);
        return functionEffects.getMaxDamageAmount();
    }

    @Override
    public float minimumDamagePercent()
    {
        functionEffects.initialise(this, functions, errorHandler);
        return functionEffects.getMinDamagePercent();
    }

    @Override
    public float maximumDamagePercent()
    {
        functionEffects.initialise(this, functions, errorHandler);
        return functionEffects.getMaxDamagePercent();
    }

    @Override
    public IData nbt()
    {
        functionEffects.initialise(this, functions, errorHandler);
        return functionEffects.getNbt();
    }

    @Override
    public LootEntryTableRepresentation asTableEntry()
    {
        errorHandler.error("%s is not a TableEntry", describe());
        return null;
    }

    public ItemStack g()
    {
        ItemStack stack = ItemStack.EMPTY;
        WorldServer world = FMLCommonHandler.instance()
            .getMinecraftServerInstance().getWorld(0);
        LootContext context = new LootContext.Builder(world).build();
        for (LootFunction function : functions)
        {
            function.apply(stack, world.rand, context);
        }
        return stack;
    }
}
