package leviathan143.loottweaker.common.lib;

import leviathan143.loottweaker.common.ErrorHandler;
import net.minecraft.world.storage.loot.RandomValueRange;

public class RandomValueRanges
{
    public static RandomValueRange checked(ErrorHandler errorHandler, float min, float max)
    {
        if (min >= max)
            errorHandler.error("Minimum (%d) must be less than maximum (%d)", min, max);
        return new RandomValueRange(min, max);
    }
}
