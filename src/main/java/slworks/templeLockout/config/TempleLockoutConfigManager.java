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

    private List<Integer> times;

    public TempleLockoutConfigManager(Plugin plugin) {
        super(plugin);
        times = new ArrayList<>();
        times.add(getShrinkTime(1));
        times.add(getNextShrinkTime(1));
        times.add(getShrinkTime(2));
        times.add(getNextShrinkTime(2));
        times.add(getShrinkTime(3));
        times.add(getNextShrinkTime(3));
        times.add(getShrinkTime(4));
        times.add(getNextShrinkTime(4));
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
        addConfig("game.time.next_shrink_1", 45);
        addConfig("game.time.shrinking_1", 45);
        addConfig("game.time.next_shrink_2", 20);
        addConfig("game.time.shrinking_2", 45);
        addConfig("game.time.next_shrink_3", 20);
        addConfig("game.time.shrinking_3", 45);
        addConfig("game.time.next_shrink_final", 12);
        addConfig("game.time.shrinking_final", 10);

        addConfig("arena.room_width", 10+10+3); // 不包括房间的墙壁大小,也就是内部可以走动的范围是23*23
        addConfig("arena.corridor_length", 9); // 和上面的房间宽度没有重合的,也就是可以直接23+9+...
        addConfig("arena.corridor_width", 5);

        addConfig("arena.center", new Vector(0, 100, 0));
    }

    public int getTotalTime() {
        int total = 0;
        for (int t : times) {
            total += t;
        }
        return total;
    }

    public int getTotalWidth() {
        return getRoomWidth() * 7 + getCorridorLength() * 6;
    }

    public int getShrinkTime(int stage) {
        if (stage > 4 || stage < 1) {
            return 0;
        } else if (stage == 4) {
            return config.getInt("game.time.shrinking_final");
        }
        return config.getInt("game.time.shrinking_" + stage);
    }

    public int getNextShrinkTime(int stage) {
        if (stage > 4 || stage < 1) {
            return 0;
        } else if (stage == 4) {
            return config.getInt("game.time.next_shrink_final");
        }
        return config.getInt("game.time.next_shrink_" + stage);
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

//    public Set<Vector> getCapturePointBlocks() {
//    [21:39:58 ERROR]: Error occurred while enabling TempleLockout v0.0.1 (Is it up to date?)
//            java.lang.ClassCastException: class org.bukkit.configuration.MemorySection cannot be cast to class java.util.Set (org.bukkit.configuration.MemoryS
//                    ection is in unnamed module of loader java.net.URLClassLoader @484b61fc; java.util.Set is in module java.base of loader 'bootstrap')
//            at TempleLockout-0.0.1-1761917997909.jar/slworks.templeLockout.config.TempleLockoutConfigManager.getCapturePointBlocks(TempleLockoutConfig
//                    Manager.java:107) ~[TempleLockout-0.0.1-1761917997909.jar:?]
//        放弃用config yml存这些点的位置
//        return (Set<Vector>) config.get("arena.capturepoints");
//    }

//    public void registerCapturePointBlocks() {
//        Vector coord_1 = new Vector(-getTotalWidth()/2, 50, -getTotalWidth()/2);
//        Vector coord_2 = new Vector(getTotalWidth()/2, 150, getTotalWidth()/2);
//        config.set("arena.capturepoints", TempleLockout.getInstance().getArena().registerCapturePointBlocks(coord_1, coord_2));
//        saveConfig();
//    }
}
