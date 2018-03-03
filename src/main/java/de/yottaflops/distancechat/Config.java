package de.yottaflops.distancechat;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

class Config {
    int distanceObfuscatingStarts = 50;
    int distanceTooQuietStarts = 75;
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    /**
     * Creates a new config
     *
     * @param configPath The path where the config will be created
     */
    Config(Path configPath) {
        loader = HoconConfigurationLoader.builder().setPath(configPath).build();
        loadConfig();
        saveConfig();
    }

    /**
     * Load the data from the config
     */
    private void loadConfig() {
        ConfigurationNode rootNode;
        try {
            rootNode = loader.load();

            distanceObfuscatingStarts = rootNode.getNode("distanceToStartObfuscating").getInt();
            distanceTooQuietStarts = rootNode.getNode("distanceWhereTooQuietStarts").getInt();
        } catch(IOException ignored) {}
    }

    /**
     * Save the data to the config
     */
    private void saveConfig() {
        ConfigurationNode rootNode = loader.createEmptyNode();
        rootNode.getNode("distanceToStartObfuscating").setValue(distanceObfuscatingStarts);
        rootNode.getNode("distanceWhereTooQuietStarts").setValue(distanceTooQuietStarts);
        try {
            loader.save(rootNode);
        } catch (IOException ignored) {}
    }

    /**
     * Reload the config
     */
    public void reload() {
        loadConfig();
    }
}
