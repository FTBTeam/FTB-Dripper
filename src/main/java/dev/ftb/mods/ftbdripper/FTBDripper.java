package dev.ftb.mods.ftbdripper;

import dev.ftb.mods.ftbdripper.block.FTBDripperBlocks;
import dev.ftb.mods.ftbdripper.block.entity.FTBDripperBlockEntities;
import dev.ftb.mods.ftbdripper.item.FTBDripperItems;
import dev.ftb.mods.ftbdripper.item.WaterBowlItem;
import dev.ftb.mods.ftbdripper.recipe.FTBDripperRecipeSerializers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * @author LatvianModder
 */
@Mod(FTBDripper.MOD_ID)
public class FTBDripper {
	public static final String MOD_ID = "ftbdripper";

	public static int consumedFluid = 50;

	public FTBDripper() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		FTBDripperBlocks.REGISTRY.register(modEventBus);
		FTBDripperItems.REGISTRY.register(modEventBus);
		FTBDripperBlockEntities.REGISTRY.register(modEventBus);
		FTBDripperRecipeSerializers.REGISTRY.register(modEventBus);
		FTBDripperRecipeSerializers.REGISTRY_RECIPE_TYPE.register(modEventBus);

		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.addListener(this::itemRightClick);
	}

	public void itemRightClick(PlayerInteractEvent.RightClickItem event) {
		Player player = event.getEntity();
		if (event.getItemStack().getItem() == Items.BOWL && WaterBowlItem.fillBowl(event.getLevel(), player)) {
			event.getItemStack().shrink(1);

			if (!event.getLevel().isClientSide()) {
				ItemStack stack = new ItemStack(FTBDripperItems.WATER_BOWL.get());
				ItemHandlerHelper.giveItemToPlayer(player, stack, player.getInventory().selected);
			}

			player.swing(event.getHand());
			event.setCancellationResult(InteractionResult.SUCCESS);
			event.setCanceled(true);
		}
	}
}
