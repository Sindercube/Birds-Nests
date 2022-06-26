package daniking.birdsnests;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = BirdsNests.MODID)
public class ConfigFile implements ConfigData {
    public double nestDropChance = 0.050;
    public int maxCount = 1;

}
