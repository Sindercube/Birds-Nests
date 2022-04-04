package daniking.birdsnests;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = BirdsNests.MODID)
public class ConfigFile implements ConfigData {

    public double nestDropChance = 0.075;
    public int maxCount = 1;

}
