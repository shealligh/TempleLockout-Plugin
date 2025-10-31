package slworks.templeLockout.config;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import slworks.synlinkGames.API.config.GameConfigManager;
import slworks.templeLockout.TempleLockout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TempleLockoutConfigManager extends GameConfigManager {

    private List<Integer> shrinkingTimes;
    private List<Integer> nextShrinkTimes;

    public TempleLockoutConfigManager(Plugin plugin) {
        super(plugin);
        setShrinkingTimes();
        setNextShrinkTimes();
    }

    @Override
    public void setupCustomConfig() {
        // pandoras box defaults

        // 下次缩圈 45秒
        // 正在缩圈 45秒 第一次缩圈
        // 下次缩圈 20秒
        // 正在缩圈 45秒 第二次缩圈
        // 下次缩圈 20秒
        // 正在缩圈 45秒 第三次缩圈 将缩至只有一个中心圈
        // 下次缩圈 12秒
        // 最终决战
        addConfig("game.time.next_shrink", List.of(45, 20, 20, 12));
        addConfig("game.time.shrinking", List.of(45, 45, 45, 10));

        addConfig("arena.room_width", 10+10+3); // 不包括房间的墙壁大小,也就是内部可以走动的范围是23*23
        addConfig("arena.corridor_length", 9); // 和上面的房间宽度没有重合的,也就是可以直接23+9+...
        addConfig("arena.corridor_width", 5);
        addConfig("arena.layers", 4);

        addConfig("arena.center", new Vector(0, 100, 0));
    }

    public int getTotalTime() {
        int total = 0;
        for (int t : shrinkingTimes) {
            total += t;
        }
        for (int t : nextShrinkTimes) {
            total += t;
        }
        return total;
    }

    public int getTotalWidth() {
        return getRoomWidth() * 7 + getCorridorLength() * 6;
    }

    private void setShrinkingTimes() {
        List<Integer> tmp = config.getIntegerList("game.time.shrinking");
        if (getLayers() < 4) {
            for (int i = 0; i < 4 - getLayers(); i++) {
                tmp.remove(i + 1);
            }
        }
        shrinkingTimes = tmp;
    }

    public List<Integer> getShrinkingTimes() {
        return shrinkingTimes;
    }

    private void setNextShrinkTimes() {
        List<Integer> tmp = config.getIntegerList("game.time.next_shrink");
        if (getLayers() < 4) {
            for (int i = 0; i < 4 - getLayers(); i++) {
                tmp.remove(i + 1);
            }
        }
        nextShrinkTimes = tmp;
    }

    public List<Integer> getNextShrinkTimes() {
        return nextShrinkTimes;
    }

    public int getRoomWidth() {
        return config.getInt("arena.room_width");
    }

    public int getCorridorLength() {
        return config.getInt("arena.corridor_length");
    }

    public int getCorridorWidth() {
        return config.getInt("arena.corridor_width");
    }

    public Location getArenaCenter() {
        Vector vec = config.getVector("arena.center");
        return vec.toLocation(TempleLockout.getInstance().getArena().getWorld());
    }

    public int getLayers() {
        return config.getInt("arena.layers");
    }
}
