package dev.ftb.mods.ftbdripper.block.entity;

import dev.ftb.mods.ftbdripper.FTBDripper;
import dev.ftb.mods.ftbdripper.block.DripperBlock;
import dev.ftb.mods.ftbdripper.recipe.DripRecipe;
import dev.ftb.mods.ftbdripper.recipe.FTBDripperRecipeSerializers;
import dev.ftb.mods.ftbdripper.recipe.NoInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author LatvianModder
 */
public class DripperBlockEntity extends BlockEntity {
	private final FluidTank tank;
	private final LazyOptional<IFluidHandler> fluidCap;
	private int prevAmount = -1;
	private Fluid prevFluid = null;

	public DripperBlockEntity(BlockPos pos, BlockState state) {
		super(FTBDripperBlockEntities.DRIPPER.get(), pos, state);
		tank = new FluidTank(4000) {
			@Override
			protected void onContentsChanged() {
				fluidChanged();
			}
		};
		fluidCap = LazyOptional.of(() -> tank);
	}

	public FluidTank getTank() {
		return tank;
	}

	public void writeData(CompoundTag tag) {
		tag.put("Tank", tank.writeToNBT(new CompoundTag()));
	}

	public void readData(CompoundTag tag) {
		tank.readFromNBT(tag.getCompound("Tank"));
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		writeData(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		readData(tag);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return saveWithFullMetadata();
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		load(tag);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		writeData(nbt);
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		readData(packet.getTag());
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.FLUID_HANDLER) {
			return fluidCap.cast();
		}

		return super.getCapability(cap, side);
	}

	@Override
	public void setRemoved() {
		super.setRemoved();

		fluidCap.invalidate();
	}

	private void fluidChanged() {
		setChanged();

		if (!level.isClientSide() && prevAmount != tank.getFluidAmount()) {
			prevAmount = tank.getFluidAmount();
			level.setBlock(worldPosition, getBlockState().setValue(DripperBlock.ACTIVE, prevAmount > 0), Block.UPDATE_ALL);
		}

		if (!level.isClientSide() && prevFluid != tank.getFluid().getFluid()) {
			prevFluid = tank.getFluid().getFluid();
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
		}
	}

	public void tick(BlockState state, BlockPos posBelow, BlockState blockBelow, RandomSource random) {
		if (!tank.isEmpty()) {
			FluidStack fluid = tank.drain(FTBDripper.consumedFluid, IFluidHandler.FluidAction.EXECUTE);

			for (DripRecipe recipe : level.getRecipeManager().getRecipesFor(FTBDripperRecipeSerializers.DRIP_TYPE.get(), NoInventory.INSTANCE, level)) {
				if (recipe.getFluid() == fluid.getFluid() && recipe.testInput(blockBelow)) {
					if (random.nextDouble() < recipe.getChance()) {
						level.setBlock(posBelow, recipe.getOutputState(), Block.UPDATE_ALL);
					}

					break;
				}
			}
		}
	}
}
