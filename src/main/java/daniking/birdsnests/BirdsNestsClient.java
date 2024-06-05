package daniking.birdsnests;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

public class BirdsNestsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
			content.addAfter(Items.WARPED_FUNGUS_ON_A_STICK, BirdsNests.BIRDS_NEST);
		});
	}

}
