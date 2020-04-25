package leviathan143.loottweaker.common.zenscript.wrapper;

import static java.util.stream.Collectors.toList;

import java.util.*;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.IData;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.DataParser;
import leviathan143.loottweaker.common.lib.QualifiedPoolIdentifier;
import leviathan143.loottweaker.common.mutable_loot.MutableLootPool;
import leviathan143.loottweaker.common.mutable_loot.entry.MutableLootEntry;
import leviathan143.loottweaker.common.mutable_loot.entry.MutableLootEntryEmpty;
import leviathan143.loottweaker.common.mutable_loot.entry.MutableLootEntryItem;
import leviathan143.loottweaker.common.mutable_loot.entry.MutableLootEntryTable;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.*;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".vanilla.loot.LootPool")
public class ZenLootPoolWrapper
{
    private static final String ENTRY_NAME_PREFIX = "loottweaker#";
    private static final int DEFAULT_QUALITY = 0;
    private static final LootCondition[] NO_CONDITIONS = new LootCondition[0];
    private static final LootFunction[] NO_FUNCTIONS = new LootFunction[0];
    //Other state
    private final LootTweakerContext context;
    private final DataParser loggingParser;
    private final List<LootPoolTweaker> tweakers = new ArrayList<>();
    private final Map<String, ZenLootEntryWrapper> tweakedEntries = new HashMap<>();
    //LootPool state
    private final QualifiedPoolIdentifier qualifiedId;
    private int nextEntryNameId = 1;

    public ZenLootPoolWrapper(LootTweakerContext context, ResourceLocation parentTableId, String id)
    {
        this.context = context;
        this.loggingParser = createDataParser(context.getErrorHandler());
        this.qualifiedId = new QualifiedPoolIdentifier(parentTableId, id);
    }

    private DataParser createDataParser(ErrorHandler errorHandler)
    {
        return new DataParser(LootTableManagerAccessors.getGsonInstance(), e -> errorHandler.error(e.getMessage()));
    }

    @ZenMethod
	public void addConditionsHelper(ZenLootConditionWrapper[] conditionWrappers)
	{
        List<LootCondition> parsedConditions = Arrays.stream(conditionWrappers)
            .filter(ZenLootConditionWrapper::isValid)
            .map(ZenLootConditionWrapper::unwrap)
            .collect(toList());
        enqueueTweaker(pool -> pool.addConditions(parsedConditions),
            "Added %d conditions to %s", parsedConditions.size(), qualifiedId);
	}

    @ZenMethod
    public void addConditionsJson(IData[] conditionsJson)
    {
        List<LootCondition> parsedConditions = Arrays.stream(conditionsJson)
            .map(c -> loggingParser.parse(c, LootCondition.class))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .collect(toList());
        enqueueTweaker(pool -> pool.addConditions(parsedConditions),
            "Added %d conditions to %s", parsedConditions.size(), qualifiedId);
    }

	@ZenMethod
	public void clearConditions()
	{
	    enqueueTweaker(MutableLootPool::clearConditions,
	        "Queuing all conditions of %s for removal", qualifiedId);
	}

	@ZenMethod
    public void clearEntries()
    {
        enqueueTweaker(MutableLootPool::clearEntries,
            "Queuing all entries of %s for removal", qualifiedId);
    }

	@ZenMethod
    public ZenLootEntryWrapper getEntry(String entryName)
    {
        if (tweakedEntries.containsKey(entryName))
            return tweakedEntries.get(entryName);

        ZenLootEntryWrapper entry = context.wrapEntry(this, entryName);
        tweakedEntries.put(entryName, entry);
        enqueueTweaker(table ->
        {
            MutableLootEntry targetEntry = table.getEntry(entryName);
            if (targetEntry != null)
                entry.tweak(targetEntry);
            else
                context.getErrorHandler().error("No entry with name '%s' exists in %s", entryName, qualifiedId);
        }, "Retrieved entry %s from %s", entryName, qualifiedId);
        return entry;
    }

	@ZenMethod
	public void removeEntry(String entryName)
	{
		enqueueTweaker(pool ->
		{
		    if (pool.removeEntry(entryName) == null)
                context.getErrorHandler().error("No entry with name '%s' exists in %s", entryName, qualifiedId);
		}, "Queueing entry %s of %s for removal", entryName, qualifiedId);
	}

	@ZenMethod
	public ZenLootEntryWrapper addItemEntry(IItemStack stack, int weight, @Optional String name)
	{
		return addItemEntryInternal(stack, weight, DEFAULT_QUALITY, NO_FUNCTIONS, NO_CONDITIONS, name);
	}

