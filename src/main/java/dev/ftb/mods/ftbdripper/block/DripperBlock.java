package dev.ftb.mods.ftbdripper.block;


import dev.ftb.mods.ftbdripper.block.entity.DripperBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * @author LatvianModder
 */
public class DripperBlock extends Block implements EntityBlock {
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

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DripperBlockEntity(pos, state);
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
				FluidUtil.interactWithFluidHandler(player, hand, tank);

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
			BlockEntity entity = level.getBlockEntity(pos);
			boolean foundParticle = false;

			if (entity instanceof DripperBlockEntity && !((DripperBlockEntity) entity).tank.isEmpty()) {
				Fluid fluid = ((DripperBlockEntity) entity).tank.getFluid().getFluid();

				if (fluid != Fluids.EMPTY) {
					BlockState s = fluid.defaultFluidState().createLegacyBlock();

					if (s.getBlock() != Blocks.AIR) {
						level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, s), pos.getX() + 0.5D, pos.getY() + 0.475D, pos.getZ() + 0.5D, 0D, -1D, 0D);
						foundParticle = true;
					}
				}
			}

			if (!foundParticle) {
				level.addParticle(ParticleTypes.DRIPPING_WATER, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0D, 0D, 0D);
			}
		}
	}

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		BlockPos posBelow = pos;
		BlockState blockBelow;

		do {
			posBelow = posBelow.below();

			if (posBelow.getY() == level.getMinBuildHeight()) {
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
