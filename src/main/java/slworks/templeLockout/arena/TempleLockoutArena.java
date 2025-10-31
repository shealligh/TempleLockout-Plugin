package slworks.templeLockout.arena;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import slworks.synlinkGames.API.arena.GameArena;
import slworks.synlinkGames.API.util.Pair;
import slworks.templeLockout.TempleLockout;

public class TempleLockoutArena extends GameArena {

    public Map<Pair<Integer, Integer>, Color> pixelStatus;
    public Set<Vector> capturePointBlocks;
    private int roomWidth;
    private int corridorLength;
    private int corridorWidth;
    private int totalWidth;

    public TempleLockoutArena(World world) {
        super(world);
        pixelStatus = new HashMap<>();
        capturePointBlocks = new HashSet<>();
        totalWidth = TempleLockout.getInstance().getConfigManager().getTotalWidth();
        Vector coord_1 = new Vector(-totalWidth/2, 50, -totalWidth/2);
        Vector coord_2 = new Vector(totalWidth/2, 150, totalWidth/2);
        capturePointBlocks = registerCapturePointBlocks(coord_1, coord_2);
        roomWidth = TempleLockout.getInstance().getConfigManager().getRoomWidth();
        corridorLength = TempleLockout.getInstance().getConfigManager().getCorridorLength();
        corridorWidth = TempleLockout.getInstance().getConfigManager().getCorridorWidth();

        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                for (int x = -roomWidth/2; x <= roomWidth/2; x++) {
                    for (int z = -roomWidth/2; z <= roomWidth/2; z++) {
                        updatePixelStatus(x + i * (roomWidth + corridorLength), z + j * (roomWidth + corridorLength), Color.WHITE);
                    }
                }

                // 上方和右方的走廊
                if (i != 3) {
                    for (int x = roomWidth/2 + 1; x <= roomWidth/2 + corridorLength; x++) {
                        for (int z = -corridorWidth/2; z <= corridorWidth/2; z++) {
                            updatePixelStatus(x + i * (roomWidth + corridorLength), z + j * (roomWidth + corridorLength), Color.MAGENTA);
                        }
                    }
                }

                if (j != 3) {
                    for (int z = roomWidth/2 + 1; z <= roomWidth/2 + corridorLength; z++) {
                        for (int x = -corridorWidth/2; x <= corridorWidth/2; x++) {
                            updatePixelStatus(x + i * (roomWidth + corridorLength), z + j * (roomWidth + corridorLength), Color.MAGENTA);
                        }
                    }
                }
            }
        }
    }

    public void updatePixelStatus(Location loc, Color newStatus) {
        // 这里的输入是方块的坐标,需要转换为区域坐标
        for (Pair<Integer, Integer> pair : toPixel(loc.getBlockX(), loc.getBlockY())) {
            pixelStatus.put(pair, newStatus);
        }
    }

    public void updatePixelStatus(int x, int y, Color newStatus) {
        for (Pair<Integer, Integer> pair : toPixel(x, y)) {
            pixelStatus.put(pair, newStatus);
        }
    }

    public void teleportPlayersToSpawns() {

    }

    // 只能通过命令调用!
    public Set<Vector> registerCapturePointBlocks(Vector coord_1, Vector coord_2) {
        Set<Vector> tracingBlocks = new HashSet<>();
        int minX = Math.min(coord_1.getBlockX(), coord_2.getBlockX());
        int maxX = Math.max(coord_1.getBlockX(), coord_2.getBlockX());
        int minY = Math.min(coord_1.getBlockY(), coord_2.getBlockY());
        int maxY = Math.max(coord_1.getBlockY(), coord_2.getBlockY());
        int minZ = Math.min(coord_1.getBlockZ(), coord_2.getBlockZ());
        int maxZ = Math.max(coord_1.getBlockZ(), coord_2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Vector vec = new Vector(x, y, z);
                    Material loc = world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getType();
                    if (loc.equals(Material.BEDROCK)) {
                        tracingBlocks.add(vec);
                    }
                }
            }
        }
        return tracingBlocks;
    }

    public Set<Pair<Integer, Integer>> toPixel(int x, int y) {
        Set<Pair<Integer, Integer>> pixels = new HashSet<>();
//        pixels.add(new Pair<>(x*2, y*2));
//        pixels.add(new Pair<>(x*2 + 1, y*2));
//        pixels.add(new Pair<>(x*2, y*2 + 1));
//        pixels.add(new Pair<>(x*2 + 1, y*2 + 1));
        pixels.add(new Pair<>(x, y));
        return pixels;
    }

    @Override
    public void initialize() {
        world.setPVP(true);
    }

    @Override
    public void reset() {
        world.setPVP(false);
    }

}
