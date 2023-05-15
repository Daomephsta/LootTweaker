package leviathan143.loottweaker.common;

import java.util.Map;

import org.spongepowered.asm.launch.MixinBootstrap;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(-5000)
public class MixinLoader implements IFMLLoadingPlugin
{
    public MixinLoader()
    {
        MixinBootstrap.init();
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {}

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
