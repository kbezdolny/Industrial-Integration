package org.reims.industrial_integration.block.custom;


import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.reims.industrial_integration.block.entity.ModBlockEntities;
import org.reims.industrial_integration.block.entity.SlicerBlockEntity;

public class SlicerBlock extends AbstractMachineBlock<SlicerBlockEntity> {

    public SlicerBlock(Properties pProperties) {
        super(pProperties, SlicerBlockEntity::new);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.SLICER.get(), SlicerBlockEntity::tick);
    }
}