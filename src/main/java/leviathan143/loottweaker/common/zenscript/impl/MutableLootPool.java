package leviathan143.loottweaker.common.zenscript.impl;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;

import java.util.*;
import java.util.function.BinaryOperator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.IData;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootPoolAccessors;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.DataParser;
import leviathan143.loottweaker.common.lib.LootConditions;
import leviathan143.loottweaker.common.lib.LootFunctions;
import leviathan143.loottweaker.common.lib.QualifiedPoolIdentifier;
import leviathan143.loottweaker.common.zenscript.api.LootPoolRepresentation;
import leviathan143.loottweaker.common.zenscript.impl.entry.MutableLootEntry;
import leviathan143.loottweaker.common.zenscript.impl.entry.MutableLootEntryEmpty;
import leviathan143.loottweaker.common.zenscript.impl.entry.MutableLootEntryItem;
import leviathan143.loottweaker.common.zenscript.impl.entry.MutableLootEntryTable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.*;
import stanhebben.zenscript.annotations.Optional;

public class MutableLootPool implements LootPoolRepresentation
{
    private static final Logger SANITY_LOGGER = LogManager.getLogger(LootTweaker.MODID + ".sanity_checks");
    private static final int DEFAULT_QUALITY = 0;
    private static final int DEFAULT_WEIGHT = 1;
    private final LootTweakerContext context;
    private final DataParser loggingParser;
    private QualifiedPoolIdentifier qualifiedId;
    private Map<String, MutableLootEntry> entries;
    private List<LootCondition> conditions;
    private RandomValueRange rolls, bonusRolls;

    public MutableLootPool(LootPool pool, ResourceLocation parentTableId, LootTweakerContext context)
    {
        this.context = context;
        this.loggingParser = createDataParser(context.getErrorHandler());
        this.qualifiedId = new QualifiedPoolIdentifier(parentTableId, pool.getName());
        List<LootEntry> immutableEntries = LootPoolAccessors.getEntries(pool);
        this.entries = new HashMap<>(immutableEntries.size());
        int uniqueSuffix = 0;
        for (LootEntry entry : immutableEntries)
        {
            MutableLootEntry mutableEntry = MutableLootEntry.from(entry);
            MutableLootEntry existing = entries.get(mutableEntry.getName());
            if (existing != null)
            {
                String newName = mutableEntry.getName() + uniqueSuffix++;
                SANITY_LOGGER.error("Unexpected duplicate entry name '{}' in pool '{}'. Duplicate added as '{}'."
                    + "\nReport this to the loot adder.", mutableEntry.getName(), getName(), newName);
                mutableEntry.setName(newName);
                entries.put(newName, mutableEntry);
            }
            else
                entries.put(mutableEntry.getName(), mutableEntry);
        }
        this.conditions = LootPoolAccessors.getConditions(pool);
        this.rolls = pool.getRolls();
        this.bonusRolls = pool.getBonusRolls();
    }

    public MutableLootPool(QualifiedPoolIdentifier qualifiedId, Map<String, MutableLootEntry> entries, List<LootCondition> conditions, RandomValueRange rolls, RandomValueRange bonusRolls, LootTweakerContext context)
    {
        this.qualifiedId = qualifiedId;
        this.entries = entries;
        this.conditions = conditions;
        this.rolls = rolls;
        this.bonusRolls = bonusRolls;
        this.context = context;
        this.loggingParser = createDataParser(context.getErrorHandler());
    }

    private DataParser createDataParser(ErrorHandler errorHandler)
    {
        return new DataParser(LootTableManagerAccessors.getGsonInstance(), e -> errorHandler.error(e.getMessage()));
    }

    public MutableLootPool deepClone()
    {
        //Can never be duplicate entries when deep cloning, but be informative just in case
        BinaryOperator<MutableLootEntry> mergeFunction = (a, b) ->
        {
            throw new IllegalStateException(String.format(
                "Unexpected duplicate entry '%s' while deep cloning mutable pool '%s'. Report this to the mod author",
                a.getName(), getName()));
        };
        Map<String, MutableLootEntry> entriesDeepClone = entries.entrySet().stream()
            .collect(toMap(Map.Entry::getKey, e -> e.getValue().deepClone(), mergeFunction, HashMap::new));
        return new MutableLootPool(qualifiedId, entriesDeepClone, LootConditions.deepClone(conditions), rolls, bonusRolls, context);
    }

