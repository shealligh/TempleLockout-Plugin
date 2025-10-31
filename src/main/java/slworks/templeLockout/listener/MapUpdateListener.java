package slworks.templeLockout.listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import net.kyori.adventure.text.format.NamedTextColor;
import slworks.synlinkGames.API.player.PlayerUtil;
import slworks.synlinkGames.API.util.Pair;
import slworks.templeLockout.TempleLockout;

public class MapUpdateListener implements Listener {

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent ev) {
        Block block = ev.getBlock();
        Player player = ev.getPlayer();
        if (!TempleLockout.getInstance().getArena().capturePointBlocks.contains(block.getLocation().toVector())) {
            TempleLockout.getInstance().getArena().zoneStatus.replace(new Pair<>(block.getX(), block.getZ()), NamedTextColor.WHITE);
            return;
        }
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent ev) {
        Block block = ev.getBlock();
        Player player = ev.getPlayer();
        if (!TempleLockout.getInstance().getArena().capturePointBlocks.contains(block.getLocation().toVector())) {
            TempleLockout.getInstance().getArena().zoneStatus.replace(new Pair<>(block.getX(), block.getZ()), PlayerUtil.getPlayerTeamColor(player));
            return;
        }
        ev.setCancelled(true);
    }

}
