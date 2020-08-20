package com.example.pmu_projekat;

public class ChestUtility {

    private long databaseID;
    private long time;
    private int interfacePosition;
    private boolean isOpened = false;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isReady()
    {
        return time <= 0;
    }

    public long getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(long databaseID) {
        this.databaseID = databaseID;
    }

    public int getInterfacePosition() {
        return interfacePosition;
    }

    public void setInterfacePosition(int interfacePosition) {
        this.interfacePosition = interfacePosition;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }
}
