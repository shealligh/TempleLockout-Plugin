package slworks.templeLockout.util;

import org.bukkit.entity.Player;
import org.bukkit.map.*;
import slworks.synlinkGames.API.util.Pair;
import slworks.templeLockout.TempleLockout;

import java.awt.Color;
import java.util.Map;

public class CustomMapRenderer extends MapRenderer{

    // private Player owner;
    MapCursor self = new MapCursor((byte) 0, (byte) 0, (byte) 8, MapCursor.Type.PLAYER, true);
    MapCursorCollection cursors = new MapCursorCollection();

    // public CustomMapRenderer(Player owner) {
    public CustomMapRenderer() {
        // view一样也可以在不同的玩家间不同的canvas上作画
        super(true);
        // this.owner = owner;
        cursors.addCursor(self);
    }

    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        // 一个房间大小方块对应两个像素
        // if (!player.equals(owner)) {
        //     return;
        // }
        // view.setCenterX(player.getLocation().blockX());
        // view.setCenterZ(player.getLocation().blockZ());
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
                Color color = TempleLockout.getInstance().getArena().pixelStatus.get(new Pair<>(x, z));
                canvas.setPixelColor(i, j, color == null ? Color.BLACK : color);
            }
        }
        canvas.setCursors(cursors);
    }
}
