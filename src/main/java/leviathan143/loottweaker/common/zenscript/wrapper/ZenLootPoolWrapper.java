package leviathan143.loottweaker.common.zenscript.wrapper;

import java.util.*;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.IData;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootPoolAccessors;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.DataParser;
import leviathan143.loottweaker.common.zenscript.wrapper.ZenLootTableWrapper.LootTableTweak;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.*;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".vanilla.loot.LootPool")
public class ZenLootPoolWrapper implements LootTableTweak
{
    private static final int DEFAULT_QUALITY = 0;
    private static final LootCondition[] NO_CONDITIONS = new LootCondition[0];
    private static final LootFunction[] NO_FUNCTIONS = new LootFunction[0];
    //Other state
    @Inject
    ErrorHandler errorHandler;
    private final DataParser LOGGING_PARSER = new DataParser(LootTableManagerAccessors.getGsonInstance(), e -> errorHandler.handle(e.getMessage()));
    private final Queue<LootPoolTweak> tweaks = new ArrayDeque<>();
    private final ResourceLocation parentTableId;
    //LootPool state
    private final String id;
    private final List<LootEntry> entries = new ArrayList<>();
    private final List<LootCondition> conditions = new ArrayList<>();
    private java.util.Optional<RandomValueRange> rolls;
    private java.util.Optional<RandomValueRange> bonusRolls;

    public ZenLootPoolWrapper(String id, ResourceLocation parentTableId)
    {
        this.id = id;
        this.parentTableId = parentTableId;
        this.rolls = java.util.Optional.empty();
        this.bonusRolls = java.util.Optional.empty();
    }

	public ZenLootPoolWrapper(String id, ResourceLocation parentTableId, int minRolls, int maxRolls, int minBonusRolls, int maxBonusRolls)
    {
	    this.id = id;
        this.parentTableId = parentTableId;
	    this.rolls = java.util.Optional.of(new RandomValueRange(minRolls, maxRolls));
	    this.bonusRolls = java.util.Optional.of(new RandomValueRange(minBonusRolls, maxBonusRolls));
    }

    @ZenMethod
	public void addConditionsHelper(ZenLootConditionWrapper[] conditionWrappers)
	{
        for (ZenLootConditionWrapper conditionWrapper : conditionWrappers)
        {
            if (conditionWrapper.isValid())
                this.conditions.add(conditionWrapper.condition);
        }
	}

	@ZenMethod
	public void addConditionsJson(IData[] conditionsJson)
	{
		for (IData conditionData : conditionsJson)
		    LOGGING_PARSER.parse(conditionData, LootCondition.class).ifPresent(this.conditions::add);
	}

	@ZenMethod
	public void clearConditions()
	{
	    enqueueTweak(pool -> LootPoolAccessors.getConditions(pool).clear(),
	        "Queuing all conditions of pool %s in table %s for removal", id, parentTableId);
	}

	@ZenMethod
    public void clearEntries()
    {
        enqueueTweak(pool -> LootPoolAccessors.getEntries(pool).clear(),
            "Queuing all entries of pool %s in table %s for removal", id, parentTableId);
    }

	@ZenMethod
	public void removeEntry(String entryName)
	{
		enqueueTweak(pool ->
		{
		    if (pool.removeEntry(entryName) == null)
                errorHandler.handle(String.format("No entry with name %s exists in pool %s", entryName, id));
		}, String.format("Queueing entry %s of pool %s for removal", entryName, id));
	}

	@ZenMethod
	public void addItemEntry(IItemStack stack, int weight, @Optional String name)
	{
		addItemEntryInternal(stack, weight, DEFAULT_QUALITY, NO_FUNCTIONS, NO_CONDITIONS, name);
	}

	@ZenMethod
	public void addItemEntry(IItemStack stack, int weight, int quality, @Optional String name)
	{
		addItemEntryInternal(stack, weight, quality, NO_FUNCTIONS, NO_CONDITIONS, name);
	}

	@ZenMethod
	public void addItemEntryHelper(IItemStack stack, int weight, int quality, ZenLootFunctionWrapper[] functions, ZenLootConditionWrapper[] conditions, @Optional String name)
	{
		addItemEntryInternal(stack, weight, quality,
            Arrays.stream(functions).filter(ZenLootFunctionWrapper::isValid).map(ZenLootFunctionWrapper::unwrap).toArray(LootFunction[]::new),
            Arrays.stream(conditions).filter(ZenLootConditionWrapper::isValid).map(ZenLootConditionWrapper::unwrap).toArray(LootCondition[]::new),
            name);
	}

	@ZenMethod
	public void addItemEntryJson(IItemStack stack, int weight, int quality, IData[] functions, IData[] conditions, @Optional String name)
	{
		addItemEntryInternal(stack, weight, quality,
		    Arrays.stream(functions).map(c -> LOGGING_PARSER.parse(c, LootFunction.class)).toArray(LootFunction[]::new),
		    Arrays.stream(conditions).map(c -> LOGGING_PARSER.parse(c, LootCondition.class)).toArray(LootCondition[]::new), name);
	}

    private void addItemEntryInternal(IItemStack stack, int weight, int quality, LootFunction[] functions, LootCondition[] conditions, @Optional String name)
    {
	    Item item = CraftTweakerMC.getItemStack(stack).getItem();
        if (name == null) name = item.getRegistryName().toString();
        entries.add(new LootEntryItem(item, weight, quality, addStackFunctions(stack, functions), conditions, name));
        CraftTweakerAPI.logInfo(String.format("Queued item entry '%s' for addition to pool %s of table %s", name, id, parentTableId));
    }

