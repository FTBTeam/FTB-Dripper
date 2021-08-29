package dev.ftb.mods.ftbdripper.block;


import dev.ftb.mods.ftbdripper.block.entity.DripperBlockEntity;
import dev.ftb.mods.ftbdripper.item.FTBDripperItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Random;

/**
 * @author LatvianModder
 */
public class DripperBlock extends Block {
	public static final VoxelShape SHAPE = Shapes.or(
			Block.box(7, 8, 7, 9, 9, 9),
			Block.box(0, 13, 0, 16, 16, 16),
			Block.box(1, 12, 1, 15, 13, 15),
			Block.box(3, 11, 3, 13, 12, 13),
			Block.box(5, 10, 5, 11, 11, 11),
			Block.box(6, 9, 6, 10, 10, 10)
	);

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public DripperBlock() {
		super(Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(2F).randomTicks());
		registerDefaultState(getStateDefinition().any().setValue(ACTIVE, false));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new DripperBlockEntity();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);

			if (blockEntity instanceof DripperBlockEntity) {
				FluidTank tank = ((DripperBlockEntity) blockEntity).tank;
				ItemStack heldItem = player.getItemInHand(hand);

				if (heldItem.getItem() == Items.POTION) {
					if (tank.fill(new FluidStack(Fluids.WATER, 300), IFluidHandler.FluidAction.EXECUTE) > 0) {
						heldItem.shrink(1);
						ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.GLASS_BOTTLE), player.inventory.selected);
					}
				} else if (heldItem.getItem() == FTBDripperItems.WATER_BOWL.get()) {
					if (tank.fill(new FluidStack(Fluids.WATER, 250), IFluidHandler.FluidAction.EXECUTE) > 0) {
						heldItem.shrink(1);
						ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.BOWL), player.inventory.selected);
					}
				} else if (!heldItem.isEmpty()) {
					FluidUtil.interactWithFluidHandler(player, hand, tank);
				}

				if (tank.getFluidAmount() == 0) {
					player.displayClientMessage(new TextComponent("Empty"), true);
				} else {
					player.displayClientMessage(new TextComponent(tank.getFluidAmount() + " / " + tank.getCapacity()).append(" of ").append(tank.getFluid().getDisplayName()), true);
				}
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
		if (state.getValue(ACTIVE)) {
			level.addParticle(ParticleTypes.DRIPPING_WATER, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0D, 0D, 0D);
		}
	}

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		BlockPos posBelow = pos;
		BlockState blockBelow;

		do {
			posBelow = posBelow.below();

			if (posBelow.getY() == 0) {
				return;
			}

			blockBelow = level.getBlockState(posBelow);
		}
		while (blockBelow.isAir());

		BlockEntity blockEntity = level.getBlockEntity(pos);

		if (blockEntity instanceof DripperBlockEntity) {
			((DripperBlockEntity) blockEntity).tick(state, posBelow, blockBelow, random);
		}
	}
}
