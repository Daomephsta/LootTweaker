package leviathan143.loottweaker.common.dagger;

import javax.inject.Singleton;

import dagger.Component;
import io.github.daomephsta.loottweaker.test.dagger.TestModule;
import leviathan143.loottweaker.common.zenscript.LootTableTweakManager;

@Singleton
@Component(modules = TestModule.class)
public interface LootTableTweakManagerProvider
{
    LootTableTweakManager provide();
}
