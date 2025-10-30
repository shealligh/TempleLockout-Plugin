package slworks.templeLockout.config;

import org.bukkit.plugin.Plugin;

import slworks.synlinkGames.API.config.GameConfigManager;

public class TempleLockoutConfigManager extends GameConfigManager {

    public TempleLockoutConfigManager(Plugin plugin) {
        super(plugin);
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
        addConfig(configFileName, config);
    }

}
