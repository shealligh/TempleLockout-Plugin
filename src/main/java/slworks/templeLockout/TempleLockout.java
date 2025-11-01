package slworks.templeLockout;

import org.bukkit.plugin.java.JavaPlugin;

import slworks.synlinkGames.API.world.WorldUtil;
import slworks.synlinkGames.worlds.WorldManager;
import slworks.templeLockout.arena.TempleLockoutArena;
import slworks.templeLockout.command.TempleLockoutCommand;
import slworks.templeLockout.config.TempleLockoutConfigManager;
import slworks.templeLockout.game.TempleLockoutCore;

public final class TempleLockout extends JavaPlugin {

    private static TempleLockout INSTANCE;

    public static TempleLockout getInstance() {
        return INSTANCE;
    }

    private TempleLockoutConfigManager configManager;
    private TempleLockoutCore game;
    private TempleLockoutArena arena;
    private TempleLockoutCommand command;


    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        reload();

        command = new TempleLockoutCommand(this);
        getCommand("templelockout").setExecutor(command);
        getCommand("templelockout").setTabCompleter(command);
    }

    public void unload() {
        if (game != null) {
            game.forceEndGame();
        }
        if (arena != null) {
            arena.reset();
        }
    }

    public void reload() {
        unload();
        configManager = new TempleLockoutConfigManager(this);

        arena = new TempleLockoutArena(WorldUtil.getWorld(WorldManager.TEMPLE_LOCKOUT_WORLD));
        game = new TempleLockoutCore(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        unload();
    }

    public TempleLockoutConfigManager getConfigManager() {
        return configManager;
    }

    public TempleLockoutCore getGame() {
        return game;
    }

    public TempleLockoutArena getArena() {
        return arena;
    }
}
