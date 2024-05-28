package org.reims.industrial_integration.gui.utils;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.reims.industrial_integration.Industrial_Integration;

import java.util.List;
import java.util.stream.Collectors;

public class MachineInterfaceData {
    public final String UID, displayedName;
    public final ResourceLocation BACKGROUND;
    public final int backgroundWidth, backgroundHeight;
    public @Nullable final Progressbar progressbar;
    public final int slotsCount;
    public final List<MachineSlot> slots;
    public @Nullable final EnergyBar energyBar;

    public MachineInterfaceData(String uid, String displayedName, String background, int backgroundWidth, int backgroundHeight,
                                @Nullable Progressbar progressbar, int slotsCount, List<MachineSlot> slots, @Nullable EnergyBar energyBar) {
        UID = uid;
        this.displayedName = displayedName;
        BACKGROUND = new ResourceLocation(Industrial_Integration.MODID, background);
        this.slotsCount = slotsCount;
        this.backgroundWidth = backgroundWidth;
        this.backgroundHeight = backgroundHeight;
        this.progressbar = progressbar;
        this.slots = slots;
        this.energyBar = energyBar;
    }

    public List<MachineSlot> getTypedSlots(MachineSlot.SlotType slotType) {
        return slots.stream()
                .filter(slot -> slot.type == slotType)
                .collect(Collectors.toList());
    }
}
