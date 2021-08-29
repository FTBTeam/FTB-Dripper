package dev.ftb.mods.ftbdripper.item;

import dev.ftb.mods.ftbdripper.FTBDripper;
import dev.ftb.mods.ftbdripper.block.FTBDripperBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class FTBDripperItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, FTBDripper.MOD_ID);

	public static RegistryObject<BlockItem> blockItem(String id, Supplier<Block> sup) {
		return REGISTRY.register(id, () -> new BlockItem(sup.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	}

	public static final RegistryObject<Item> WATER_BOWL = REGISTRY.register("water_bowl", WaterBowlItem::new);

	public static final RegistryObject<BlockItem> DRIPPER = blockItem("dripper", FTBDripperBlocks.DRIPPER);
}