package mols.johannes;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class SettingsManager {

    private Gson gson;

    private MonitorConfiguration[] config;
    private Settings settings;

    SettingsManager() throws IOException {
        gson = new Gson();

        // Verify that directory exists
        Path directory = Paths.get(System.getProperty("user.home"), "diy-adalight");
        if (!Files.isDirectory(directory)) {
            Files.createDirectory(directory);
        }

        // Verify that files exist
        Path configFile = Paths.get(directory.toString(), "monitor_configuration.json");
        Path settingsFile = Paths.get(directory.toString(), "config.json");
        if (!Files.exists(configFile)) {
            Files.createFile(configFile);
        }
        if (!Files.exists(settingsFile)) {
            Files.createFile(settingsFile);
        }

        // Read files
        try {
            JsonReader reader = new JsonReader(new FileReader(configFile.toFile()));
            config = gson.fromJson(reader, MonitorConfiguration[].class);

            reader = new JsonReader(new FileReader(settingsFile.toFile()));
            settings = gson.fromJson(reader, Settings.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            config = new MonitorConfiguration[] { };
            settings = new Settings();
        }
    }

    MonitorConfiguration[] getConfig() {
        return config;
    }

    Settings getSettings() {
        return settings;
    }
}
