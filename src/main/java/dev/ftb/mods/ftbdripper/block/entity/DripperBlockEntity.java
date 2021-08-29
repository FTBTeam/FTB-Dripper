package dev.ftb.mods.ftbdripper.block.entity;

import dev.ftb.mods.ftbdripper.FTBDripper;
import dev.ftb.mods.ftbdripper.block.DripperBlock;
import dev.ftb.mods.ftbdripper.recipe.DripRecipe;
import dev.ftb.mods.ftbdripper.recipe.FTBDripperRecipeSerializers;
import dev.ftb.mods.ftbdripper.recipe.NoInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * @author LatvianModder
 */
public class DripperBlockEntity extends BlockEntity {
	public final FluidTank tank;
	private final LazyOptional<IFluidHandler> fluidCap;
	private int prevAmount = -1;

	public DripperBlockEntity() {
		super(FTBDripperBlockEntities.DRIPPER.get());
		tank = new FluidTank(4000) {
			@Override
			protected void onContentsChanged() {
				fluidChanged();
			}
		};
		fluidCap = LazyOptional.of(() -> tank);
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		tag.put("Tank", tank.writeToNBT(new CompoundTag()));
		return super.save(tag);
	}

	@Override
	public void load(BlockState state, CompoundTag tag) {
		super.load(state, tag);
		tank.readFromNBT(tag.getCompound("Tank"));
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return fluidCap.cast();
		}

		return super.getCapability(cap, side);
	}

	private void fluidChanged() {
		setChanged();

		if (!level.isClientSide() && prevAmount != tank.getFluidAmount()) {
			prevAmount = tank.getFluidAmount();
			level.setBlock(worldPosition, getBlockState().setValue(DripperBlock.ACTIVE, prevAmount > 0), 3);
		}
	}

	public void tick(BlockState state, BlockPos posBelow, BlockState blockBelow, Random random) {
		if (!tank.isEmpty()) {
			FluidStack fluid = tank.drain(FTBDripper.consumedFluid, IFluidHandler.FluidAction.EXECUTE);

			for (DripRecipe recipe : level.getRecipeManager().getRecipesFor(FTBDripperRecipeSerializers.DRIP_TYPE, NoInventory.INSTANCE, level)) {
				if (recipe.fluid == fluid.getFluid() && recipe.testInput(blockBelow)) {
					if (random.nextDouble() < recipe.chance) {
						level.setBlock(posBelow, recipe.output, 3);
					}

					break;
				}
			}
		}
	}
}