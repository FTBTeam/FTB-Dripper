package dev.ftb.mods.ftbdripper.jei;


import dev.ftb.mods.ftbdripper.FTBDripper;
import dev.ftb.mods.ftbdripper.item.FTBDripperItems;
import dev.ftb.mods.ftbdripper.recipe.DripRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author LatvianModder
 */
public class DripperCategory implements IRecipeCategory<DripRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(FTBDripper.MOD_ID + ":drip");

	private final IDrawable background;
	private final IDrawable icon;

	public DripperCategory(IGuiHelper guiHelper) {
		background = guiHelper.drawableBuilder(new ResourceLocation(FTBDripper.MOD_ID + ":textures/gui/drip_jei.png"), 0, 0, 91, 30).setTextureSize(128, 64).build();
		icon = guiHelper.createDrawableIngredient(new ItemStack(FTBDripperItems.DRIPPER.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends DripRecipe> getRecipeClass() {
		return DripRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.get("block." + FTBDripper.MOD_ID + ".dripper");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(DripRecipe recipe, IIngredients ingredients) {
		ingredients.setOutput(VanillaTypes.ITEM, recipe.outputItem);
		ingredients.setInput(VanillaTypes.ITEM, recipe.inputItem);
		ingredients.setInput(VanillaTypes.FLUID, new FluidStack(recipe.fluid, FluidAttributes.BUCKET_VOLUME));
	}

	@Override
	public void setRecipe(IRecipeLayout layout, DripRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = layout.getItemStacks();
		IGuiFluidStackGroup fluidStacks = layout.getFluidStacks();
		itemStacks.init(0, false, 67, 6);
		itemStacks.init(1, true, 22, 6);
		fluidStacks.init(0, true, 3, 7);
		itemStacks.set(ingredients);
		fluidStacks.set(ingredients);

		if (recipe.chance < 1D) {
			itemStacks.addTooltipCallback((idx, input, stack, tooltip) -> {
				if (idx == 0) {
					String s = String.valueOf(recipe.chance * 100D);
					tooltip.add(new TextComponent("Chance: " + (s.endsWith(".0") ? s.substring(0, s.length() - 2) : s) + "%").withStyle(ChatFormatting.YELLOW));
				}
			});
		}
	}
}
