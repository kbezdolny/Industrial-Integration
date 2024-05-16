package org.reims.industrial_integration.block.custom;


import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.reims.industrial_integration.block.entity.CompressorBlockEntity;
import org.reims.industrial_integration.block.entity.ModBlockEntities;

public class CompressorBlock extends AbstractMachineBlock<CompressorBlockEntity> {

    public CompressorBlock(Properties pProperties) {
        super(pProperties, CompressorBlockEntity::new);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.COMPRESSOR.get(), CompressorBlockEntity::tick);
    }
}