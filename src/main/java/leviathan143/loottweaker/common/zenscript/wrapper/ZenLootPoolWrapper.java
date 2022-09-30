package leviathan143.loottweaker.common.zenscript.wrapper;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.DataMap;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.lib.*;
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
    //Other state
    private final LootTweakerContext context;
    private final List<LootPoolTweaker> tweakers = new ArrayList<>();
    //LootPool state
    private final QualifiedPoolIdentifier qualifiedId;
    private int nextEntryNameId = 1;

    public ZenLootPoolWrapper(LootTweakerContext context, ResourceLocation parentTableId, String id)
    {
        this.context = context;
        this.qualifiedId = new QualifiedPoolIdentifier(parentTableId, id);
    }

    @ZenMethod
    public void addConditions(ZenLootConditionWrapper[] conditions)
    {
        if (!Arguments.nonNull(context.getErrorHandler(), "conditions", conditions)) return;
        List<LootCondition> parsedConditions = Arrays.stream(conditions)
            .filter(ZenLootConditionWrapper::isValid)
            .map(ZenLootConditionWrapper::unwrap)
            .collect(toList());
        enqueueTweaker(pool -> pool.addConditions(parsedConditions), "Added %d conditions to %s",
            parsedConditions.size(), qualifiedId);
    }

    @ZenMethod
    public void clearConditions()
    {
        enqueueTweaker(MutableLootPool::clearConditions, "Queuing all conditions of %s for removal", qualifiedId);
    }

    @ZenMethod
    public void clearEntries()
    {
        enqueueTweaker(MutableLootPool::clearEntries, "Queuing all entries of %s for removal", qualifiedId);
    }

    @ZenMethod
    public void removeEntry(String entryName)
    {
        enqueueTweaker(pool ->
        {
            if (pool.removeEntry(entryName) == null)
                context.getErrorHandler().error("No entry with name %s exists in %s", entryName, qualifiedId);
        }, "Queueing entry %s of %s for removal", entryName, qualifiedId);
    }

    @ZenMethod
    public void addItemEntry(IItemStack stack, int weight, @Optional
    String name)
    {
        if (!Arguments.nonNull(context.getErrorHandler(), "stack", stack)) return;
        addItemEntryInternal(stack, weight, DEFAULT_QUALITY, LootFunctions.NONE, LootConditions.NONE, name);
    }

    @ZenMethod
    public void addItemEntry(IItemStack stack, int weight, int quality, @Optional
    String name)
    {
        if (!Arguments.nonNull(context.getErrorHandler(), "stack", stack)) return;
        addItemEntryInternal(stack, weight, quality, LootFunctions.NONE, LootConditions.NONE, name);
    }

    @ZenMethod
    public void addItemEntry(IItemStack stack, int weight, int quality, ZenLootFunctionWrapper[] functions,
        ZenLootConditionWrapper[] conditions, @Optional
        String name)
    {
        if (!Arguments.nonNull(context.getErrorHandler(), "stack", stack, "functions", functions, "conditions",
            conditions)) return;
        LootFunction[] unwrappedFunctions = Arrays.stream(functions)
            .filter(ZenLootFunctionWrapper::isValid)
            .map(ZenLootFunctionWrapper::unwrap)
            .toArray(LootFunction[]::new);
        LootCondition[] unwrappedConditions = Arrays.stream(conditions)
            .filter(ZenLootConditionWrapper::isValid)
            .map(ZenLootConditionWrapper::unwrap)
            .toArray(LootCondition[]::new);
        addItemEntryInternal(stack, weight, quality, unwrappedFunctions, unwrappedConditions, name);
    }

    private void addItemEntryInternal(IItemStack stack, int weight, int quality, LootFunction[] functions,
        LootCondition[] conditions, @Optional
        String name)
    {
        if (stack == null) return;
        String entryName = name != null ? name : generateName();
        Item item = CraftTweakerMC.getItemStack(stack).getItem();
        MutableLootEntryItem entry = new MutableLootEntryItem(entryName, weight, quality,
            Lists.newArrayList(conditions), item, withStackFunctions(stack, functions));
        addEntry(entry, "Queued item entry '%s' for addition to %s", entryName, qualifiedId);
    }

    /* Adds loot functions equivalent to the damage, stacksize and NBT of the input
     * stack to the passed in array, if loot functions of the same type are not
     * present. */
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
                ? new SetDamage(LootConditions.NONE,
                    new RandomValueRange((float) stack.getItemDamage() / (float) stack.getMaxDamage()))
                : new SetMetadata(LootConditions.NONE, new RandomValueRange(iStack.getDamage())));
        }
        if (iStack.getTag() != DataMap.EMPTY && !nbtFuncExists)
            functionsOut.add(new SetNBT(LootConditions.NONE, CraftTweakerMC.getNBTCompound(iStack.getTag())));
        return functionsOut;
    }

    @ZenMethod
    public void addLootTableEntry(String tableName, int weight, @Optional
    String name)
    {
        addLootTableEntry(tableName, weight, DEFAULT_QUALITY, name);
    }

    @ZenMethod
    public void addLootTableEntry(String tableName, int weight, int quality, @Optional
    String name)
    {
        if (!Arguments.nonNull(context.getErrorHandler(), "table name", tableName)) return;
        addLootTableEntryInternal(tableName, weight, quality, LootConditions.NONE, name);
    }

    @ZenMethod
    public void addLootTableEntry(String tableName, int weight, int quality, ZenLootConditionWrapper[] conditions,
        @Optional
        String name)
    {
        if (!Arguments.nonNull(context.getErrorHandler(), "table name", tableName, "conditions", conditions))
            return;
        LootCondition[] unwrappedConditions = Arrays.stream(conditions)
            .filter(ZenLootConditionWrapper::isValid)
            .map(ZenLootConditionWrapper::unwrap)
            .toArray(LootCondition[]::new);
        addLootTableEntryInternal(tableName, weight, quality, unwrappedConditions, name);
    }

    private void addLootTableEntryInternal(String tableName, int weight, int quality, LootCondition[] conditions,
        @Optional
        String name)
    {
        String entryName = name != null ? name : generateName();
        addEntry(
            new MutableLootEntryTable(entryName, weight, quality, conditions, new ResourceLocation(tableName)),
            "Queued loot table entry '%s' for addition to %s", entryName, qualifiedId);
    }

    @ZenMethod
    public void addEmptyEntry(int weight, @Optional
    String name)
    {
        addEmptyEntry(weight, DEFAULT_QUALITY, name);
    }

    @ZenMethod
    public void addEmptyEntry(int weight, int quality, @Optional
    String name)
    {
        addEmptyEntryInternal(weight, quality, LootConditions.NONE, name);
    }

    @ZenMethod
    public void addEmptyEntry(int weight, int quality, ZenLootConditionWrapper[] conditions, @Optional
    String name)
    {
        if (!Arguments.nonNull(context.getErrorHandler(), "conditions", conditions)) return;
        LootCondition[] unwrappedConditions = Arrays.stream(conditions)
            .filter(ZenLootConditionWrapper::isValid)
            .map(ZenLootConditionWrapper::unwrap)
            .toArray(LootCondition[]::new);
        addEmptyEntryInternal(weight, quality, unwrappedConditions, name);
    }

    private void addEmptyEntryInternal(int weight, int quality, LootCondition[] conditions, @Optional
    String name)
    {
        String entryName = name != null ? name : generateName();
        addEntry(new MutableLootEntryEmpty(entryName, weight, quality, conditions),
            "Queued empty entry '%s' for addition to %s", entryName, qualifiedId);
    }

    private String generateName()
    {
        return ENTRY_NAME_PREFIX + nextEntryNameId++;
    }

    @ZenMethod
    public void setRolls(float minRolls, float maxRolls)
    {
        enqueueTweaker(
            pool -> pool.setRolls(RandomValueRanges.checked(context.getErrorHandler(), minRolls, maxRolls)),
            "Rolls of %s will be set to (%.0f, %.0f)", qualifiedId, minRolls, maxRolls);
    }

    @ZenMethod
    public void setBonusRolls(float minBonusRolls, float maxBonusRolls)
    {
        enqueueTweaker(
            pool -> pool
                .setBonusRolls(RandomValueRanges.checked(context.getErrorHandler(), minBonusRolls, maxBonusRolls)),
            "Bonus rolls of %s will be set to (%.0f, %.0f)", qualifiedId, minBonusRolls, maxBonusRolls);
    }

    private void addEntry(MutableLootEntry entry, String format, Object... args)
    {
        enqueueTweaker(pool ->
        {
            if (pool.getEntry(entry.getName()) != null)
            {
                context.getErrorHandler()
                    .error("Cannot add entry '%s' to %s. Entry names must be unique within their pool.",
                        entry.getName(), pool.getName());
                return;
            }
            pool.addEntry(entry);
        }, format, args);
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
