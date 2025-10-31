package slworks.templeLockout.util;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.Color;

public class CustomMapRenderer extends MapRenderer{

    // private Player owner;

    // public CustomMapRenderer(Player owner) {
    public CustomMapRenderer() {
        // view一样也可以在不同的玩家间不同的canvas上作画
        super(false);
        // this.owner = owner;
    }

    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        // 一个房间大小方块对应两个像素
        // if (!player.equals(owner)) {
        //     return;
        // }
        // view.setCenterX(player.getLocation().blockX());
        // view.setCenterZ(player.getLocation().blockZ());
        // canvas.setPixelColor(0, 0, Color.getColor("FF0000"));
    }
}
