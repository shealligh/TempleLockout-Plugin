package slworks.templeLockout.config;

import org.bukkit.plugin.Plugin;

import slworks.synlinkGames.API.config.GameConfigManager;

import java.util.List;

public class TempleLockoutConfigManager extends GameConfigManager {

    private List<Integer> times;

    public TempleLockoutConfigManager(Plugin plugin) {
        super(plugin);
        times.add(getShrinkTime(1));
        times.add
    }

    @Override
    public void setupCustomConfig() {
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
    }

    public int getTotalTime() {

    }

    public int getShrinkTime(int stage) {
        if (stage > 4 || stage < 1) {
            return 0;
        } else if (stage == 4) {
            return config.getInt("game.time.shrinking_final", 0);
        }
        return config.getInt("game.time.shrinking_" + stage, 0);
    }

    public int getNextShrinkTime(int stage) {
        if (stage > 4 || stage < 1) {
            return 0;
        } else if (stage == 4) {
            return config.getInt("game.time.next_shrink_final", 0);
        }
        return config.getInt("game.time.next_shrink_" + stage, 0);
    }

}
