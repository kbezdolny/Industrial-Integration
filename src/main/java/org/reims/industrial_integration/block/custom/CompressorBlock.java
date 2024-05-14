package org.reims.industrial_integration.block.custom;


import org.reims.industrial_integration.block.entity.CompressorBlockEntity;
import org.reims.industrial_integration.block.entity.ModBlockEntities;

public class CompressorBlock extends AbstractMachineBlock<CompressorBlockEntity> {
    public CompressorBlock(Properties pProperties) {
        super(pProperties, ModBlockEntities.COMPRESSOR.get(), CompressorBlockEntity::new, CompressorBlockEntity::tick);
    }
}