	@ZenMethod
	public ZenLootEntryWrapper addItemEntry(IItemStack stack, int weight, int quality, @Optional String name)
	{
	    return addItemEntryInternal(stack, weight, quality, NO_FUNCTIONS, NO_CONDITIONS, name);
	}

	@ZenMethod
	public ZenLootEntryWrapper addItemEntryHelper(IItemStack stack, int weight, int quality, ZenLootFunctionWrapper[] functions, ZenLootConditionWrapper[] conditions, @Optional String name)
	{
	    return addItemEntryInternal(stack, weight, quality,
            Arrays.stream(functions).filter(ZenLootFunctionWrapper::isValid).map(ZenLootFunctionWrapper::unwrap).toArray(LootFunction[]::new),
            Arrays.stream(conditions).filter(ZenLootConditionWrapper::isValid).map(ZenLootConditionWrapper::unwrap).toArray(LootCondition[]::new),
            name);
	}

	@ZenMethod
	public ZenLootEntryWrapper addItemEntryJson(IItemStack stack, int weight, int quality, IData[] functions, IData[] conditions, @Optional String name)
	{
	    LootFunction[] parsedFunctions = Arrays.stream(functions)
	        .map(c -> loggingParser.parse(c, LootFunction.class))
	        .filter(java.util.Optional::isPresent)
	        .map(java.util.Optional::get)
	        .toArray(LootFunction[]::new);
	    LootCondition[] parsedConditions = Arrays.stream(conditions)
	        .map(c -> loggingParser.parse(c, LootCondition.class))
	        .filter(java.util.Optional::isPresent)
	        .map(java.util.Optional::get)
	        .toArray(LootCondition[]::new);
	    return addItemEntryInternal(stack, weight, quality,
	        parsedFunctions,
	        parsedConditions, name);
	}

    private ZenLootEntryWrapper addItemEntryInternal(IItemStack stack, int weight, int quality, LootFunction[] functions, LootCondition[] conditions, @Optional String name)
    {
        String entryName = name != null ? name : generateName();
        if (stack == null)
            return context.wrapEntry(this, entryName);
        Item item = CraftTweakerMC.getItemStack(stack).getItem();
        MutableLootEntryItem entry = new MutableLootEntryItem(entryName, weight, quality, Lists.newArrayList(conditions),
            item, withStackFunctions(stack, functions));
        return addEntry(entry, "Queued item entry '%s' for addition to %s", entryName, qualifiedId);
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
            functionsOut.add(new SetCount(NO_CONDITIONS, new RandomValueRange(iStack.getAmount())));
        if (iStack.getDamage() > 0 && !damageFuncExists)
        {
            functionsOut.add(stack.isItemStackDamageable()
                // SetDamage takes a percentage, not a number
                ? new SetDamage(NO_CONDITIONS, new RandomValueRange((float) stack.getItemDamage() / (float) stack.getMaxDamage()))
                : new SetMetadata(NO_CONDITIONS, new RandomValueRange(iStack.getDamage())));
        }
        if (iStack.getTag() != DataMap.EMPTY && !nbtFuncExists)
            functionsOut.add(new SetNBT(NO_CONDITIONS, CraftTweakerMC.getNBTCompound(iStack.getTag())));
        return functionsOut;
    }

	@ZenMethod
	public ZenLootEntryWrapper addLootTableEntry(String tableName, int weight, @Optional String name)
	{
	    return addLootTableEntry(tableName, weight, DEFAULT_QUALITY, name);
	}

	@ZenMethod
	public ZenLootEntryWrapper addLootTableEntry(String tableName, int weight, int quality, @Optional String name)
	{
	    return addLootTableEntryInternal(tableName, weight, quality, NO_CONDITIONS, name);
	}

	@ZenMethod
	public ZenLootEntryWrapper addLootTableEntryHelper(String tableName, int weight, int quality, ZenLootConditionWrapper[] conditions, @Optional String name)
	{
	    return addLootTableEntryInternal(tableName, weight, quality,
	        Arrays.stream(conditions).filter(ZenLootConditionWrapper::isValid).map(ZenLootConditionWrapper::unwrap).toArray(LootCondition[]::new),
	        name);
	}

	@ZenMethod
	public void addLootTableEntryJson(String tableName, int weight, int quality, IData[] conditions, @Optional String name)
	{
		LootCondition[] parsedConditions = Arrays.stream(conditions)
		    .map(c -> loggingParser.parse(c, LootCondition.class))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
		    .toArray(LootCondition[]::new);
        addLootTableEntryInternal(tableName, weight, quality,
		    parsedConditions, name);
	}

