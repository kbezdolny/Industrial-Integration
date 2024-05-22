package org.reims.industrial_integration.gui.utils;

import java.util.ArrayList;

public class MachineInterfaces {
    public static final MachineInterfaceData COMPRESSOR =
            new MachineInterfaceData("compressor", "Compressor",
                    "textures/gui/compressor_gui.png", 176, 166,
                    new Progressbar(176, 0, 69, 38, 20, 13, Progressbar.AnimationDirection.Right),
                    2, new ArrayList<>() {{
                        add(new MachineSlot(0, 44, 36, MachineSlot.SlotType.INPUT));
                        add(new MachineSlot(1, 98, 36, MachineSlot.SlotType.OUTPUT));
                    }},
                    new EnergyBar(243, 0, 145, 20, 13, 48, 16000)
            );

    public static final MachineInterfaceData SLICER =
            new MachineInterfaceData("slicer", "Slicer",
                    "textures/gui/slicer_gui.png", 176, 166,
                    new Progressbar(176, 0, 69, 39, 19, 11, Progressbar.AnimationDirection.Right),
                    2, new ArrayList<>() {{
                add(new MachineSlot(0, 44, 36, MachineSlot.SlotType.INPUT));
                add(new MachineSlot(1, 98, 36, MachineSlot.SlotType.OUTPUT));
            }},
                    new EnergyBar(243, 0, 145, 20, 13, 48, 10000)
            );
}
