package leviathan143.loottweaker.common.lib;

import leviathan143.loottweaker.common.ErrorHandler;
import net.minecraft.world.storage.loot.RandomValueRange;


public class RandomValueRanges
{
    public static RandomValueRange checked(ErrorHandler errorHandler, float min, float max)
    {
        if (min > max) errorHandler.error("Minimum (%f) must be less than or equal to maximum (%f)", min, max);
        return new RandomValueRange(min, max);
    }
}
