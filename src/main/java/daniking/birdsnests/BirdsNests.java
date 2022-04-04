package daniking.birdsnests;

import com.google.common.collect.ImmutableList;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BirdsNests implements ModInitializer {

    public static final String MODID = "birdsnests";
    public static final Logger LOGGER = LoggerFactory.getLogger(BirdsNests.class);
    private static final List<Identifier> LOOT_TABLE_IDENTIFIERS = ImmutableList.of(Blocks.OAK_LEAVES.getLootTableId(), Blocks.SPRUCE_LEAVES.getLootTableId(), Blocks.BIRCH_LEAVES.getLootTableId(), Blocks.JUNGLE_LEAVES.getLootTableId(), Blocks.ACACIA_LEAVES.getLootTableId(), Blocks.DARK_OAK_LEAVES.getLootTableId());
    public static ConfigFile configFile;
    public static Item nest;

    @Override
    public void onInitialize() {
        AutoConfig.register(ConfigFile.class, GsonConfigSerializer::new);
        configFile = AutoConfig.getConfigHolder(ConfigFile.class).getConfig();
        // Done for late static initialization
        nest = new NestItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(configFile.maxCount));
        Registry.register(Registry.ITEM, new Identifier(MODID, "nest"), nest);
        registerLootTables();
        LOGGER.info("BirdsNests Initialized");
    }

    static void registerLootTables() {
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            for (final Identifier entry : LOOT_TABLE_IDENTIFIERS) {
                if (id.equals(entry)) {
                    supplier.withPool(buildLoot().build());
                    break;
                }
            }
        });
    }

    static FabricLootPoolBuilder buildLoot() {
        return FabricLootPoolBuilder.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .withCondition(RandomChanceLootCondition.builder((float) configFile.nestDropChance).build())
                .withEntry(ItemEntry.builder(nest).build());
    }
}
