package dev.ftb.mods.ftbdripper.recipe;

import dev.ftb.mods.ftbdripper.FTBDripper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class FTBDripperRecipeSerializers {
	public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FTBDripper.MOD_ID);

	public static final Supplier<RecipeSerializer<?>> DRIP = REGISTRY.register("drip", DripRecipeSerializer::new);
	public static final RecipeType<DripRecipe> DRIP_TYPE = RecipeType.register(FTBDripper.MOD_ID + ":drip");
}