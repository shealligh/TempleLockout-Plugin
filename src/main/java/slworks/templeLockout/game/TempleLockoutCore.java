package slworks.templeLockout.game;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import slworks.synlinkGames.API.game.GameCore;
import slworks.synlinkGames.API.game.Phase;
import slworks.synlinkGames.API.game.PhaseFactory;
import slworks.synlinkGames.API.player.PlayerUtil;
import slworks.templeLockout.TempleLockout;

public class TempleLockoutCore extends GameCore {

    private TempleLockoutConfigManager configManager;
    private TempleLockoutScoreboard scoreboard;

    public TempleLockoutCore(Plugin plugin) {
        super(plugin);
        configManager = new TempleLockoutConfigManager(plugin);
        configManager.setupCustomConfig();
        gameMode = configManager.getGameMode();

        initializeTimeline();

        scoreboard = new TempleLockoutScoreboard();
    }

    @Override
    protected void initializeTimeline() {
        Phase pregamePhase = PhaseFactory.createPregamePhase(this, 15);
        pregamePhase.setOnStart(() -> {
            scoreboard.initialize();
            for (Player player : PlayerUtil.getIngamePlayers()) {
                playerManager.addIngamePlayer(player);
                playerManager.addAlivePlayer(player);
                // player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getBaseValue());
                player.setHealth(20);
                player.setFoodLevel(20);
            }
            for (Player player : PlayerUtil.getSpectators()) {
                playerManager.addSpectator(player);
            }

            TempleLockout.getInstance().getArena().teleportPlayersToSpawns();
            // playerManager.alivePlayers.clear();
        });
    }   

    @Override
    protected void onGameEnd() {
    }

}
