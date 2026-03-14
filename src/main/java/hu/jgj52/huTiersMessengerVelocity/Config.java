package hu.jgj52.huTiersMessengerVelocity;

import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.nio.file.Files;
import java.util.Map;;

public class Config {

    private final File configFile;
    private Map<String, Object> config;

    public Config(String pluginId) {
        File folder = new File("plugins/" + pluginId);
        if (!folder.exists()) folder.mkdirs();

        configFile = new File(folder, "config.yml");

        if (!configFile.exists()) {
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml")) {
                if (in != null) {
                    Files.copy(in, configFile.toPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        load();
    }

    public void load() {
        Yaml yaml = new Yaml();
        try (InputStream in = new FileInputStream(configFile)) {
            config = yaml.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public Object get(String key) {
        return config.get(key);
    }
}