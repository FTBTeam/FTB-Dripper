package dev.ftb.mods.ftbdripper;

import dev.ftb.mods.ftbdripper.block.FTBDripperBlocks;
import dev.ftb.mods.ftbdripper.block.entity.FTBDripperBlockEntities;
import dev.ftb.mods.ftbdripper.item.FTBDripperItems;
import dev.ftb.mods.ftbdripper.item.WaterBowlItem;
import dev.ftb.mods.ftbdripper.recipe.FTBDripperRecipeSerializers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * @author LatvianModder
 */
@Mod(FTBDripper.MOD_ID)
@Mod.EventBusSubscriber(modid = FTBDripper.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FTBDripper {
	public static final String MOD_ID = "ftbdripper";

	public static int consumedFluid = 50;

	public FTBDripper() {
		FTBDripperBlocks.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		FTBDripperItems.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		FTBDripperBlockEntities.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		FTBDripperRecipeSerializers.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	@SubscribeEvent
	public static void itemRightClick(PlayerInteractEvent.RightClickItem event) {
		if (event.getItemStack().getItem() == Items.BOWL && WaterBowlItem.fillBowl(event.getWorld(), event.getPlayer(), event.getHand())) {
			event.getItemStack().shrink(1);

			if (!event.getWorld().isClientSide()) {
				ItemStack stack = new ItemStack(FTBDripperItems.WATER_BOWL.get());
				stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElse(null).fill(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME / 4), IFluidHandler.FluidAction.EXECUTE);
				ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), stack, event.getPlayer().inventory.selected);
			}

			event.getPlayer().swing(event.getHand());
			event.setCancellationResult(InteractionResult.SUCCESS);
			event.setCanceled(true);
		}
	}
}