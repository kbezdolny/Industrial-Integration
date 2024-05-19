package org.reims.industrial_integration.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.reims.industrial_integration.Industrial_Integration;
import org.reims.industrial_integration.block.ModBlocks;
import org.reims.industrial_integration.gui.screen.CompressorScreen;
import org.reims.industrial_integration.gui.screen.SlicerScreen;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;
import org.reims.industrial_integration.gui.utils.Progressbar;
import org.reims.industrial_integration.recipe.CompressorRecipe;
import org.reims.industrial_integration.recipe.SlicerRecipe;
import org.reims.industrial_integration.recipe.StationRecipe;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIIIPlugin implements IModPlugin {
    public static RecipeType<StationRecipe> STATION_TYPE =
            new RecipeType<>(StationRecipeCategory.UID, StationRecipe.class);

    public static RecipeType<CompressorRecipe> COMPRESSOR_TYPE =
            new RecipeType<>(getUID(MachineInterfaces.COMPRESSOR.UID), CompressorRecipe.class);

    public static RecipeType<SlicerRecipe> SLICER_TYPE =
            new RecipeType<>(getUID(MachineInterfaces.SLICER.UID), SlicerRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Industrial_Integration.MODID, "jei_plugin");
    }

    private static ResourceLocation getUID(String id) {
        return new ResourceLocation(Industrial_Integration.MODID, id);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                StationRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                CompressorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                SlicerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<StationRecipe> stationRecipesInfusing = rm.getAllRecipesFor(StationRecipe.Type.INSTANCE);
        registration.addRecipes(STATION_TYPE, stationRecipesInfusing);

        List<CompressorRecipe> compressorRecipesInfusing = rm.getAllRecipesFor(CompressorRecipe.Type.INSTANCE);
        registration.addRecipes(COMPRESSOR_TYPE, compressorRecipesInfusing);

        List<SlicerRecipe> slicerRecipesInfusing = rm.getAllRecipesFor(SlicerRecipe.Type.INSTANCE);
        registration.addRecipes(SLICER_TYPE, slicerRecipesInfusing);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        Progressbar compressor = MachineInterfaces.COMPRESSOR.progressbar;
        registration.addRecipeClickArea(CompressorScreen.class, compressor.startX, compressor.startY,
                compressor.width, compressor.height, JEIIIPlugin.COMPRESSOR_TYPE);

        Progressbar slicer = MachineInterfaces.SLICER.progressbar;
        registration.addRecipeClickArea(SlicerScreen.class, slicer.startX, slicer.startY,
                slicer.width, slicer.height, JEIIIPlugin.SLICER_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.COMPRESSOR_BLOCK.get()), JEIIIPlugin.COMPRESSOR_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SLICER_BLOCK.get()), JEIIIPlugin.SLICER_TYPE);
    }
}
