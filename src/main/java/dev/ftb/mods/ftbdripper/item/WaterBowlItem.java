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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import org.jetbrains.annotations.Nullable;

public class WaterBowlItem extends Item {
	public WaterBowlItem() {
		super(new Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1));
	}

	public static boolean fillBowl(Level level, Player player) {
		BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

		if (hit.getType() == HitResult.Type.BLOCK && level.getBlockState(hit.getBlockPos()).getBlock() == Blocks.WATER) {
			player.awardStat(Stats.ITEM_USED.get(Items.BOWL));
			player.playSound(SoundEvents.BUCKET_FILL, 1F, 1F);
			return true;
		}

		return false;

	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new WaterBowlFluidHandler(stack);
	}

	public static class WaterBowlFluidHandler extends FluidHandlerItemStackSimple.SwapEmpty {
		protected static final int BOWL_CAPACITY = FluidType.BUCKET_VOLUME / 4;

		public WaterBowlFluidHandler(ItemStack container) {
			super(container, new ItemStack(Items.BOWL), BOWL_CAPACITY);

			setFluid(new FluidStack(Fluids.WATER, BOWL_CAPACITY));
		}

		@Override
		public boolean canFillFluidType(FluidStack fluid) {
			return false;
		}

		@Override
		public boolean canDrainFluidType(FluidStack fluid) {
			return fluid.getFluid() == Fluids.WATER;
		}
	}
}
