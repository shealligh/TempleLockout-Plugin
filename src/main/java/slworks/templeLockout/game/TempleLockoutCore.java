package slworks.templeLockout.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.bukkit.plugin.Plugin;

import slworks.synlinkGames.API.game.GameCore;
import slworks.synlinkGames.API.game.Phase;
import slworks.synlinkGames.API.game.PhaseFactory;
import slworks.synlinkGames.API.player.PlayerUtil;
import slworks.synlinkGames.API.util.ItemUtils;
import slworks.synlinkGames.worlds.WorldManager;
import slworks.templeLockout.TempleLockout;
import slworks.templeLockout.listener.MapUpdateListener;
import slworks.templeLockout.util.TempleLockoutMapRenderer;

public class TempleLockoutCore extends GameCore {

    private TempleLockoutScoreboard scoreboard;

    public TempleLockoutCore(Plugin plugin) {
        super(plugin);
        gameMode = TempleLockout.getInstance().getConfigManager().getGameMode();

        scoreboard = new TempleLockoutScoreboard(plugin);

        initializeTimeline();
    }

    @Override
    public void startGame() {
        scoreboard.initialize();
        // TempleLockout.getInstance().getArena().reset();
        // for (Team team : TeamManager.getTeams()) {
        //     team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.FOR_OWN_TEAM);
        // }
        TempleLockout.getInstance().getArena().initialize();
        super.startGame();
    }

    @Override
    protected void initializeTimeline() {
        Phase pregamePhase = PhaseFactory.createPregamePhase(this, 12);
        pregamePhase.setOnStart(() -> {
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
                player.give(getMapItem());
            }
            for (Player player : PlayerUtil.getSpectators()) {
                spectators.add(player);
            }

            TempleLockout.getInstance().getArena().teleportPlayersToSpawns();
        });

        Phase ingamePhase = PhaseFactory.createIngamePhase(this, TempleLockout.getInstance().getConfigManager().getTotalTime());
        ingamePhase.addEventListener(new MapUpdateListener());
        ingamePhase.addOnRun(phaseTask -> ingameRunning(phaseTask.getRemainingSeconds()));
        // TODO: 最终决战阶段是无限长的阶段，，，可能得大改api，，，

        addPhase(pregamePhase);
        addPhase(ingamePhase);
    }

    public void ingameRunning(int remainingSeconds) {
        // TODO: 优化锕
        int j = 0;
        for (int i = 0; i < TempleLockout.getInstance().getConfigManager().getLayers() * 2; i++) {
            if (TempleLockout.getInstance().getConfigManager().getRemainingTime(i) < remainingSeconds) {
                j = i-1;
                break;
            }
        }
        Component subTimePrefix = Component.text("下次缩圈: ", NamedTextColor.RED);
        if (j % 2 == 1) {
            subTimePrefix = Component.text("正在缩圈: ", NamedTextColor.RED);
        }
        scoreboard.updateSubTime(subTimePrefix,  remainingSeconds - TempleLockout.getInstance().getConfigManager().getRemainingTime(j + 1));
    }

    private ItemStack getMapItem() {
        return TempleLockout.getInstance().getArena().getMapItem();
    }

    @Override
    protected void onGameEnd() {
        TempleLockout.getInstance().getArena().reset();
    }
}
