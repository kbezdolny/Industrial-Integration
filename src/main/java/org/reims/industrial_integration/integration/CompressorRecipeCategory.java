package org.reims.industrial_integration.integration;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import org.reims.industrial_integration.block.ModBlocks;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;
import org.reims.industrial_integration.recipe.CompressorRecipe;

public class CompressorRecipeCategory extends AbstractMachineRecipeCategory<CompressorRecipe> {

    public CompressorRecipeCategory(IGuiHelper helper) {
        super(helper, MachineInterfaces.COMPRESSOR, ModBlocks.COMPRESSOR_BLOCK.get());
    }

    @Override
    public RecipeType<CompressorRecipe> getRecipeType() {
        return JEIIIPlugin.COMPRESSOR_TYPE;
    }
}
