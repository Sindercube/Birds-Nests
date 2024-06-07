package daniking.birdsnests;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BirdsNests implements ModInitializer {

    public static final String MOD_ID = "birds_nests";
    public static final Logger LOGGER = LoggerFactory.getLogger(BirdsNests.class);

	public static Identifier of(String path) {
		return Identifier.of(MOD_ID, path);
	}

    public static Item BIRDS_NEST = Registry.register(
            Registries.ITEM,
            of("birds_nest"),
            new BirdsNestItem(new Item.Settings())
    );

	@Override
	public void onInitialize() {
        LootTableEvents.MODIFY.register(BirdsNests::modifyLeaves);
		LOGGER.info("Bird's Nests Initialized");
	}

    public static void modifyLeaves(RegistryKey<LootTable> key, LootTable.Builder builder, LootTableSource source) {
        if (!source.isBuiltin()) return;
		if (!isLeavesBlock(key)) return;

        var loot = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(RandomChanceLootCondition.builder(0.050F).build())
                .with(ItemEntry.builder(BIRDS_NEST).build())
                .build();
        builder.pool(loot);
    }

	public static boolean isLeavesBlock(RegistryKey<LootTable> key) {
		Identifier id = key.getValue();
		String[] split = id.getPath().split("/");
		String type = split[0];
		if (!type.equals("blocks")) return false;

		String blockPath = split[split.length-1];
		Identifier blockId = id.withPath(blockPath);
		Optional<Block> block = Registries.BLOCK.getOrEmpty(blockId);
		return block.filter(value -> value instanceof LeavesBlock).isPresent();
	}

}
