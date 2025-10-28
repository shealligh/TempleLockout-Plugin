package slworks.templeLockout;

import org.bukkit.plugin.java.JavaPlugin;

import slworks.templeLockout.arena.TempleLockoutArena;
import slworks.templeLockout.command.TempleLockoutCommand;
import slworks.templeLockout.game.TempleLockoutConfigManager;
import slworks.templeLockout.game.TempleLockoutCore;
import slworks.templeLockout.game.TempleLockoutScoreboard;

public final class TempleLockout extends JavaPlugin {

    private static TempleLockout INSTANCE;

    public static TempleLockout getInstance() {
        return INSTANCE;
    }

    // private TempleLockoutConfigManager configManager;
    private TempleLockoutCore game;
    // private TempleLockoutScoreboard scoreboard;
    private TempleLockoutArena arena;
    private TempleLockoutCommand command;


    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        // configManager = new TempleLockoutConfigManager(this);

        reload();

        command = new TempleLockoutCommand(this);
        getCommand("templelockout").setExecutor(command);
        getCommand("templelockout").setTabCompleter(command);
    }

    public void unload() {
        if (game != null) {
            game.forceEndGame();
            // game.reset();
            // game.cleanup();
        }
        if (arena != null) {
            arena.reset();
        }
        // if (scoreboard != null) {
        //     scoreboard.reset();
        // }
    }

    public void reload() {
        unload();
        game = new TempleLockoutCore(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        unload();
    }


    // public TempleLockoutConfigManager getConfigManager() {
    //     return configManager;
    // }

    public TempleLockoutCore getGame() {
        return game;
    }

    // public TempleLockoutScoreboard getScoreboard() {
    //     return scoreboard;
    // }

    public TempleLockoutArena getArena() {
        return arena;
    }
}