    public LootPool toImmutable()
    {
        LootEntry[] entriesArray = entries.values().stream()
            .map(MutableLootEntry::toImmutable)
            .toArray(LootEntry[]::new);
        return new LootPool(entriesArray, conditions.toArray(LootConditions.NONE), rolls, bonusRolls, qualifiedId.getPoolId());
    }

    public List<LootCondition> getConditions()
    {
        return conditions;
    }

    public void setConditions(List<LootCondition> conditions)
    {
        this.conditions = conditions;
    }

    public void addCondition(LootCondition condition)
    {
        conditions.add(condition);
    }

    public void addConditions(List<LootCondition> newConditions)
    {
        conditions.addAll(newConditions);
    }

    public void clearConditions()
    {
        conditions.clear();
    }

    public RandomValueRange getRolls()
    {
        return rolls;
    }

    public void setRolls(RandomValueRange rolls)
    {
        this.rolls = rolls;
    }

    public RandomValueRange getBonusRolls()
    {
        return bonusRolls;
    }

    public void setBonusRolls(RandomValueRange bonusRolls)
    {
        this.bonusRolls = bonusRolls;
    }

    public String getName()
    {
        return qualifiedId.getPoolId();
    }

    public void setName(String name)
    {
        qualifiedId.setPoolId(name);
    }

    public Map<String, MutableLootEntry> getEntries()
    {
        return entries;
    }

    public MutableLootEntry getEntry(String name)
    {
        return entries.get(name);
    }

    @Override
    public void addItemEntry(IItemStack iStack, String name)
    {
        addItemEntry(iStack, DEFAULT_WEIGHT, name);
    }

    @Override
    public void addItemEntry(IItemStack iStack, int weight, String name)
    {
        addItemEntry(iStack, weight, DEFAULT_QUALITY, name);
    }

    @Override
    public void addItemEntry(IItemStack iStack, int weight, int quality, String name)
    {
        ItemStack stack = CraftTweakerMC.getItemStack(iStack);
        addEntry(new MutableLootEntryItem(name, weight, quality, new ArrayList<>(),
            stack.getItem(), withStackFunctions(iStack, LootFunctions.NONE)));
    }

    @Override
    public void addItemEntryJson(IItemStack iStack, int weight, int quality, IData[] functions, IData[] conditions, String name)
    {
        ItemStack stack = CraftTweakerMC.getItemStack(iStack);
        List<LootCondition> parsedConditions = Arrays.stream(conditions)
            .map(c1 -> loggingParser.parse(c1, LootCondition.class))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .collect(toCollection(ArrayList::new));
        LootFunction[] parsedFunctions = Arrays.stream(functions)
            .map(c -> loggingParser.parse(c, LootFunction.class))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .toArray(LootFunction[]::new);
        addEntry(new MutableLootEntryItem(name, weight, quality, parsedConditions,
            stack.getItem(), withStackFunctions(iStack, parsedFunctions)));
    }

