package dev.ftb.mods.ftbdripper.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.Nullable;

public class WaterBowlItem extends Item {
	public WaterBowlItem() {
		super(new Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1));
	}

	public static boolean fillBowl(Level level, Player player, InteractionHand hand) {
		BlockHitResult hit = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY);

		if (hit.getType() != HitResult.Type.BLOCK) {
			return false;
		}

		BlockState state = level.getBlockState(hit.getBlockPos());

		if (state.getBlock() == Blocks.WATER) {
			player.awardStat(Stats.ITEM_USED.get(Items.BOWL));
			player.playSound(SoundEvents.BUCKET_FILL, 1F, 1F);
			return true;
		}

		return false;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new FluidHandlerItemStack.SwapEmpty(stack, new ItemStack(Items.BOWL), FluidAttributes.BUCKET_VOLUME / 4);
	}
}
