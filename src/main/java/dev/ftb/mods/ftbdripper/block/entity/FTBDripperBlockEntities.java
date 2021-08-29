package dev.ftb.mods.ftbdripper.block.entity;

import dev.ftb.mods.ftbdripper.FTBDripper;
import dev.ftb.mods.ftbdripper.block.FTBDripperBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class FTBDripperBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, FTBDripper.MOD_ID);

	public static final RegistryObject<BlockEntityType<DripperBlockEntity>> DRIPPER = REGISTRY.register("jar", () -> BlockEntityType.Builder.of(DripperBlockEntity::new, FTBDripperBlocks.DRIPPER.get()).build(null));
}