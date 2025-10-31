package slworks.templeLockout.game;

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
    MapView view = Bukkit.createMap(WorldManager.getWorld(WorldManager.TEMPLE_LOCKOUT_WORLD));
    {
        view.setScale(Scale.CLOSEST);
        view.setLocked(true);
        view.setTrackingPosition(false); 
        view.setUnlimitedTracking(false);
        view.setCenterX(0);
        view.setCenterZ(0);

        view.getRenderers().forEach(view::removeRenderer);
    }

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
        view.addRenderer(TempleLockout.getInstance().getArena().getRenderer());
        super.startGame();
    }

    @Override
    protected void initializeTimeline() {
        Phase pregamePhase = PhaseFactory.createPregamePhase(this, 10000);
        pregamePhase.setOnStart(() -> {
//            MapUpdateListener listener = new MapUpdateListener();
//            Bukkit.getPluginManager().registerEvents(listener, plugin);
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
                player.give(getMap(player));
                
            }
            for (Player player : PlayerUtil.getSpectators()) {
                spectators.add(player);
            }

            TempleLockout.getInstance().getArena().teleportPlayersToSpawns();
            // playerManager.alivePlayers.clear();
        });

        addPhase(pregamePhase);
    }

    @Override
    protected void onGameEnd() {
    }

    public ItemStack getMap(Player player) {
        ItemStack map = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        // view.setScale(Scale.CLOSEST);
        // view.setLocked(true);
        // view.view.setTrackingPosition(false); 
        // view.setUnlimitedTracking(false);
        mapMeta.setMapView(view);
        mapMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        map.setItemMeta(mapMeta);

        return map;
    }

}
