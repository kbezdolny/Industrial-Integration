package org.reims.industrial_integration.integration;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import org.reims.industrial_integration.block.ModBlocks;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;
import org.reims.industrial_integration.gui.utils.MachineSlot;
import org.reims.industrial_integration.recipe.SlicerRecipe;

public class SlicerRecipeCategory extends AbstractMachineRecipeCategory<SlicerRecipe> {

    public SlicerRecipeCategory(IGuiHelper helper) {
        super(helper, MachineInterfaces.SLICER, ModBlocks.SLICER_BLOCK.get());
    }

    @Override
    public RecipeType<SlicerRecipe> getRecipeType() {
        return JEIIIPlugin.SLICER_TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SlicerRecipe stationRecipe, IFocusGroup iFocusGroup) {
        // TODO add auto generating
        MachineSlot inputSlot = machineData.slots.get(0);
        MachineSlot outputSlot = machineData.slots.get(1);

        builder.addSlot(RecipeIngredientRole.INPUT, inputSlot.posX-offset, inputSlot.posY-offset)
                .addIngredients(stationRecipe.getIngredients().get(inputSlot.index));
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlot.posX-offset, outputSlot.posY-offset)
                .addItemStack(stationRecipe.getResultItem());
    }
}
