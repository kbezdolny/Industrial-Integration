package org.reims.industrial_integration.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.reims.industrial_integration.gui.menu.SlicerMenu;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;
import org.reims.industrial_integration.recipe.AbstractMachineRecipe;
import org.reims.industrial_integration.recipe.SlicerRecipe;

import java.util.Optional;

public class SlicerBlockEntity extends AbstractMachineWithEnergyBlockEntity {
    public SlicerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SLICER.get(), pPos, pBlockState, MachineInterfaces.SLICER);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        super.createMenu(pContainerId, pPlayerInventory, pPlayer);
        return new SlicerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, AbstractMachineBlockEntity blockEntity) {
        SlicerBlockEntity.customTick(level, blockPos, state, blockEntity, SlicerBlockEntity::craftLogic);
    }

    private static void craftLogic(Level level, BlockPos blockPos, BlockState state, AbstractMachineBlockEntity abstractEntity,
                                   SimpleContainer inventory) {
        SlicerBlockEntity blockEntity = (SlicerBlockEntity) abstractEntity;
        Optional<SlicerRecipe> recipe = getRecipe(level, inventory);
        
        if (hasRecipe(inventory, recipe)) {
            if (!hasEnoughEnergy(blockEntity, recipe.get().getEnergyReq())) {
                return;
            }

            int craftSpeed = recipe.get().getCraftSpeed();
            if (blockEntity.maxProgress != craftSpeed){
                blockEntity.maxProgress = craftSpeed;
                blockEntity.resetProgress();
            }

            blockEntity.progress++;
            extractEnergy(blockEntity, recipe.get().getEnergyReq());
            setChanged(level, blockPos, state);

            if (blockEntity.progress >= blockEntity.maxProgress) {
                craftItem(blockEntity, inventory, recipe);
            }
        } else {
            blockEntity.resetProgress();
            setChanged(level, blockPos, state);
        }
    }

    private static void craftItem(SlicerBlockEntity blockEntity, SimpleContainer inventory, Optional<SlicerRecipe> recipe) {
        if (hasRecipe(inventory, recipe)) {
            // TODO Take custom input item count
            blockEntity.itemHandler.extractItem(MachineInterfaces.SLICER.slots.get(0).index, 1, false);
            blockEntity.itemHandler.setStackInSlot(MachineInterfaces.SLICER.slots.get(1).index, new ItemStack(recipe.get().getResultItem().getItem(),
                    blockEntity.itemHandler.getStackInSlot(MachineInterfaces.SLICER.slots.get(1).index).getCount() + recipe.get().getOutputCount()));

            blockEntity.resetProgress();
        }
    }

    private static Optional<SlicerRecipe> getRecipe(Level level, SimpleContainer inventory) {
        return level.getRecipeManager().getRecipeFor(SlicerRecipe.Type.INSTANCE, inventory, level);
    }

    private static boolean hasRecipe(SimpleContainer inventory, Optional<? extends AbstractMachineRecipe> recipe) {
        int outputSlot = MachineInterfaces.SLICER.slots.get(1).index;
        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory, outputSlot)
                && canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem(), outputSlot);
    }
}
