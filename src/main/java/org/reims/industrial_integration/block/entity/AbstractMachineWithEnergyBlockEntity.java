package org.reims.industrial_integration.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reims.industrial_integration.gui.utils.MachineInterfaceData;
import org.reims.industrial_integration.networking.ModMessages;
import org.reims.industrial_integration.networking.packet.EnergySyncS2CPacket;
import org.reims.industrial_integration.util.ModEnergyStorage;


public abstract class AbstractMachineWithEnergyBlockEntity extends AbstractMachineBlockEntity {
    protected int maxTransfer = 512;

    protected final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(machineData.energyBar.capacity, maxTransfer) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };

    public AbstractMachineWithEnergyBlockEntity(BlockEntityType<? extends AbstractMachineBlockEntity> pType, BlockPos pPos, BlockState pBlockState,
                                                MachineInterfaceData machineData) {
        super(pType, pPos, pBlockState, machineData);
    }

    private LazyOptional<IEnergyStorage> lazyEnergyStorage = LazyOptional.empty();

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        super.createMenu(pContainerId, pPlayerInventory, pPlayer);
        ENERGY_STORAGE.onEnergyChanged();
        return null;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyStorage.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyStorage = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyStorage.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt(machineData.UID + ".energy", ENERGY_STORAGE.getEnergyStored());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ENERGY_STORAGE.setEnergy(nbt.getInt(machineData.UID + ".energy"));
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        ENERGY_STORAGE.setEnergy(energy);
    }

    protected static void extractEnergy(AbstractMachineWithEnergyBlockEntity blockEntity, int energyReq) {
        blockEntity.ENERGY_STORAGE.extractEnergy(energyReq, false);
    }

    protected static boolean hasEnoughEnergy(AbstractMachineWithEnergyBlockEntity blockEntity, int energyReq) {
        return blockEntity.ENERGY_STORAGE.getEnergyStored() >= energyReq;
    }
}
