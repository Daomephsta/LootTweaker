package leviathan143.loottweaker.common.dagger;

import javax.inject.Singleton;

import dagger.Component;
import leviathan143.loottweaker.common.zenscript.LootTableTweakManager;

@Singleton
@Component(modules = ProductionModule.class)
public interface LootTableTweakManagerProvider
{
    LootTableTweakManager provide();
}
