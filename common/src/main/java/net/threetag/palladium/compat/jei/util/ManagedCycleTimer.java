package net.threetag.palladium.compat.jei.util;

public class ManagedCycleTimer extends CycleTimer {

    private boolean isRunning = false;
    private boolean paused = false;

    public ManagedCycleTimer(int offset) {
        super(offset);
    }

    @Override
    public void onDraw() {
        if (!isRunning) {
            return;
        }
        super.onDraw();
    }

    @Override
    public int getValue(int size) {
        if (!isRunning) {
            return 0;
        }
        return super.getValue(size);
    }

    @Override
    public boolean shouldPause() {
        return super.shouldPause() || paused;
    }

    public void start() {
        isRunning = true;

        startTime = drawTime = System.currentTimeMillis();
    }

    public void stop() {
        isRunning = false;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isPaused() {
        return paused;
    }
}
