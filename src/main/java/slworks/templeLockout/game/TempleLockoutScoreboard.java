package slworks.templeLockout.game;

import org.bukkit.plugin.Plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import slworks.synlinkGames.API.scoreboard.GameScoreboard;
import slworks.synlinkGames.API.scoreboard.ScoreboardEntry;

public class TempleLockoutScoreboard extends GameScoreboard {

    public TempleLockoutScoreboard(Plugin plugin) {
        super(plugin);

        gameName = Component.text("Temple Lockout");

        addTime();
        addInfoLine(ScoreboardEntry.SUB_TIME, Component.text("下次缩圈: ", NamedTextColor.RED), Component.text("00: 00"));
        addBlankLine();
        addAliveTeams();
        addAlivePlayers();
    }

    public void updateSubTime(Component prefix, int remainingSeconds) {
        String time = formatTime(remainingSeconds);
        updateInfoLine(ScoreboardEntry.SUB_TIME, prefix, Component.text(time));
    }
}
