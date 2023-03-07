package dev.ftb.mods.ftbdripper.jei;

import dev.ftb.mods.ftbdripper.FTBDripper;
import dev.ftb.mods.ftbdripper.item.FTBDripperItems;
import dev.ftb.mods.ftbdripper.recipe.DripRecipe;
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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Objects;

/**
 * @author LatvianModder
 */
@JeiPlugin
public class FTBDripperJEIPlugin implements IModPlugin {
	private static final ResourceLocation ID = new ResourceLocation(FTBDripper.MOD_ID, "jei");

	static IJeiRuntime RUNTIME;

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime r) {
		RUNTIME = r;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration r) {
		r.addRecipeCatalyst(new ItemStack(FTBDripperItems.DRIPPER.get()), RecipeTypes.DRIP);
	}

	@Override
	public void registerRecipes(IRecipeRegistration r) {
		Level level = Objects.requireNonNull(Minecraft.getInstance().level);

		r.addRecipes(RecipeTypes.DRIP, level.getRecipeManager().getRecipesFor(FTBDripperRecipeSerializers.DRIP_TYPE.get(), NoInventory.INSTANCE, level));
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration r) {
		r.addRecipeCategories(new DripperCategory(r.getJeiHelpers().getGuiHelper()));
	}
}
