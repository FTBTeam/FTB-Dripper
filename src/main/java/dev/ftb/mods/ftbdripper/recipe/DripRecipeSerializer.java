package dev.ftb.mods.ftbdripper.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * @author LatvianModder
 */
public class DripRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<DripRecipe> {
	@Override
	public DripRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		DripRecipe r = new DripRecipe(recipeId, json.has("group") ? json.get("group").getAsString() : "");

		if (json.has("inputBlock")) {
			r.setInputString(json.get("inputBlock").getAsString());
		}

		if (json.has("inputItem")) {
			if (json.get("inputItem").isJsonObject()) {
				r.inputItem = ShapedRecipe.itemFromJson(json.get("inputItem").getAsJsonObject());
			} else {
				r.inputItem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("inputItem").getAsString())));
			}
		}

		if (json.has("outputBlock")) {
			r.setOutputString(json.get("outputBlock").getAsString());
		}

		if (json.has("outputItem")) {
			if (json.get("outputItem").isJsonObject()) {
				r.outputItem = ShapedRecipe.itemFromJson(json.get("outputItem").getAsJsonObject());
			} else {
				r.outputItem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("outputItem").getAsString())));
			}
		}

		if (json.has("fluid")) {
			r.fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(json.get("fluid").getAsString()));
		}

		if (json.has("chance")) {
			r.chance = json.get("chance").getAsDouble();
		}

		return r;
	}

	@Override
	public DripRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		DripRecipe r = new DripRecipe(recipeId, buffer.readUtf(Short.MAX_VALUE));
		r.setInputString(buffer.readUtf(Short.MAX_VALUE));
		r.inputItem = buffer.readItem();
		r.setOutputString(buffer.readUtf(Short.MAX_VALUE));
		r.outputItem = buffer.readItem();
		r.fluid = ForgeRegistries.FLUIDS.getValue(buffer.readResourceLocation());
		r.chance = buffer.readDouble();
		return r;
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, DripRecipe r) {
		buffer.writeUtf(r.getGroup(), Short.MAX_VALUE);
		buffer.writeUtf(r.getInputString(), Short.MAX_VALUE);
		buffer.writeItem(r.inputItem);
		buffer.writeUtf(r.getOutputString(), Short.MAX_VALUE);
		buffer.writeItem(r.outputItem);
		buffer.writeResourceLocation(r.fluid.getRegistryName());
		buffer.writeDouble(r.chance);
	}
}
