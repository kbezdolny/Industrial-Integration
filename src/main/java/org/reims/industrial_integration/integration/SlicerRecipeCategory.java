package org.reims.industrial_integration.integration;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import org.reims.industrial_integration.block.ModBlocks;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;
import org.reims.industrial_integration.recipe.SlicerRecipe;

public class SlicerRecipeCategory extends AbstractMachineRecipeCategory<SlicerRecipe> {

    public SlicerRecipeCategory(IGuiHelper helper) {
        super(helper, MachineInterfaces.SLICER, ModBlocks.SLICER_BLOCK.get());
    }

    @Override
    public RecipeType<SlicerRecipe> getRecipeType() {
        return JEIIIPlugin.SLICER_TYPE;
    }
}
