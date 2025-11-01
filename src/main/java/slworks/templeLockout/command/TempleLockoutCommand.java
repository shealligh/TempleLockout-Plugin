package slworks.templeLockout.command;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import slworks.synlinkGames.API.command.GameCommand;
import slworks.templeLockout.TempleLockout;

public class TempleLockoutCommand extends GameCommand {

    public TempleLockoutCommand(Plugin plugin) {
        // super(plugin);
        // opSubcommands = List.of(
        //     "start",
        //     "stop"
        // ).toArray(String[]::new);
        opSubcommands = new String[] {
                "start",
                "end",
                "reload"
        };
        normalSubcommands = new String[] {};
    }

    @Override
    protected boolean executeCommand(String subcommand, Player sender) {
        return switch (subcommand.toLowerCase()) {
            case "start" -> handleStart(sender);
            case "end" -> handleEnd(sender);
            case "reload" -> handleReload(sender);
            default -> handleCommandNotFound(sender);
        };
    }

    @Override
    protected @Nullable boolean executeCommand(String subcommand, String[] subArgs, Player sender) {
        return handleCommandNotFound(sender);
    }

    private boolean handleStart(Player sender) {
        TempleLockout.getInstance().getGame().startGame();
        return true;
    }

    private boolean handleEnd(Player sender) {
        TempleLockout.getInstance().getGame().forceEndGame();
        return true;
    }

    private boolean handleReload(Player sender) {
        TempleLockout.getInstance().reload();
        return true;
    }
}
