package mols.johannes;

public class Settings {

    private int refreshRateInSeconds;

    public Settings() {
        refreshRateInSeconds = 1;
    }

    public int getRefreshRateInSeconds() {
        return refreshRateInSeconds;
    }

    public void setRefreshRateInSeconds(int refreshRateInSeconds) {
        this.refreshRateInSeconds = refreshRateInSeconds;
    }
}
