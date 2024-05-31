package org.reims.industrial_integration.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reims.industrial_integration.block.custom.AbstractMachineBlock;
import org.reims.industrial_integration.block.custom.StationBlock;
import org.reims.industrial_integration.gui.utils.MachineInterfaceData;
import org.reims.industrial_integration.gui.utils.MachineSlot;
import org.reims.industrial_integration.recipe.AbstractMachineRecipe;
import org.reims.industrial_integration.util.RecipeDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractMachineBlockEntity extends BlockEntity implements MenuProvider {
    @FunctionalInterface
    interface TickConsumer<T, U, V, I, O> {
        void accept(T t, U u, V v, I i, O o);
    }

    public final MachineInterfaceData machineData;
    protected final int defaultSpeed = 78;

    protected ItemStackHandler itemHandler;
    protected LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData data;
    protected int progress = 0;
    protected int maxProgress = defaultSpeed;

    public AbstractMachineBlockEntity(BlockEntityType<? extends AbstractMachineBlockEntity> pType,
                                      BlockPos pPos, BlockState pBlockState,
                                      MachineInterfaceData machineData) {
        super(pType, pPos, pBlockState);
        this.machineData = machineData;
        itemHandler = new ItemStackHandler(machineData.slotsCount) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                for (int i = 0; i < machineData.slotsCount; i++) {
                    if (i == slot && machineData.slots.get(i).type == MachineSlot.SlotType.INPUT) {
                        return true;
                    }
                }
                return false;
            }
        };

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AbstractMachineBlockEntity.this.progress;
                    case 1 -> AbstractMachineBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AbstractMachineBlockEntity.this.progress = value;
                    case 1 -> AbstractMachineBlockEntity.this.maxProgress = value;
                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    // TODO change this
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1, (i, s) -> false)),
                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1, (i, s) -> false)),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 0,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> itemHandler.isItemValid(0, stack))));

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap != ForgeCapabilities.ITEM_HANDLER) {
            return super.getCapability(cap, side);
        }

        if (side == null) {
            return lazyItemHandler.cast();
        }

        if (!directionWrappedHandlerMap.containsKey(side)) {
            return super.getCapability(cap, side);
        }

        Direction localDir = this.getBlockState().getValue(StationBlock.FACING);
        if(side == Direction.UP || side == Direction.DOWN) {
            return directionWrappedHandlerMap.get(side).cast();
        }

        return switch (localDir) {
            default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
            case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
            case SOUTH -> directionWrappedHandlerMap.get(side).cast();
            case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
        };
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        // TODO localisation ( Component.translatable() )
        return Component.literal(machineData.displayedName);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt(machineData.UID + ".progress", this.progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt(machineData.UID + ".progress");
    }

    public void drops() {
        SimpleContainer container = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < container.getContainerSize(); i++) {
            container.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.getBlockPos(), container);
    }

    protected void resetProgress() {
        this.progress = 0;
    }

    protected boolean isCrafting() {
        return this.progress > 0;
    }

    protected static void craftItem(AbstractMachineBlockEntity blockEntity, SimpleContainer inventory,
                                    Optional<? extends AbstractMachineRecipe> recipe, MachineInterfaceData machineData) {
        if (hasRecipe(inventory, recipe, machineData)) {
            List<MachineSlot> inputSlots = machineData.getTypedSlots(MachineSlot.SlotType.INPUT);
            for (MachineSlot slot : inputSlots) {
                blockEntity.itemHandler.extractItem(slot.index, recipe.get().getRecipeItems().get(slot.index).itemCount, false);
            }

            List<RecipeDto.ItemInterface> resultItems = recipe.get().getResultItems();
            List<MachineSlot> outputSlots = machineData.getTypedSlots(MachineSlot.SlotType.OUTPUT);

            if (resultItems == null) {
                return;
            }

            int nextFreeSlotIndex = outputSlots.get(0).index;
            for (RecipeDto.ItemInterface resultItem : resultItems) {
                int nextSlotIndex = nextFreeSlotIndex;
                int itemCount = resultItem.itemCount;
                for (MachineSlot slot : outputSlots) {
                    if (!canInsertItemIntoOutputSlot(inventory, resultItem.itemStack, nextSlotIndex)) {
                        if (isLastSlot(nextSlotIndex, outputSlots.get(outputSlots.size()-1).index)) {
                            break;
                        }

                        nextSlotIndex++;
                        continue;
                    }

                    if (!canInsertAmountIntoOutputSlot(inventory, nextSlotIndex, itemCount)) {
                        if (isLastSlot(nextSlotIndex, outputSlots.get(outputSlots.size()-1).index)) {
                            break;
                        }

                        int tempCount = inventory.getItem(nextSlotIndex).getMaxStackSize() - inventory.getItem(nextSlotIndex).getCount();
                        if (tempCount > 0) {
                            itemCount -= tempCount;
                            blockEntity.itemHandler.setStackInSlot(nextSlotIndex, new ItemStack(resultItem.itemStack.copy().getItem(),
                                    blockEntity.itemHandler.getStackInSlot(nextSlotIndex).getCount() + tempCount));
                        }
                        nextSlotIndex++;
                        continue;
                    }

                    blockEntity.itemHandler.setStackInSlot(nextSlotIndex, new ItemStack(resultItem.itemStack.copy().getItem(),
                            blockEntity.itemHandler.getStackInSlot(nextSlotIndex).getCount() + itemCount));
                    nextFreeSlotIndex++;
                    break;
                }

            }

            blockEntity.resetProgress();
        }
    }

    protected static boolean hasRecipe(SimpleContainer inventory, Optional<? extends AbstractMachineRecipe> recipe, MachineInterfaceData machineData) {
        if (recipe.isEmpty()) {
            return false;
        }

        List<MachineSlot> inputSlots = machineData.getTypedSlots(MachineSlot.SlotType.INPUT);
        for (MachineSlot slot : inputSlots) {
            if (!enoughInputItem(inventory, recipe.get(), slot.index)) {
                return false;
            }
        }

        List<MachineSlot> outputSlots = machineData.getTypedSlots(MachineSlot.SlotType.OUTPUT);
        List<RecipeDto.ItemInterface> resultItems = recipe.get().getResultItems();

        if (resultItems == null) {
            return false;
        }

        if (getFullSlotsCount(inventory) > outputSlots.size()-resultItems.size()) {
            return false;
        }

        for (RecipeDto.ItemInterface resultItem : resultItems) {
            int nextFreeSlotIndex = outputSlots.get(0).index;
            int itemCount = resultItem.itemCount;
            for (int i = 0; i < outputSlots.size(); i++) {
                if (!canInsertItemIntoOutputSlot(inventory, resultItem.itemStack, nextFreeSlotIndex)) {
                    if (isLastSlot(nextFreeSlotIndex, outputSlots.get(outputSlots.size()-1).index)) {
                        return false;
                    }

                    nextFreeSlotIndex++;
                    continue;
                }

                if (!canInsertAmountIntoOutputSlot(inventory, nextFreeSlotIndex, itemCount)) {
                    if (isLastSlot(nextFreeSlotIndex, outputSlots.get(outputSlots.size()-1).index)) {
                        return false;
                    }

                    itemCount -= inventory.getItem(nextFreeSlotIndex).getMaxStackSize() - inventory.getItem(nextFreeSlotIndex).getCount();
                    nextFreeSlotIndex++;
                    continue;
                }

                break;
            }
        }

        return true;
    }

    private static boolean isLastSlot(int slot, int lastSlot) {
        return slot == lastSlot;
    }

    protected static SimpleContainer getInventory(AbstractMachineBlockEntity blockEntity) {
        SimpleContainer inventory = new SimpleContainer(blockEntity.itemHandler.getSlots());
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inventory.setItem(i, blockEntity.itemHandler.getStackInSlot(i));
        }

        return inventory;
    }

    protected static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack itemStack, int slot) {
        return inventory.getItem(slot).getItem() == itemStack.getItem() || inventory.getItem(slot).isEmpty();
    }

    protected static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory, int slot, int resultItemCount) {
        return inventory.getItem(slot).getMaxStackSize() >= inventory.getItem(slot).getCount() + resultItemCount;
    }

    protected static boolean enoughInputItem(SimpleContainer inventory, AbstractMachineRecipe recipe, int ingredientSlot) {
        return inventory.getItem(ingredientSlot).getCount() >= recipe.getRecipeItems().get(ingredientSlot).itemCount;
    }

    protected static int getFullSlotsCount(SimpleContainer inventory) {
        int fullSlotsCount = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).getCount() == inventory.getMaxStackSize()) {
                fullSlotsCount++;
            }
        }

        return fullSlotsCount;
    }

    public static void customTick(Level level, BlockPos blockPos, BlockState state,
                                  AbstractMachineBlockEntity blockEntity,
                                  TickConsumer<Level, BlockPos, BlockState, AbstractMachineBlockEntity, SimpleContainer> craftLogic) {
        if (level.isClientSide()) {
            return;
        }

        boolean flag = blockEntity.isCrafting();

        SimpleContainer inventory = getInventory(blockEntity);
        craftLogic.accept(level, blockPos, state, blockEntity, inventory);

        if (flag != blockEntity.isCrafting()) {
            state = state.setValue(AbstractMachineBlock.CRAFTING, blockEntity.isCrafting());
            level.setBlock(blockPos, state, 2);
            setChanged(level, blockPos, state);
        }
    }
}
