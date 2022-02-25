package dev.ftb.mods.ftbdripper.jei;


import dev.ftb.mods.ftbdripper.FTBDripper;
import dev.ftb.mods.ftbdripper.item.FTBDripperItems;
import dev.ftb.mods.ftbdripper.recipe.DripRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * @author LatvianModder
 */
public class DripperCategory implements IRecipeCategory<DripRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(FTBDripper.MOD_ID + ":drip");

	private final Component title;
	private final IDrawable background;
	private final IDrawable icon;

	public DripperCategory(IGuiHelper guiHelper) {
		title = new TranslatableComponent("block." + FTBDripper.MOD_ID + ".dripper");
		background = guiHelper.drawableBuilder(new ResourceLocation(FTBDripper.MOD_ID + ":textures/gui/drip_jei.png"), 0, 0, 91, 30).setTextureSize(128, 64).build();
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(FTBDripperItems.DRIPPER.get()));
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
	public Component getTitle() {
		return title;
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
	public void setRecipe(IRecipeLayoutBuilder builder, DripRecipe recipe, List<? extends IFocus<?>> focuses) {
		builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 7)
				.addItemStack(recipe.outputItem)
		;

		builder.addSlot(RecipeIngredientRole.INPUT, 23, 7)
				.addIngredient(VanillaTypes.ITEM, recipe.inputItem)
		;

		builder.addSlot(RecipeIngredientRole.INPUT, 3, 7)
				.addIngredient(VanillaTypes.FLUID, new FluidStack(recipe.fluid, FluidAttributes.BUCKET_VOLUME))
				.addTooltipCallback((recipeSlotView, list) -> {
					if (recipe.chance < 1D) {
						String s = String.valueOf(recipe.chance * 100D);
						list.add(new TextComponent("Chance: " + (s.endsWith(".0") ? s.substring(0, s.length() - 2) : s) + "%").withStyle(ChatFormatting.YELLOW));
					}
				})
		;
	}
}
