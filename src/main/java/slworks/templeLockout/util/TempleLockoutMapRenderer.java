package slworks.templeLockout.util;

import org.bukkit.entity.Player;
import org.bukkit.map.*;
import slworks.synlinkGames.API.util.Pair;
import slworks.templeLockout.TempleLockout;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class TempleLockoutMapRenderer extends MapRenderer{

    // private Player owner;
    MapCursor self = new MapCursor((byte) 0, (byte) 0, (byte) 8, MapCursor.Type.PLAYER, true);
    MapCursorCollection cursors = new MapCursorCollection();
    private Map<Pair<Integer, Integer>, Color> pixelStatus;

    public TempleLockoutMapRenderer() {
        // view一样也可以在不同的玩家间不同的canvas上作画
        super(true);
        cursors.addCursor(self);
        pixelStatus = new HashMap<>();
    }

    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        // 一个房间大小方块对应1.5个像素
        float angle = player.getLocation().getYaw();
        int pX = player.getLocation().getBlockX();
        int pZ = player.getLocation().getBlockZ();
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                double i1 = -((i - 64)/1.5);
                double j1 = -((j - 64)/1.5);
                int i2 = (int) Math.round(i1 * Math.cos(Math.toRadians(angle)) - j1 * Math.sin(Math.toRadians(angle)));
                int j2 = (int) Math.round(i1 * Math.sin(Math.toRadians(angle)) + j1 * Math.cos(Math.toRadians(angle)));
                int x = i2 + pX;
                int z = j2 + pZ;
                Color color = pixelStatus.get(new Pair<>(x, z));
                canvas.setPixelColor(i, j, color == null ? new Color(54, 54, 54) : color);
            }
        }
        canvas.setCursors(cursors);
    }

    public Map<Pair<Integer, Integer>, Color> getPixelStatus() {
        return pixelStatus;
    }
}
