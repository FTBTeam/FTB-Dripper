package dev.ftb.mods.ftbdripper.jei;

import dev.ftb.mods.ftbdripper.FTBDripper;
import dev.ftb.mods.ftbdripper.recipe.DripRecipe;
import mezz.jei.api.recipe.RecipeType;

public class RecipeTypes {
    public static final RecipeType<DripRecipe> DRIP = RecipeType.create(FTBDripper.MOD_ID, "drip", DripRecipe.class);
}
