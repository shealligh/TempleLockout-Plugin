package slworks.templeLockout.game;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import slworks.synlinkGames.API.game.GameCore;
import slworks.synlinkGames.API.game.Phase;
import slworks.synlinkGames.API.game.PhaseFactory;
import slworks.synlinkGames.API.player.PlayerUtil;
import slworks.synlinkGames.API.util.ItemUtils;
import slworks.templeLockout.TempleLockout;
import slworks.templeLockout.config.TempleLockoutConfigManager;

public class TempleLockoutCore extends GameCore {

    private TempleLockoutConfigManager configManager;
    private TempleLockoutScoreboard scoreboard;

    public TempleLockoutCore(Plugin plugin) {
        super(plugin);
        configManager = new TempleLockoutConfigManager(plugin);
        configManager.setupCustomConfig();
        gameMode = configManager.getGameMode();

        scoreboard = new TempleLockoutScoreboard(plugin);

        initializeTimeline();
    }

    @Override
    protected void initializeTimeline() {
        Phase pregamePhase = PhaseFactory.createPregamePhase(this, 15);
        pregamePhase.setOnStart(() -> {
            scoreboard.initialize();
            for (Player player : PlayerUtil.getIngamePlayers()) {
                ingamePlayers.add(player);
                alivePlayers.add(player);
                // player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getBaseValue());
                player.setHealth(20);
                player.setFoodLevel(20);
                player.give(ItemUtils.getTool(Material.STONE_PICKAXE));
                player.give(ItemUtils.getTool(Material.STONE_SWORD));
                player.give(ItemUtils.getTool(Material.BOW));
                player.give(ItemUtils.getItem(Material.ARROW, 8));
            }
            for (Player player : PlayerUtil.getSpectators()) {
                spectators.add(player);
            }

            TempleLockout.getInstance().getArena().teleportPlayersToSpawns();
            // playerManager.alivePlayers.clear();
        });
    }

    @Override
    protected void onGameEnd() {
    }

}
