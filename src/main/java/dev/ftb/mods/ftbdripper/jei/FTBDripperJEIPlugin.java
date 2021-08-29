package dev.ftb.mods.ftbdripper.jei;

import dev.ftb.mods.ftbdripper.FTBDripper;
import dev.ftb.mods.ftbdripper.item.FTBDripperItems;
import dev.ftb.mods.ftbdripper.recipe.FTBDripperRecipeSerializers;
import dev.ftb.mods.ftbdripper.recipe.NoInventory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author LatvianModder
 */
@JeiPlugin
public class FTBDripperJEIPlugin implements IModPlugin {
	public static IJeiRuntime RUNTIME;

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(FTBDripper.MOD_ID + ":jei");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime r) {
		RUNTIME = r;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration r) {
		r.addRecipeCatalyst(new ItemStack(FTBDripperItems.DRIPPER.get()), DripperCategory.UID);
	}

	@Override
	public void registerRecipes(IRecipeRegistration r) {
		Level level = Minecraft.getInstance().level;
		r.addRecipes(level.getRecipeManager().getRecipesFor(FTBDripperRecipeSerializers.DRIP_TYPE, NoInventory.INSTANCE, level), DripperCategory.UID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration r) {
		r.addRecipeCategories(new DripperCategory(r.getJeiHelpers().getGuiHelper()));
	}
}