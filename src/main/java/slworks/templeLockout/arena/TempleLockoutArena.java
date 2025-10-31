package slworks.templeLockout.arena;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import slworks.synlinkGames.API.arena.GameArena;
import slworks.synlinkGames.API.util.Pair;
import slworks.templeLockout.TempleLockout;
import slworks.templeLockout.util.TempleLockoutMapRenderer;

public class TempleLockoutArena extends GameArena {

    public Set<Vector> capturePointBlocks;
    private int roomWidth;
    private int corridorLength;
    private int corridorWidth;
    private int totalWidth;
    private TempleLockoutMapRenderer renderer = new TempleLockoutMapRenderer();

//    private Color corridorColor = new Color(188, 143, 143);
    private final Color corridorColor = new Color(236, 121, 147);
    private final Color innerRoomColor = new Color(57, 197, 187); // "39C5BB"
    private final Color secondRoomColor = Color.YELLOW;
    private final Color thirdRoomColor = new Color(255, 153, 51);
    private final Color outerRoomColor = new Color(176, 32, 39); // "B02027"

    public TempleLockoutArena(World world) {
        super(world);
        capturePointBlocks = new HashSet<>();
        totalWidth = TempleLockout.getInstance().getConfigManager().getTotalWidth();
        roomWidth = TempleLockout.getInstance().getConfigManager().getRoomWidth();
        corridorLength = TempleLockout.getInstance().getConfigManager().getCorridorLength();
        corridorWidth = TempleLockout.getInstance().getConfigManager().getCorridorWidth();
    }

    public void updatePixelStatus(int x, int y, Color newStatus) {
        renderer.getPixelStatus().put(Pair.of(x, y), newStatus);
    }

    public void updateZoneColor(Vector vec, Color newColor) {
        //TODO: 增加robust
        int roomPlusCorridor = roomWidth + corridorLength;
        int roomX = Math.round((float) vec.getBlockX() /roomPlusCorridor);
        int roomZ = Math.round((float) vec.getBlockZ() /roomPlusCorridor);
        int centerX = roomX * roomPlusCorridor;
        int centerZ = roomZ * roomPlusCorridor;
        int dirX = vec.getBlockX() - centerX;
        int dirZ = vec.getBlockZ() - centerZ;
        int minX, maxX, minZ, maxZ;
        int roomWidth6 = Math.round((float) roomWidth /6);
        int roomWidth2 = roomWidth /2;
        if (dirZ ==0) {
            minZ = -roomWidth6;
            maxZ = roomWidth6;
        } else {
            minZ = dirZ > 0 ? roomWidth6 : -roomWidth2;
            maxZ = dirZ > 0 ? roomWidth2 : -roomWidth6;
        }
        if (dirX ==0) {
            minX = -roomWidth6;
            maxX = roomWidth6;
        } else {
            minX = dirX > 0 ? roomWidth6 : -roomWidth2;
            maxX = dirX > 0 ? roomWidth2 : -roomWidth6;
        }
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                updatePixelStatus(x + centerX, z + centerZ, newColor);
            }
        }
    }

    public TempleLockoutMapRenderer getRenderer() {
        return renderer;
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
                    Block block = world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
                    if (block.getType().equals(Material.BEDROCK)) {
                        tracingBlocks.add(vec);
                        block.setType(Material.CALCITE);
                    }
                }
            }
        }
        return tracingBlocks;
    }

    @Override
    public void initialize() {
        world.setPVP(true);
        Vector coord_1 = new Vector(-totalWidth/2, 50, -totalWidth/2);
        Vector coord_2 = new Vector(totalWidth/2, 150, totalWidth/2);
        capturePointBlocks = registerCapturePointBlocks(coord_1, coord_2);

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
                            updatePixelStatus(x + i * (roomWidth + corridorLength), z + j * (roomWidth + corridorLength), corridorColor);
                        }
                    }
                }

                if (j != 3) {
                    for (int z = roomWidth/2 + 1; z <= roomWidth/2 + corridorLength; z++) {
                        for (int x = -corridorWidth/2; x <= corridorWidth/2; x++) {
                            updatePixelStatus(x + i * (roomWidth + corridorLength), z + j * (roomWidth + corridorLength), corridorColor);
                        }
                    }
                }

                // 房间周围的颜色圈
                Color roomColor;
                if (i == 0 && j == 0) {
                    roomColor = innerRoomColor;
                } else if ((Math.abs(i) == 1 && Math.abs(j) <= 1) || (Math.abs(j) == 1 && Math.abs(i) <= 1)) {
                    roomColor = secondRoomColor;
                } else if ((Math.abs(i) == 2 && Math.abs(j) <= 2) || (Math.abs(j) == 2 && Math.abs(i) <= 2)) {
                    roomColor = thirdRoomColor;
                } else {
                    roomColor = outerRoomColor;
                }

                for (int x = -roomWidth/2 - 2; x <= roomWidth/2 + 2; x++) {
                    updatePixelStatus(x + i * (roomWidth + corridorLength), roomWidth/2 + 1 + j * (roomWidth + corridorLength), roomColor);
                    updatePixelStatus(x + i * (roomWidth + corridorLength), roomWidth/2 + 2 + j * (roomWidth + corridorLength), roomColor);
                    updatePixelStatus(x + i * (roomWidth + corridorLength), -roomWidth/2 - 1 + j * (roomWidth + corridorLength), roomColor);
                    updatePixelStatus(x + i * (roomWidth + corridorLength), -roomWidth/2 - 2 + j * (roomWidth + corridorLength), roomColor);
                }

                for (int z = -roomWidth/2; z <= roomWidth/2; z++) {
                    updatePixelStatus(roomWidth/2 + 1 + i * (roomWidth + corridorLength), z + j * (roomWidth + corridorLength), roomColor);
                    updatePixelStatus(roomWidth/2 + 2 + i * (roomWidth + corridorLength), z + j * (roomWidth + corridorLength), roomColor);
                    updatePixelStatus(-roomWidth/2 - 1 + i * (roomWidth + corridorLength), z + j * (roomWidth + corridorLength), roomColor);
                    updatePixelStatus(-roomWidth/2 - 2 + i * (roomWidth + corridorLength), z + j * (roomWidth + corridorLength), roomColor);
                }
            }
        }
    }

    @Override
    public void reset() {
        world.setPVP(false);
        for (Vector vec : capturePointBlocks) {
            world.getBlockAt(new Location(world, vec.getBlockX(), vec.getBlockY(), vec.getBlockZ())).setType(Material.BEDROCK);
        }
    }
}
