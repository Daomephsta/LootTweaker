package leviathan143.loottweaker.common.zenscript.wrapper;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import leviathan143.loottweaker.common.ErrorHandler;
import leviathan143.loottweaker.common.LootTweaker;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.lib.DataParser;
import leviathan143.loottweaker.common.lib.QualifiedEntryIdentifier;
import leviathan143.loottweaker.common.mutable_loot.entry.MutableLootEntry;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenRegister
@ZenClass(LootTweaker.MODID + ".LootEntry")
public class ZenLootEntryWrapper
{
    //Other state
    private final LootTweakerContext context;
    private final DataParser loggingParser;
    private final List<LootEntryTweaker> tweakers = new ArrayList<>();
    //LootEntry state
    private final QualifiedEntryIdentifier qualifiedId;

    public ZenLootEntryWrapper(LootTweakerContext context, QualifiedEntryIdentifier qualifiedId)
    {
        this.context = context;
        this.loggingParser = createDataParser(context.getErrorHandler());
        this.qualifiedId = qualifiedId;
    }

    private DataParser createDataParser(ErrorHandler errorHandler)
    {
        return new DataParser(LootTableManagerAccessors.getGsonInstance(), e -> errorHandler.error(e.getMessage()));
    }

    @ZenSetter("weight")
    public void setWeight(int weight)
    {
        enqueueTweaker(entry -> entry.setWeight(weight), "Setting weight of %s to %d", qualifiedId, weight);
    }

    @ZenSetter("quality")
    public void setQuality(int quality)
    {
        enqueueTweaker(entry -> entry.setQuality(quality), "Setting quality of %s to %d", qualifiedId, quality);
    }

    @ZenMethod
    public void setConditionsHelper(ZenLootConditionWrapper[] conditions)
    {
        List<LootCondition> parsedConditions = Arrays.stream(conditions)
            .filter(ZenLootConditionWrapper::isValid)
            .map(ZenLootConditionWrapper::unwrap)
            .collect(toList());
        enqueueTweaker(entry -> entry.setConditions(parsedConditions), "Setting conditions of %s", qualifiedId);
    }

    @ZenMethod
    public void setConditionsJson(IData[] conditions)
    {
        List<LootCondition> parsedConditions = Arrays.stream(conditions)
            .map(c -> loggingParser.parse(c, LootCondition.class))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .collect(toList());
        enqueueTweaker(entry -> entry.setConditions(parsedConditions), "Setting conditions of %s", qualifiedId);
    }

    @ZenMethod
    public void addConditionsHelper(ZenLootConditionWrapper[] conditions)
    {
        List<LootCondition> parsedCondition = Arrays.stream(conditions)
            .filter(ZenLootConditionWrapper::isValid)
            .map(ZenLootConditionWrapper::unwrap)
            .collect(toList());
        enqueueTweaker(entry -> entry.addConditions(parsedCondition), "Adding conditions to %s", qualifiedId);
    }

    @ZenMethod
    public void addConditionsJson(IData[] conditions)
    {
        List<LootCondition> parsedConditions = Arrays.stream(conditions)
            .map(c -> loggingParser.parse(c, LootCondition.class))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .collect(toList());
        enqueueTweaker(entry -> entry.addConditions(parsedConditions), "Adding conditions to %s", qualifiedId);
    }

    @ZenMethod
    public void clearConditions()
    {
        enqueueTweaker(MutableLootEntry::clearConditions, "Clearing conditions of %s", qualifiedId);
    }

	private void enqueueTweaker(LootEntryTweaker tweaker, String format, Object... args)
    {
        tweakers.add(tweaker);
        CraftTweakerAPI.logInfo(String.format(format, args));
    }

    public void tweak(MutableLootEntry entry)
    {
        //Note: Tweaks MUST be applied in declaration order, see https://github.com/Daomephsta/LootTweaker/issues/65
        for (LootEntryTweaker tweaker : tweakers)
            tweaker.tweak(entry);
    }

    @FunctionalInterface
    public interface LootEntryTweaker
    {
        public void tweak(MutableLootEntry entry);
    }
}
