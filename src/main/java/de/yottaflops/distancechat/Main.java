package de.yottaflops.distancechat;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.nio.file.Path;
import java.util.Optional;

@Plugin(
        id = "distancechat",
        name = "DistanceChat",
        version = "1.0.0",
        description = "A plugin that obfuscates messages of players far away",
        authors = {"YottaFLOPS"}
)
public class Main {

    @Inject
    private Logger logger;

    private Config config;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        config = new Config(defaultConfig);
        logger.info("Loaded config");
    }

    /**
     * A message is sent
     *
     * @param event A MessageChannelEvent event
     */
    @Listener
    public void onMessageSent(MessageChannelEvent event) {
        Optional<Player> playerOptional = event.getCause().first(Player.class);
        if (playerOptional.isPresent()) {
            if (event.getClass().getSimpleName().equals("MessageChannelEvent$Chat$Impl")) {
                event.setMessageCancelled(true);
                Player player = playerOptional.get();
                Message message = new Message(event.getMessage().toPlain(), player);
                for (Player p : player.getWorld().getPlayers()) {
                    Optional<String> newMessage = message.getMessageFor(p, config.distanceObfuscatingStarts, config.distanceTooQuietStarts);
                    newMessage.ifPresent(s -> p.sendMessage(Text.of(s)));
                }
            }
        }
    }

    /**
     * Reload the config
     *
     * @param event The reload event
     */
   @Listener
   public void reload(GameReloadEvent event) {
       config.reload();
       logger.info("Reloaded config");
       Optional<Player> playerOptional = event.getCause().first(Player.class);
       playerOptional.ifPresent(player -> player.sendMessage(Text.of("DistanceChat reloaded config").toBuilder().color(TextColors.GRAY).toText()));
   }

    @Listener
    public void clientConnection(ClientConnectionEvent.Join event) {}

    @Listener
    public void clientConnection(ClientConnectionEvent.Login event) {}

    @Listener
    public void clientConnection(ClientConnectionEvent.Auth event) {}

    @Listener
    public void clientConnection(ClientConnectionEvent.Disconnect event) {}
}