    /* Adds loot functions equivalent to the damage, stacksize and NBT of the
     * input stack to the passed in array, if loot functions of the same type
     * are not present. */
    private List<LootFunction> withStackFunctions(IItemStack iStack, LootFunction[] existingFunctions)
    {
        ItemStack stack = CraftTweakerMC.getItemStack(iStack);
        boolean sizeFuncExists = false, damageFuncExists = false, nbtFuncExists = false;
        for (LootFunction lootFunction : existingFunctions)
        {
            if (lootFunction instanceof SetCount) sizeFuncExists = true;
            if (lootFunction instanceof SetDamage || lootFunction instanceof SetMetadata) damageFuncExists = true;
            if (lootFunction instanceof SetNBT) nbtFuncExists = true;
        }
        List<LootFunction> functionsOut = Lists.newArrayListWithCapacity(existingFunctions.length + 3);
        Collections.addAll(functionsOut, existingFunctions);
        if (iStack.getAmount() > 1 && !sizeFuncExists)
            functionsOut.add(new SetCount(LootConditions.NONE, new RandomValueRange(iStack.getAmount())));
        if (iStack.getDamage() > 0 && !damageFuncExists)
        {
            functionsOut.add(stack.isItemStackDamageable()
                // SetDamage takes a percentage, not a number
                ? new SetDamage(LootConditions.NONE, new RandomValueRange((float) stack.getItemDamage() / (float) stack.getMaxDamage()))
                : new SetMetadata(LootConditions.NONE, new RandomValueRange(iStack.getDamage())));
        }
        if (iStack.getTag() != DataMap.EMPTY && !nbtFuncExists)
            functionsOut.add(new SetNBT(LootConditions.NONE, CraftTweakerMC.getNBTCompound(iStack.getTag())));
        return functionsOut;
    }

    @Override
    public void addLootTableEntry(String delegateTableId, String name)
    {
        addLootTableEntry(delegateTableId, DEFAULT_WEIGHT, name);
    }

    @Override
    public void addLootTableEntry(String delegateTableId, int weight, String name)
    {
        addLootTableEntry(delegateTableId, weight, DEFAULT_QUALITY, name);
    }

    @Override
    public void addLootTableEntry(String delegateTableId, int weight, int quality, String name)
    {
        //TODO Consider checking existence of the table
        ResourceLocation delegateTableRL = new ResourceLocation(delegateTableId);
        addEntry(new MutableLootEntryTable(name, weight, quality, LootConditions.NONE, delegateTableRL ));
    }

    @Override
    public void addLootTableEntryJson(String delegateTableId, int weight, int quality, IData[] conditions, String name)
    {
        //TODO Consider checking existence of the table
        ResourceLocation delegateTableRL = new ResourceLocation(delegateTableId);
        addEntry(new MutableLootEntryTable(name, weight, quality, parseConditions(conditions), delegateTableRL ));
    }

    @Override
    public void addEmptyEntry(@Optional String name)
    {
        addEmptyEntry(DEFAULT_WEIGHT, name);
    }

    @Override
    public void addEmptyEntry(int weight, @Optional String name)
    {
        addEmptyEntry(weight, DEFAULT_QUALITY, name);
    }

    @Override
    public void addEmptyEntry(int weight, int quality, @Optional String name)
    {
        addEntry(new MutableLootEntryEmpty(name, weight, quality, LootConditions.NONE));
    }

    @Override
    public void addEmptyEntryJson(int weight, int quality, IData[] conditions, @Optional String name)
    {
        addEntry(new MutableLootEntryEmpty(name, weight, quality, parseConditions(conditions)));
    }

    private LootCondition[] parseConditions(IData[] conditions)
    {
        return Arrays.stream(conditions)
            .map(c -> loggingParser.parse(c, LootCondition.class))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .toArray(LootCondition[]::new);
    }

    private int nextEntryId = 0;
    private void addEntry(MutableLootEntry entry)
    {
        //Entry name autogeneration
        if (entry.getName() == null)
            entry.setName("loottweaker#" + nextEntryId++);

        if (entries.putIfAbsent(entry.getName(), entry) != null)
        {
            context.getErrorHandler().error("Cannot add entry '%s' to %s. Entry names must be unique within their pool.",
                entry.getName(), qualifiedId);
        }
        else
            CraftTweakerAPI.logInfo(String.format("Added entry '%s' to %s", entry.getName(), qualifiedId));
    }

    @Override
    public void removeEntry(String entryId)
    {
        if (entries.remove(entryId) == null)
            context.getErrorHandler().error("No entry with id '%s' exists in %s", entryId, qualifiedId);
        else
            CraftTweakerAPI.logInfo(String.format("Removed entry '%s' from %s", entryId, qualifiedId));
    }

    @Override
    public void removeAllEntries()
    {
        entries.clear();
        CraftTweakerAPI.logInfo("Removed all entries from " + qualifiedId);
    }
}
