package org.reims.industrial_integration.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.reims.industrial_integration.gui.menu.CompressorMenu;
import org.reims.industrial_integration.recipe.AbstractMachineRecipe;
import org.reims.industrial_integration.recipe.CompressorRecipe;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;

import java.util.Optional;

public class CompressorBlockEntity extends AbstractMachineWithEnergyBlockEntity {
    static {
        machineData = MachineInterfaces.COMPRESSOR;
    }

    private static final int inputSlotIndex = MachineInterfaces.COMPRESSOR.slots.get(0).index;
    private static final int outputSlotIndex = MachineInterfaces.COMPRESSOR.slots.get(1).index;

    public CompressorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.COMPRESSOR.get(), pPos, pBlockState);
    }

    @Nullable
    @Override
    public CompressorMenu createMenu(int id, Inventory inventory, Player player) {
        super.createMenu(id, inventory, player);
        return new CompressorMenu(id, inventory, this, this.data);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, AbstractMachineBlockEntity blockEntity) {
        CompressorBlockEntity.customTick(level, blockPos, state, blockEntity, CompressorBlockEntity::craftLogic);
    }

    private static void craftLogic(Level level, BlockPos blockPos, BlockState state, AbstractMachineBlockEntity abstractEntity,
                                   SimpleContainer inventory) {
        CompressorBlockEntity blockEntity = (CompressorBlockEntity) abstractEntity;
        Optional<CompressorRecipe> recipe = getRecipe(level, inventory);

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

    private static void craftItem(CompressorBlockEntity blockEntity, SimpleContainer inventory, Optional<CompressorRecipe> recipe) {
        if (hasRecipe(inventory, recipe)) {
            // TODO Take custom input item count
            blockEntity.itemHandler.extractItem(inputSlotIndex, 1, false);
            blockEntity.itemHandler.setStackInSlot(outputSlotIndex, new ItemStack(recipe.get().getResultItem().getItem(),
                    blockEntity.itemHandler.getStackInSlot(outputSlotIndex).getCount() + recipe.get().getOutputCount()));

            blockEntity.resetProgress();
        }
    }

    protected static Optional<CompressorRecipe> getRecipe(Level level, SimpleContainer inventory) {
        return level.getRecipeManager().getRecipeFor(CompressorRecipe.Type.INSTANCE, inventory, level);
    }

    protected static boolean hasRecipe(SimpleContainer inventory, Optional<? extends AbstractMachineRecipe> recipe) {
        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory, outputSlotIndex)
                && canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem(), outputSlotIndex);
    }
}
