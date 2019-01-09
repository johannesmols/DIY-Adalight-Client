package mols.johannes;

public class Settings {

    private int refreshRateInMilliSeconds, resolution;
    private String ip;
    private int port;

    public Settings() {
        refreshRateInMilliSeconds = 1;
        resolution = 1;
        ip = "127.0.0.1";
        port = 5000;
    }

    public int getRefreshRateInMilliSeconds() {
        return refreshRateInMilliSeconds;
    }

    public void setRefreshRateInMilliSeconds(int refreshRateInMilliSeconds) {
        this.refreshRateInMilliSeconds = refreshRateInMilliSeconds;
    }

    public int getResolution() {
        return resolution;
    }
    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
