package net.threetag.palladium.compat.jei.util;

import net.minecraft.client.gui.screens.Screen;

// Copied and modified from JEI's CycleTimer
public class CycleTimer {

    /* the amount of time in ms to display one thing before cycling to the next one */
    private static final int cycleTime = 1000;
    protected long startTime;
    protected long drawTime;
    private long pausedDuration = 0;

    public CycleTimer(int offset) {
        long time = System.currentTimeMillis();
        this.startTime = time - ((long) offset * cycleTime);
        this.drawTime = time;
    }

    public int getValue(int size) {
        if (size == 0) {
            return 0;
        }
        long index = ((drawTime - startTime) / cycleTime) % size;
        return Math.toIntExact(index);
    }

    public void onDraw() {
        if (!shouldPause()) {
            if (pausedDuration > 0) {
                startTime += pausedDuration;
                pausedDuration = 0;
            }
            drawTime = System.currentTimeMillis();
        } else {
            pausedDuration = System.currentTimeMillis() - drawTime;
        }
    }

    public boolean shouldPause() {
        return Screen.hasShiftDown();
    }
}
