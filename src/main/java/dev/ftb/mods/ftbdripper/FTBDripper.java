package dev.ftb.mods.ftbdripper;

import dev.ftb.mods.ftbdripper.block.FTBDripperBlocks;
import dev.ftb.mods.ftbdripper.block.entity.FTBDripperBlockEntities;
import dev.ftb.mods.ftbdripper.item.FTBDripperItems;
import dev.ftb.mods.ftbdripper.item.WaterBowlItem;
import dev.ftb.mods.ftbdripper.recipe.FTBDripperRecipeSerializers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
		if (event.getItemStack().getItem() == Items.BOWL && WaterBowlItem.DUMMY.use(event.getWorld(), event.getPlayer(), event.getHand()).getObject().getItem() == Items.WATER_BUCKET) {
			event.getItemStack().shrink(1);

			if (!event.getWorld().isClientSide()) {
				ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), new ItemStack(FTBDripperItems.WATER_BOWL.get()));
			}

			event.setCancellationResult(InteractionResult.SUCCESS);
			event.setCanceled(true);
		}
	}
}