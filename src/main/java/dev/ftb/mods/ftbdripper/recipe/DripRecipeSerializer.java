package dev.ftb.mods.ftbdripper.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

/**
 * @author LatvianModder
 */
public class DripRecipeSerializer implements RecipeSerializer<DripRecipe> {
	private static void requireField(JsonObject json, String field) {
		if (!json.has(field)) throw new JsonSyntaxException(String.format("recipe is missing '%s' field!", field));
	}

	@Override
	public DripRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		requireField(json, "inputBlock");
		requireField(json, "outputBlock");
		requireField(json, "fluid");
		requireField(json, "chance");

		String group = json.has("group") ? json.get("group").getAsString() : "";
		String inputString = json.get("inputBlock").getAsString();
		String outputString = json.get("outputBlock").getAsString();
		String fluidName = json.get("fluid").getAsString();
		double chance = json.has("chance") ? json.get("chance").getAsDouble() : 1.0D;

		Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName));
		if (fluid == null || fluid == Fluids.EMPTY) throw new JsonSyntaxException("unknown fluid '" + fluidName + "' !");

		return new DripRecipe(recipeId, group, inputString, outputString, fluid, chance);
	}

	@Override
	public DripRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		String group = buffer.readUtf(Short.MAX_VALUE);
		String inputString = buffer.readUtf(Short.MAX_VALUE);
		String outputString = buffer.readUtf(Short.MAX_VALUE);
		ResourceLocation fluidName = buffer.readResourceLocation();
		double chance = buffer.readDouble();

		Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidName);
		if (fluid == null) {
			// shouldn't happen unless client is out of sync with server...
			fluid = Fluids.EMPTY;
		}

		return new DripRecipe(recipeId, group, inputString, outputString, fluid, chance);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, DripRecipe r) {
		buffer.writeUtf(r.getGroup(), Short.MAX_VALUE);
		buffer.writeUtf(r.getInputString(), Short.MAX_VALUE);
		buffer.writeUtf(r.getOutputString(), Short.MAX_VALUE);
		buffer.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(r.getFluid())));
		buffer.writeDouble(r.getChance());
	}
}