    /* Adds loot functions equivalent to the damage, stacksize and NBT of the
     * input stack to the passed in array, if loot functions of the same type
     * are not present. */
    private LootFunction[] addStackFunctions(IItemStack iStack, LootFunction[] existingFunctions)
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
        return functionsOut.toArray(NO_FUNCTIONS);
    }

	@ZenMethod
	public void addLootTableEntry(String tableName, int weight, @Optional String name)
	{
		addLootTableEntry(tableName, weight, DEFAULT_QUALITY, name);
	}

	@ZenMethod
	public void addLootTableEntry(String tableName, int weight, int quality, @Optional String name)
	{
		addLootTableEntryInternal(tableName, weight, quality, NO_CONDITIONS, name);
	}

	@ZenMethod
	public void addLootTableEntryHelper(String tableName, int weight, int quality, ZenLootConditionWrapper[] conditions, @Optional String name)
	{
	    addLootTableEntryInternal(tableName, weight, quality,
	        Arrays.stream(conditions).filter(ZenLootConditionWrapper::isValid).map(ZenLootConditionWrapper::unwrap).toArray(LootCondition[]::new),
	        name);
	}

	@ZenMethod
	public void addLootTableEntryJson(String tableName, int weight, int quality, IData[] conditions, @Optional String name)
	{
		addLootTableEntryInternal(tableName, weight, quality,
		    Arrays.stream(conditions).map(c -> LOGGING_PARSER.parse(c, LootCondition.class)).toArray(LootCondition[]::new), name);
	}

	private void addLootTableEntryInternal(String tableName, int weight, int quality, LootCondition[] conditions, @Optional String name)
    {
	    if (name == null) name = tableName;
	    entries.add(new LootEntryTable(new ResourceLocation(tableName), weight, quality, conditions, name));
	    CraftTweakerAPI.logInfo(String.format("Queued loot table entry '%s' for addition to pool %s of table %s", name, id, parentTableId));
    }

	@ZenMethod
	public void addEmptyEntry(int weight, @Optional String name)
	{
		addEmptyEntry(weight, DEFAULT_QUALITY, name);
	}

	@ZenMethod
	public void addEmptyEntry(int weight, int quality, @Optional String name)
	{
		addEmptyEntryInternal(weight, quality, NO_CONDITIONS, name);
	}

	@ZenMethod
	public void addEmptyEntryHelper(int weight, int quality, ZenLootConditionWrapper[] conditions, @Optional String name)
	{
	    addEmptyEntryInternal(weight, quality,
            Arrays.stream(conditions).filter(ZenLootConditionWrapper::isValid).map(ZenLootConditionWrapper::unwrap).toArray(LootCondition[]::new),
            name);
	}

	@ZenMethod
	public void addEmptyEntryJson(int weight, int quality, IData[] conditions, @Optional String name)
	{
		addEmptyEntryInternal(weight, quality,
		    Arrays.stream(conditions).map(c -> LOGGING_PARSER.parse(c, LootCondition.class)).toArray(LootCondition[]::new), name);
	}

	public void addEmptyEntryInternal(int weight, int quality, LootCondition[] conditions, @Optional String name)
	{
	    if (name == null) name = "empty";
	    entries.add(new LootEntryEmpty(weight, quality, conditions, name));
	    CraftTweakerAPI.logInfo(String.format("Queued empty entry '%s' for addition to pool %s of table %s", name, id, parentTableId));
	}

	@ZenMethod
	public void setRolls(float minRolls, float maxRolls)
	{
	    this.rolls = java.util.Optional.of(new RandomValueRange(minRolls, maxRolls));
	    CraftTweakerAPI.logInfo(String.format("Rolls of pool %s in table %s will be set to (%f, %f)", id, parentTableId, minRolls, maxRolls));
	}

	@ZenMethod
	public void setBonusRolls(float minBonusRolls, float maxBonusRolls)
	{
	    this.bonusRolls = java.util.Optional.of(new RandomValueRange(minBonusRolls, maxBonusRolls));
	    CraftTweakerAPI.logInfo(String.format("Bonus rolls of pool %s in table %s will be set to (%f, %f)", id, parentTableId, minBonusRolls, maxBonusRolls));
	}

	private void enqueueTweak(LootPoolTweak tweak, String format, Object... args)
    {
        tweaks.add(tweak);
        CraftTweakerAPI.logInfo(String.format(format, args));
    }

    private void enqueueTweak(LootPoolTweak tweak, String description)
    {
        tweaks.add(tweak);
        CraftTweakerAPI.logInfo(description);
    }

	@Override
	public void tweak(LootTable table)
	{
	    LootEntry[] lootEntriesArray = entries.toArray(new LootEntry[0]);
        LootCondition[] poolConditionsArray = conditions.toArray(NO_CONDITIONS);
        LootPool existing = table.getPool(id);
        if (existing != null)
            tweak(existing);
        else
        {
            table.addPool(new LootPool(lootEntriesArray, poolConditionsArray, rolls.get(), bonusRolls.get(), id));
            CraftTweakerAPI.logInfo(String.format("Added new pool %s to table %s", id, parentTableId));
        }
	}

    public void tweak(LootPool pool)
    {
        for (LootEntry entry : entries)
            pool.addEntry(entry);
        LootPoolAccessors.getConditions(pool).addAll(conditions);
        rolls.ifPresent(pool::setRolls);
        bonusRolls.ifPresent(pool::setBonusRolls);
        while (!tweaks.isEmpty())
            tweaks.poll().tweak(pool);
    }

    @FunctionalInterface
    public interface LootPoolTweak
    {
        public void tweak(LootPool pool);
    }
}
