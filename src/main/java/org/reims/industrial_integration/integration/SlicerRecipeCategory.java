package org.reims.industrial_integration.integration;

import mezz.jei.api.helpers.IGuiHelper;
import org.reims.industrial_integration.block.custom.ModBlocks;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;
import org.reims.industrial_integration.recipe.SlicerRecipe;

public class SlicerRecipeCategory extends AbstractMachineRecipeCategory<SlicerRecipe> {
    public SlicerRecipeCategory(IGuiHelper helper) {
        super(helper, MachineInterfaces.SLICER, JEIIIPlugin.SLICER_TYPE, ModBlocks.SLICER_BLOCK.get());
    }
}
