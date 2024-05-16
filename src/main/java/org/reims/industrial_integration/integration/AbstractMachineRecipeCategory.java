package org.reims.industrial_integration.integration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.reims.industrial_integration.block.ModBlocks;
import org.reims.industrial_integration.gui.utils.EnergyBar;
import org.reims.industrial_integration.gui.utils.MachineInterfaceData;
import org.reims.industrial_integration.gui.utils.Progressbar;
import org.reims.industrial_integration.recipe.AbstractMachineRecipe;


public abstract class AbstractMachineRecipeCategory<R extends AbstractMachineRecipe> implements IRecipeCategory<R> {
    public final MachineInterfaceData machineData;
    private final ResourceLocation TEXTURE;

    private final IDrawable background;
    private final IDrawable icon;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    private final LoadingCache<Integer, IDrawableAnimated> cachedEnergyBar;
    protected final int offset = 4;

    public AbstractMachineRecipeCategory(IGuiHelper helper, MachineInterfaceData machineData) {
        this.machineData = machineData;
        TEXTURE = machineData.BACKGROUND;
        this.background = helper.createDrawable(TEXTURE, offset, offset, machineData.backgroundWidth-(offset*2), 85-offset);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.STATION_BLOCK.get()));
        this.cachedArrows = CacheBuilder.newBuilder().build(new CacheLoader<>() {
            @Override
            public IDrawableAnimated load(Integer cookTime) {
                Progressbar progressbar = machineData.progressbar;
                IDrawableAnimated.StartDirection direction = IDrawableAnimated.StartDirection.LEFT;
                if (progressbar.direction == Progressbar.AnimationDirection.Down) {
                    direction = IDrawableAnimated.StartDirection.TOP;
                }

                return helper.drawableBuilder(TEXTURE, progressbar.offsetX, progressbar.offsetY, progressbar.width, progressbar.height)
                        .buildAnimated(cookTime, direction, false);
            }
        });

        this.cachedEnergyBar = CacheBuilder.newBuilder().build(new CacheLoader<>() {
            @Override
            public IDrawableAnimated load(Integer cookTime) {
                EnergyBar energyBar = machineData.energyBar;
                return helper.drawableBuilder(TEXTURE, energyBar.offsetX, energyBar.offsetY, energyBar.width, energyBar.height)
                        .buildAnimated(cookTime, IDrawableAnimated.StartDirection.TOP, true);
            }
        });
    }

    @Override
    public Component getTitle() {
        return Component.literal(machineData.displayedName);
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    protected IDrawableAnimated getProgressbar() {
        return this.cachedArrows.getUnchecked(78); // 78 animation speed
    }

    protected IDrawableAnimated getEnergyBar() {
        return this.cachedEnergyBar.getUnchecked(240); // 240 animation speed
    }

    @Override
    public void draw(R recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Progressbar progressbarData = machineData.progressbar;
        getProgressbar().draw(stack, progressbarData.startX-offset, progressbarData.startY-offset);

        EnergyBar energyBarData = machineData.energyBar;
        getEnergyBar().draw(stack, energyBarData.startX-offset, energyBarData.startY-offset);
    }
}