	private ZenLootEntryWrapper addLootTableEntryInternal(String tableName, int weight, int quality, LootCondition[] conditions, @Optional String name)
    {
        String entryName = name != null ? name : generateName();
        return addEntry(new MutableLootEntryTable(entryName, weight, quality, conditions, new ResourceLocation(tableName)),
            "Queued loot table entry '%s' for addition to %s", entryName, qualifiedId);
    }

	@ZenMethod
	public ZenLootEntryWrapper addEmptyEntry(int weight, @Optional String name)
	{
	    return addEmptyEntry(weight, DEFAULT_QUALITY, name);
	}

	@ZenMethod
	public ZenLootEntryWrapper addEmptyEntry(int weight, int quality, @Optional String name)
	{
	    return addEmptyEntryInternal(weight, quality, NO_CONDITIONS, name);
	}

	@ZenMethod
	public ZenLootEntryWrapper addEmptyEntryHelper(int weight, int quality, ZenLootConditionWrapper[] conditions, @Optional String name)
	{
	    return addEmptyEntryInternal(weight, quality,
            Arrays.stream(conditions).filter(ZenLootConditionWrapper::isValid).map(ZenLootConditionWrapper::unwrap).toArray(LootCondition[]::new),
            name);
	}

	@ZenMethod
	public ZenLootEntryWrapper addEmptyEntryJson(int weight, int quality, IData[] conditions, @Optional String name)
	{
		LootCondition[] parsedConditions = Arrays.stream(conditions)
		    .map(c -> loggingParser.parse(c, LootCondition.class))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
		    .toArray(LootCondition[]::new);
		return addEmptyEntryInternal(weight, quality,
		    parsedConditions, name);
	}

	private ZenLootEntryWrapper addEmptyEntryInternal(int weight, int quality, LootCondition[] conditions, @Optional String name)
	{
        String entryName = name != null ? name : generateName();
        return addEntry(new MutableLootEntryEmpty(entryName, weight, quality, conditions),
            "Queued empty entry '%s' for addition to %s", entryName, qualifiedId);
	}

	private String generateName()
	{
	    return ENTRY_NAME_PREFIX + nextEntryNameId++;
	}

	@ZenMethod
	public void setRolls(float minRolls, float maxRolls)
	{
	    enqueueTweaker(pool -> pool.setRolls(new RandomValueRange(minRolls, maxRolls)),
	        "Rolls of %s will be set to (%.0f, %.0f)", qualifiedId, minRolls, maxRolls);
	}

	@ZenMethod
	public void setBonusRolls(float minBonusRolls, float maxBonusRolls)
	{
	    enqueueTweaker(pool -> pool.setBonusRolls(new RandomValueRange(minBonusRolls, maxBonusRolls)),
	        "Bonus rolls of %s will be set to (%.0f, %.0f)", qualifiedId, minBonusRolls, maxBonusRolls);
	}

	private ZenLootEntryWrapper addEntry(MutableLootEntry entry, String format, Object... args)
	{
	    ZenLootEntryWrapper entryWrapper = context.wrapEntry(this, entry.getName());
	    enqueueTweaker(pool ->
	    {
	        if (pool.getEntry(entry.getName()) != null)
	        {
	            context.getErrorHandler().error("Cannot add entry '%s' to %s. Entry names must be unique within their pool.",
	                entry.getName(), pool.getName());
	            return;
	        }
	        entryWrapper.tweak(entry);
	        pool.addEntry(entry);
	    }, format, args);
        if (tweakedEntries.putIfAbsent(entry.getName(), entryWrapper) != null)
        {
            context.getErrorHandler().error("Cannot add entry '%s' to %s. Entry names must be unique within their pool.",
                entry.getName(), qualifiedId);
            return entryWrapper;
        }
	    return entryWrapper;
	}

	private void enqueueTweaker(LootPoolTweaker tweaker, String format, Object... args)
    {
        tweakers.add(tweaker);
        CraftTweakerAPI.logInfo(String.format(format, args));
    }

    public void tweak(MutableLootPool pool)
    {
        //Note: Tweaks MUST be applied in declaration order, see https://github.com/Daomephsta/LootTweaker/issues/65
        for (LootPoolTweaker tweaker : tweakers)
            tweaker.tweak(pool);
    }

    @FunctionalInterface
    public interface LootPoolTweaker
    {
        public void tweak(MutableLootPool pool);
    }

    public QualifiedPoolIdentifier getQualifiedId()
    {
        return qualifiedId;
    }
}
