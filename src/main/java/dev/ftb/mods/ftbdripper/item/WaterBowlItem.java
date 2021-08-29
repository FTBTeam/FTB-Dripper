package dev.ftb.mods.ftbdripper.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

public class WaterBowlItem extends BucketItem {
	public static final WaterBowlItem DUMMY = new WaterBowlItem(true);

	public WaterBowlItem(boolean dummy) {
		super(() -> dummy ? Fluids.EMPTY : Fluids.WATER, new Properties());
	}

	public WaterBowlItem() {
		this(false);
	}

	@Override
	protected ItemStack getEmptySuccessItem(ItemStack stack, Player player) {
		return !player.abilities.instabuild ? new ItemStack(Items.BOWL) : stack;
	}
}
