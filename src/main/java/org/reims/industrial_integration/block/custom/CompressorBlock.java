package org.reims.industrial_integration.block.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.reims.industrial_integration.block.entity.CompressorBlockEntity;
import org.reims.industrial_integration.block.entity.ModBlockEntities;

public class CompressorBlock extends AbstractMachineBlock {

    public CompressorBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public BlockEntityType<CompressorBlockEntity> getRegistryBlockEntityType() {
        return ModBlockEntities.COMPRESSOR.get();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CompressorBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, getRegistryBlockEntityType(), CompressorBlockEntity::tick);
    }
}
