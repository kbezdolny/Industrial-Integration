package org.reims.industrial_integration.integration;

import mezz.jei.api.helpers.IGuiHelper;
import org.reims.industrial_integration.block.custom.ModBlocks;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;
import org.reims.industrial_integration.recipe.CompressorRecipe;

public class CompressorRecipeCategory extends AbstractMachineRecipeCategory<CompressorRecipe> {
    public CompressorRecipeCategory(IGuiHelper helper) {
        super(helper, MachineInterfaces.COMPRESSOR, JEIIIPlugin.COMPRESSOR_TYPE, ModBlocks.COMPRESSOR_BLOCK.get());
    }
}
