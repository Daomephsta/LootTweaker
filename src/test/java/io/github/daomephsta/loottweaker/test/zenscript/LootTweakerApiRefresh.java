package io.github.daomephsta.loottweaker.test.zenscript;

import java.lang.reflect.Method;
import java.util.function.Supplier;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.zenscript.GlobalRegistry;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerApi;
import leviathan143.loottweaker.common.zenscript.impl.LootTweakerContext;
import stanhebben.zenscript.expression.ExpressionCallVirtual;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolJavaStaticGetter;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.type.natives.JavaMethod;
import stanhebben.zenscript.util.ZenPosition;

public class LootTweakerApiRefresh implements BeforeEachCallback, AfterEachCallback
{
    private static final String API_GLOBAL_IDENTIFIER = "lootTweakerApi";
    private static final String PREVIOUS_API_KEY = "LootTweakerApiRefresh.previousApi";
    private static LootTweakerApi testApi;
    private final Supplier<LootTweakerContext> ltContextFactory;
    private final IZenSymbol testApiGetter;

    public LootTweakerApiRefresh(Supplier<LootTweakerContext> ltContextFactory)
    {
        this.ltContextFactory = ltContextFactory;
        this.testApiGetter = new SymbolJavaStaticGetter(JavaMethod.get(GlobalRegistry.getTypes(), LootTweakerApiRefresh.class, "getTestApi"));
    }

    public static LootTweakerApi getTestApi()
    {
        return testApi;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception
    {
        IZenSymbol previousApi = GlobalRegistry.getGlobals().get(API_GLOBAL_IDENTIFIER);
        context.getStore(Namespace.GLOBAL).put(PREVIOUS_API_KEY, previousApi);

        LootTweakerApiRefresh.testApi = new LootTweakerApi(ltContextFactory.get());
        GlobalRegistry.getGlobals().put(API_GLOBAL_IDENTIFIER, testApiGetter);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception
    {
        LootTweakerApiRefresh.testApi = null;
        IZenSymbol previousApi = (IZenSymbol) context.getStore(Namespace.GLOBAL).get(PREVIOUS_API_KEY);
        GlobalRegistry.getGlobals().put(API_GLOBAL_IDENTIFIER, previousApi);
    }
}
