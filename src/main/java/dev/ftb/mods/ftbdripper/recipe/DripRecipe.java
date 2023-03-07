package dev.ftb.mods.ftbdripper.recipe;

import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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

import java.util.Map;

/**
 * @author LatvianModder
 */
public class DripRecipe implements Recipe<NoInventory> {
	private final ResourceLocation id;
	private final String group;
	private final String inputString;
	private final Block inputBlock;
	private final Map<Property<?>, Comparable<?>> inputProperties;
	private final String outputString;
	private final BlockState outputState;
	private final Fluid fluid;
	private final double chance;

	public DripRecipe(ResourceLocation id, String group, String inputString, String outputString, Fluid fluid, double chance) {
		this.id = id;
		this.group = group;
		this.inputString = inputString;
		this.outputString = outputString;
		this.fluid = fluid;
		this.chance = Mth.clamp(chance, 0.0D, 1.0D);

		try {
			BlockStateParser.BlockResult blockResult = BlockStateParser.parseForBlock(Registry.BLOCK, new StringReader(inputString), false);
			inputBlock = blockResult.blockState().getBlock();
			inputProperties = blockResult.properties();
		} catch (CommandSyntaxException e) {
			throw new JsonSyntaxException(e);
		}

		try {
			BlockStateParser.BlockResult blockResult = BlockStateParser.parseForBlock(Registry.BLOCK, new StringReader(outputString), false);
			outputState = blockResult.blockState();
		} catch (CommandSyntaxException e) {
			throw new JsonSyntaxException(e);
		}
	}

	public String getInputString() {
		return inputString;
	}

	public String getOutputString() {
		return outputString;
	}

	public ItemStack getInputItem() {
		return inputBlock.asItem().getDefaultInstance();
	}

	public ItemStack getOutputItem() {
		return outputState.getBlock().asItem().getDefaultInstance();
	}

	public BlockState getOutputState() {
		return outputState;
	}

	public Fluid getFluid() {
		return fluid;
	}

	public double getChance() {
		return chance;
	}

	public boolean testInput(BlockState state) {
		if (inputBlock == Blocks.AIR || inputBlock != state.getBlock()) {
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
		return FTBDripperRecipeSerializers.DRIP_TYPE.get();
	}
}
