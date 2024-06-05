package daniking.birdsnests;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class BirdsNestItem extends Item {

    private static final RegistryKey<LootTable> LOOT_TABLE = RegistryKey.of(
            RegistryKeys.LOOT_TABLE,
            BirdsNests.of("gameplay/birds_nest_loot")
    );

    public BirdsNestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        var stack = player.getStackInHand(hand);
        if (world.isClient) return TypedActionResult.success(stack);

        stack.decrementUnlessCreative(1, player);
        world.playSound(player, player.getBlockPos(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        spawnLoot((ServerWorld)world, player);
        return TypedActionResult.success(stack);
    }

    private static void spawnLoot(ServerWorld world, PlayerEntity player) {
        LootTable lootTable = world.getServer().getReloadableRegistries().getLootTable(LOOT_TABLE);
        LootContextParameterSet parameters = new LootContextParameterSet.Builder(world)
                .build(LootContextTypes.EMPTY);
        List<ItemStack> loot = lootTable.generateLoot(parameters);
        if (loot.isEmpty()) return;

        loot.forEach(stack -> {
            ItemEntity entity = new ItemEntity(
                    world,
                    player.getX(), player.getY() + 0.5, player.getZ(),
                    stack
            );
            entity.setVelocity(
                    player.getRandom().nextGaussian() * 0.05F,
                    0.2D,
                    player.getRandom().nextGaussian() * 0.05F
            );
            world.spawnEntity(entity);
        });
    }

}
