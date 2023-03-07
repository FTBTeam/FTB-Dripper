package dev.ftb.mods.ftbdripper.block;

import dev.ftb.mods.ftbdripper.FTBDripper;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class FTBDripperBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, FTBDripper.MOD_ID);

	public static final RegistryObject<Block> DRIPPER = REGISTRY.register("dripper", DripperBlock::new);

}
