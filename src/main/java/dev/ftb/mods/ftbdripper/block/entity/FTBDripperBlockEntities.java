package dev.ftb.mods.ftbdripper.block.entity;

import dev.ftb.mods.ftbdripper.FTBDripper;
import dev.ftb.mods.ftbdripper.block.FTBDripperBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class FTBDripperBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, FTBDripper.MOD_ID);

	public static final Supplier<BlockEntityType<DripperBlockEntity>> DRIPPER = REGISTRY.register("dripper", () -> BlockEntityType.Builder.of(DripperBlockEntity::new, FTBDripperBlocks.DRIPPER.get()).build(null));
}
