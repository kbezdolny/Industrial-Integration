package org.reims.industrial_integration.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.reims.industrial_integration.gui.menu.CompressorMenu;
import org.reims.industrial_integration.recipe.CompressorRecipe;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;

import java.util.Optional;

public class CompressorBlockEntity extends AbstractMachineWithEnergyBlockEntity {
    public CompressorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.COMPRESSOR.get(), pPos, pBlockState, MachineInterfaces.COMPRESSOR);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        super.createMenu(pContainerId, pPlayerInventory, pPlayer);
        return new CompressorMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, AbstractMachineBlockEntity blockEntity) {
        CompressorBlockEntity.customTick(level, blockPos, state, blockEntity, CompressorBlockEntity::craftLogic);
    }

    private static void craftLogic(Level level, BlockPos blockPos, BlockState state, AbstractMachineBlockEntity abstractEntity,
                                   SimpleContainer inventory) {
        CompressorBlockEntity blockEntity = (CompressorBlockEntity) abstractEntity;
        Optional<CompressorRecipe> recipe = getRecipe(level, inventory);

        if (hasRecipe(inventory, recipe, MachineInterfaces.COMPRESSOR)) {
            if (!hasEnoughEnergy(blockEntity, recipe.get().getEnergyReq())) {
                return;
            }

            int craftSpeed = recipe.get().getCraftDuration();
            if (blockEntity.maxProgress != craftSpeed){
                blockEntity.maxProgress = craftSpeed;
                blockEntity.resetProgress();
            }

            blockEntity.progress++;
            extractEnergy(blockEntity, recipe.get().getEnergyReq());
            setChanged(level, blockPos, state);

            if (blockEntity.progress >= blockEntity.maxProgress) {
                craftItem(blockEntity, inventory, recipe, MachineInterfaces.COMPRESSOR);
            }
        } else {
            blockEntity.resetProgress();
            setChanged(level, blockPos, state);
        }
    }

    private static Optional<CompressorRecipe> getRecipe(Level level, SimpleContainer inventory) {
        return level.getRecipeManager().getRecipeFor(CompressorRecipe.Type.INSTANCE, inventory, level);
    }
}
