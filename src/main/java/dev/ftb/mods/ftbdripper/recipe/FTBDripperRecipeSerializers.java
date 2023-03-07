package dev.ftb.mods.ftbdripper.recipe;

import dev.ftb.mods.ftbdripper.FTBDripper;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class FTBDripperRecipeSerializers {
	public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FTBDripper.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> REGISTRY_RECIPE_TYPE = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, FTBDripper.MOD_ID);

	public static final RegistryObject<RecipeSerializer<?>> DRIP = REGISTRY.register("drip", DripRecipeSerializer::new);
	public static final RegistryObject<RecipeType<DripRecipe>> DRIP_TYPE = REGISTRY_RECIPE_TYPE.register("drip", () -> new RecipeType<>() {});
}
