package dev.ftb.mods.ftbdripper.jei;


import dev.ftb.mods.ftbdripper.FTBDripper;
import dev.ftb.mods.ftbdripper.item.FTBDripperItems;
import dev.ftb.mods.ftbdripper.recipe.DripRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

/**
 * @author LatvianModder
 */
public class DripperCategory implements IRecipeCategory<DripRecipe> {
	private final Component title;
	private final IDrawable background;
	private final IDrawable icon;

	public DripperCategory(IGuiHelper guiHelper) {
		title = Component.translatable("block." + FTBDripper.MOD_ID + ".dripper");
		background = guiHelper.drawableBuilder(new ResourceLocation(FTBDripper.MOD_ID,  "textures/gui/drip_jei.png"),
				0, 0, 91, 30).setTextureSize(128, 64).build();
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FTBDripperItems.DRIPPER.get()));
	}

	@Override
	public RecipeType<DripRecipe> getRecipeType() {
		return RecipeTypes.DRIP;
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
	public void setRecipe(IRecipeLayoutBuilder builder, DripRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 7)
				.addItemStack(recipe.getOutputItem());

		builder.addSlot(RecipeIngredientRole.INPUT, 23, 7)
				.addIngredient(VanillaTypes.ITEM_STACK, recipe.getInputItem());

		builder.addSlot(RecipeIngredientRole.INPUT, 3, 7)
				.addIngredient(ForgeTypes.FLUID_STACK, new FluidStack(recipe.getFluid(), FluidType.BUCKET_VOLUME))
				.addTooltipCallback((recipeSlotView, list) -> {
					if (recipe.getChance() < 1D) {
//						String s = String.valueOf(recipe.getChance() * 100D);
//						list.add(Component.literal("Chance: " + (s.endsWith(".0") ? s.substring(0, s.length() - 2) : s) + "%").withStyle(ChatFormatting.YELLOW));
						String pct = String.format("%.0f", recipe.getChance() * 100d);
						list.add(Component.translatable("ftbdripper.messages.chance", pct).withStyle(ChatFormatting.YELLOW));
					}
				});
	}
}
