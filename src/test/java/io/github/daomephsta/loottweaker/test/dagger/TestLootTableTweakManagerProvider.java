package io.github.daomephsta.loottweaker.test.dagger;

import javax.inject.Singleton;

import dagger.Component;
import leviathan143.loottweaker.common.zenscript.LootTableTweakManager;

@Singleton
@Component(modules = TestModule.class)
public interface TestLootTableTweakManagerProvider
{
    LootTableTweakManager provide();
}
