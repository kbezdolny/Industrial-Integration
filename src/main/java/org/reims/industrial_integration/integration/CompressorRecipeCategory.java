package org.reims.industrial_integration.integration;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;
import org.reims.industrial_integration.gui.utils.MachineSlot;
import org.reims.industrial_integration.recipe.CompressorRecipe;

public class CompressorRecipeCategory extends AbstractMachineRecipeCategory<CompressorRecipe> {
    static {
        machineData = MachineInterfaces.COMPRESSOR;
    }

    public CompressorRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public RecipeType<CompressorRecipe> getRecipeType() {
        return JEIIIPlugin.COMPRESSOR_TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CompressorRecipe stationRecipe, IFocusGroup iFocusGroup) {
        // TODO add auto generating
        MachineSlot inputSlot = machineData.slots.get(0);
        MachineSlot outputSlot = machineData.slots.get(1);

        builder.addSlot(RecipeIngredientRole.INPUT, inputSlot.posX-offset, inputSlot.posY-offset)
                .addIngredients(stationRecipe.getIngredients().get(inputSlot.index));
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlot.posX-offset, outputSlot.posY-offset)
                .addItemStack(stationRecipe.getResultItem());
    }
}
