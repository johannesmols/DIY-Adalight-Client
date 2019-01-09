package mols.johannes;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private MonitorConfiguration[] monitorConfigurations;
    private Settings settings;

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        SettingsManager settingsManager = new SettingsManager();
        main.monitorConfigurations = settingsManager.getConfig();
        main.settings = settingsManager.getSettings();

        if (main.settings == null) {
            System.err.println("Faulty configuration file");
            System.exit(1);
        }

        Runnable runnable = main::update;
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, main.settings.getRefreshRateInMilliSeconds(), TimeUnit.MILLISECONDS);
    }

    /**
     * Update the LEDs by capturing all screens, calculating the average colors of each tile and sending that data to the server
     */
    private void update() {
        ArrayList<BufferedImage> screenshots = screenshotEachMonitor();

        for (int i = 0; i < screenshots.size(); i++) {
            if (monitorConfigurations.length != screenshots.size()) {
                System.err.println("Not every monitor has a valid configuration");
                System.exit(1);
            }

            BufferedImage image = screenshots.get(i);
            MonitorConfiguration config = monitorConfigurations[i];

            ArrayList<Color> top_from_left_to_right = new ArrayList<>();
            ArrayList<Color> bottom_from_left_to_right = new ArrayList<>();
            ArrayList<Color> left_from_top_to_bottom = new ArrayList<>();
            ArrayList<Color> right_from_top_to_bottom = new ArrayList<>();

            // Calculate average for each tile
            int top_tile_width = image.getWidth() / config.getTop();
            int bottom_tile_width = image.getWidth() / config.getBottom();
            int left_tile_height = image.getHeight() / config.getLeft();
            int right_tile_height = image.getHeight() / config.getRight();
            int horizontal_offset_top_bottom = (int) ((float) image.getHeight() * config.getOffset_top_bottom_pct());
            int vertical_offset_left_right = (int) ((float) image.getWidth() * config.getOffset_left_right_pct());

            for (int t = 0; t < config.getTop(); t++) {
                top_from_left_to_right.add(averageColor(image, top_tile_width * t, 0, top_tile_width, horizontal_offset_top_bottom, settings.getResolution()));
            }

            for (int b = 0; b < config.getBottom(); b++) {
                bottom_from_left_to_right.add(averageColor(image, top_tile_width * b, image.getHeight() - horizontal_offset_top_bottom, bottom_tile_width, horizontal_offset_top_bottom, settings.getResolution()));
            }

            for (int l = 0; l < config.getLeft(); l++) {
                left_from_top_to_bottom.add(averageColor(image, 0, left_tile_height * l, vertical_offset_left_right, left_tile_height, settings.getResolution()));
            }

            for (int r = 0; r < config.getRight(); r++) {
                right_from_top_to_bottom.add(averageColor(image, image.getWidth() - vertical_offset_left_right, right_tile_height * r, vertical_offset_left_right, right_tile_height, settings.getResolution()));
            }

            // Send information to the server
            postUpdate(i, top_from_left_to_right, bottom_from_left_to_right, left_from_top_to_bottom, right_from_top_to_bottom, config.getOrder(), config.getInverted());
        }
    }

    /***
     * Post an update to the HTTP API running on the Raspberry Pi
     * @param monitor the monitor number
     * @param top the colors on the top
     * @param bottom the colors on the bottom
     * @param left the colors on the left
     * @param right the colors on the right
     * @param order the order of how the sides are wired
     * @param inverted whether or not the list should be inverted if the wiring goes the opposite way of the logical calculations
     */
    private void postUpdate(int monitor, ArrayList<Color> top, ArrayList<Color> bottom, ArrayList<Color> left, ArrayList<Color> right, String[] order, boolean[] inverted) {
        try {
            String url = "http://" + settings.getIp() + ":" + settings.getPort() + "/update";
            Gson gson = new Gson();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            StringEntity postingString = new StringEntity(gson.toJson(
                    new PostRequest(monitor, prepareColorArrayList(top), prepareColorArrayList(bottom), prepareColorArrayList(left), prepareColorArrayList(right), order, inverted)));
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            System.out.println(response.getStatusLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Convert Color Lists to nested lists so they can be converted into JSON by GSON
     * @param list the list in question
     * @return the nested list
     */
    private List<List<Integer>> prepareColorArrayList(ArrayList<Color> list) {
        List<List<Integer>> preparedList = new ArrayList<>();
        for (Color color : list) {
            List<Integer> values = new ArrayList<>();
            values.add(color.getRed());
            values.add(color.getGreen());
            values.add(color.getBlue());
            preparedList.add(values);
        }

        return preparedList;
    }

    /**
     * Take a screenshot of each connected monitor
     * Note: On Linux Mint 19, the desktop is somehow considered to be black even with a desktop wallpaper
     * @return a list of screenshots for each connected monitor
     */
    private ArrayList<BufferedImage> screenshotEachMonitor() {
        ArrayList<BufferedImage> screenshots = new ArrayList<>();
        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) { // Screenshot each monitor
            try {
                Rectangle bounds = gd.getDefaultConfiguration().getBounds();
                BufferedImage capture = new Robot().createScreenCapture(bounds);
                screenshots.add(capture);
            } catch (AWTException e) {
                e.printStackTrace();
                return screenshots;
            }
        }
        return screenshots;
    }

    /**
     * Calculate the average color of an image
     * @param image the image
     * @param topLeftX the top left corner of the rectangle to measure in, x coordinate
     * @param topLeftY the top left corner of the rectangle to measure in, y coordinate
     * @param width the width of the area to measure in
     * @param height the height of the area to measure in
     * @return the average color of the measured range in the image
     */
    private Color averageColor(BufferedImage image, int topLeftX, int topLeftY, int width, int height, int resolution) {
        int bottomRightX = topLeftX + width;
        int bottomRightY = topLeftY + height;
        long sumRed = 0, sumGreen = 0, sumBlue = 0;
        for (int x = topLeftX; x < bottomRightX; x++) {
            for (int y = topLeftY; y < bottomRightY; y++) {
                Color pixel = new Color(image.getRGB(x, y));
                sumRed += pixel.getRed();
                sumGreen += pixel.getGreen();
                sumBlue += pixel.getBlue();
            }
        }
        long pixels = width * height;
        return new Color((int) (sumRed / pixels), (int) (sumGreen / pixels), (int) (sumBlue / pixels));
    }
}
