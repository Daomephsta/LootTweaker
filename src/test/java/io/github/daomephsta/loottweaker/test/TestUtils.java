package io.github.daomephsta.loottweaker.test;

import java.util.Scanner;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import leviathan143.loottweaker.common.darkmagic.LootTableAccessors;
import leviathan143.loottweaker.common.darkmagic.LootTableManagerAccessors;
import leviathan143.loottweaker.common.zenscript.LootTweakerContext;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class TestUtils
{
    private TestUtils() {}

    public static LootTweakerContext context()
    {
        return new LootTweakerContext(new TestErrorHandler());
    }

    public static IItemStack iitemstack(Item item)
    {
        return iitemstack(item, 1);
    }

    public static IItemStack iitemstack(Item item, int amount)
    {
        return iitemstack(item, amount, 0);
    }

    public static IItemStack iitemstack(Item item, int amount, int damage)
    {
        return CraftTweakerMC.getItemStack(item, amount, damage);
    }

    public static LootTable loadTable(String namespace, String path)
    {
        return loadTable(new ResourceLocation(namespace, path));
    }

    public static LootTable loadTable(ResourceLocation name)
    {
        String location = "assets/" + name.getNamespace() + "/loot_tables/" + name.getPath() + ".json";
        StringBuilder dataBuilder = new StringBuilder();
        try(Scanner tableSource = new Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream(location)))
        {
            while(tableSource.hasNextLine())
                dataBuilder.append(tableSource.nextLine());
        }
        LootTable table = ForgeHooks.loadLootTable(LootTableManagerAccessors.getGsonInstance(), name, dataBuilder.toString(), true, null);
        // Unfreeze table & pools, because doing tests will be a PITA otherwise
        ObfuscationReflectionHelper.setPrivateValue(LootTable.class, table, false, "isFrozen");
        for (LootPool pool : LootTableAccessors.getPools(table))
            ObfuscationReflectionHelper.setPrivateValue(LootPool.class, pool, false, "isFrozen");
        return table;
    }
}
