package de.yottaflops.distancechat;

import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.Random;

public class Message {
    private String message;
    private double xSource;
    private double ySource;
    private double zSource;

    /**
     * Creates a message
     *
     * @param message The message
     * @param player The player who send the message
     */
    Message(String message, Player player) {
        this.message = message;
        this.xSource = player.getLocation().getX();
        this.ySource = player.getLocation().getY();
        this.zSource = player.getLocation().getZ();
    }

    /**
     * Returns the message for a player
     *
     * @param player The player that will/will not recieve the message
     * @param distanceObfuscationStarts The distance where the obfuscation starts
     * @param distanceTooQuietStarts The distance where the message no longer reaches the target
     * @return The message or nil if it's too far away
     */
    public Optional<String> getMessageFor(Player player, int distanceObfuscationStarts, int distanceTooQuietStarts) {
        double playerX = player.getLocation().getX();
        double playerY = player.getLocation().getY();
        double playerZ = player.getLocation().getZ();

        return getObfuscatedMessage(playerX - xSource, playerY - ySource, playerZ - zSource, distanceObfuscationStarts, distanceTooQuietStarts);
    }

    /**
     * Returns the message in an obfuscated way based on the relative distances
     *
     * @param deltaX Delta X
     * @param deltaY Delta Y
     * @param deltaZ Delta Z
     * @param distanceObfuscationStarts The distance where the obfuscation starts
     * @param distanceTooQuietStarts The distance where the message no longer reaches the target
     * @return The message or nil if it's too far away
     */
    private Optional<String> getObfuscatedMessage(double deltaX, double deltaY, double deltaZ, int distanceObfuscationStarts, int distanceTooQuietStarts) {
        double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY + deltaZ*deltaZ);

        if (distance <  distanceObfuscationStarts) {
            return Optional.of(message);
        } else if (distance > distanceTooQuietStarts) {
            return Optional.empty();
        } else {
            double obstructPercentage = (distance - distanceObfuscationStarts) / (distanceTooQuietStarts - distanceObfuscationStarts);

            String name = message.substring(1, message.indexOf(">"));
            String content = message.substring(message.indexOf(">") + 1, message.length());

            return Optional.of("<" + obfuscate(name, obstructPercentage/2) + ">" + obfuscate(content, obstructPercentage/2));
        }
    }

    /**
     * Obfuscates a string by replacing a certain percentage of characters with different ones
     *
     * @param input To be obfuscated
     * @param percentage The percentage of characters to be replaced
     * @return The obfuscated String
     */
    private String obfuscate(String input, double percentage) {
        String output = input;
        int charactersToObstruct = (int) Math.round(percentage * input.length());
        for (int i = 0; i < charactersToObstruct; i++) {
            Random r = new Random();
            char c = (char) (r.nextInt(70) + '!');
            int replaceIndex = r.nextInt(message.length() - 1);
            output = Helper.replaceCharacterAt(replaceIndex, c, output);
        }
        return output;
    }
}