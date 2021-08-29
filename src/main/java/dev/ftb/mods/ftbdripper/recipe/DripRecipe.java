package dev.ftb.mods.ftbdripper.recipe;

import com.mojang.brigadier.StringReader;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class DripRecipe implements Recipe<NoInventory> {
	private final ResourceLocation id;
	private final String group;
	private String inputString;
	private Block input;
	private Map<Property<?>, Comparable<?>> inputProperties;
	public ItemStack inputItem;
	private String outputString;
	public BlockState output;
	public ItemStack outputItem;
	public Fluid fluid;
	public double chance;

	public DripRecipe(ResourceLocation i, String g) {
		id = i;
		group = g;
		inputString = "";
		input = Blocks.AIR;
		inputProperties = Collections.emptyMap();
		inputItem = ItemStack.EMPTY;
		outputString = "";
		output = Blocks.AIR.defaultBlockState();
		outputItem = ItemStack.EMPTY;
		fluid = Fluids.WATER;
		chance = 1D;
	}

	public void setInputString(String s) {
		inputString = s;

		try {
			BlockStateParser parser = new BlockStateParser(new StringReader(inputString), false).parse(false);
			input = Objects.requireNonNull(parser.getState()).getBlock();
			inputProperties = parser.getProperties();
		} catch (Exception ex) {
			ex.printStackTrace();
			input = Blocks.AIR;
			inputProperties = Collections.emptyMap();
		}

		if (inputItem.isEmpty()) {
			inputItem = input.asItem().getDefaultInstance();
		}
	}

	public String getInputString() {
		return inputString;
	}

	public void setOutputString(String s) {
		outputString = s;

		try {
			BlockStateParser parser = new BlockStateParser(new StringReader(outputString), false).parse(false);
			output = Objects.requireNonNull(parser.getState());
		} catch (Exception ex) {
			ex.printStackTrace();
			output = Blocks.AIR.defaultBlockState();
		}

		if (outputItem.isEmpty()) {
			outputItem = output.getBlock().asItem().getDefaultInstance();
		}
	}

	public String getOutputString() {
		return outputString;
	}

	public boolean testInput(BlockState state) {
		if (input == Blocks.AIR || input != state.getBlock()) {
			return false;
		}

		for (Map.Entry<Property<?>, Comparable<?>> entry : inputProperties.entrySet()) {
			if (!state.getValue(entry.getKey()).equals(entry.getValue())) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean matches(NoInventory inv, Level world) {
		return true;
	}

	@Override
	public ItemStack assemble(NoInventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return FTBDripperRecipeSerializers.DRIP.get();
	}

	@Override
	public RecipeType<?> getType() {
		return FTBDripperRecipeSerializers.DRIP_TYPE;
	}
}