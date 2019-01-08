package mols.johannes;

public class Settings {

    private int refreshRateInSeconds, resolution;

    public Settings() {
        refreshRateInSeconds = 1;
        resolution = 1;
    }

    public int getRefreshRateInSeconds() {
        return refreshRateInSeconds;
    }

    public void setRefreshRateInSeconds(int refreshRateInSeconds) {
        this.refreshRateInSeconds = refreshRateInSeconds;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }
}
