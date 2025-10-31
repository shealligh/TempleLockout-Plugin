package slworks.templeLockout.arena;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import net.kyori.adventure.text.format.NamedTextColor;
import slworks.synlinkGames.API.arena.GameArena;
import slworks.synlinkGames.API.util.Pair;
import slworks.templeLockout.TempleLockout;

public class TempleLockoutArena extends GameArena {

    public Map<Pair<Integer, Integer>, NamedTextColor> zoneStatus;
    public Set<Vector> capturePointBlocks;
    private int roomWidth;
    private int corridorLength;
    private int corridorWidth;

    public TempleLockoutArena(World world) {
        super(world);
        zoneStatus = new HashMap<>();
        capturePointBlocks = new HashSet<>();
        capturePointBlocks = TempleLockout.getInstance().getConfigManager().getCapturePointBlocks();
        roomWidth = TempleLockout.getInstance().getConfigManager().getRoomWidth();
        corridorLength = TempleLockout.getInstance().getConfigManager().getCorridorLength();
        corridorWidth = TempleLockout.getInstance().getConfigManager().getCorridorWidth();

        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                for (int x = -roomWidth/2; x <= roomWidth/2; x++) {
                    for (int z = -roomWidth/2; z <= roomWidth/2; z++) {
                        zoneStatus.put(new Pair<>(x + i * (roomWidth + corridorLength), z + j * (roomWidth + corridorLength)), NamedTextColor.WHITE);
                    }
                }

                // 上方和右方的走廊
                if (i != 3) {
                    for (int x = roomWidth/2 + 1; x <= roomWidth/2 + corridorLength; x++) {
                        for (int z = -corridorWidth/2; z <= corridorWidth/2; z++) {
                            zoneStatus.put(new Pair<>(x + i * (roomWidth + corridorLength) + roomWidth/2 + corridorLength/2, z + j * (roomWidth + corridorLength)), NamedTextColor.WHITE);
                        }
                    }
                }

                if (j != 3) {
                    for (int z = roomWidth/2 + 1; z <= roomWidth/2 + corridorLength; z++) {
                        for (int x = -corridorWidth/2; x <= corridorWidth/2; x++) {
                            zoneStatus.put(new Pair<>(x + i * (roomWidth + corridorLength), z + j * (roomWidth + corridorLength) + roomWidth/2 + corridorLength/2), NamedTextColor.WHITE);
                        }
                    }
                }
            }
        }
    }

    public void updateZoneStatus(int blockX, int blockZ, NamedTextColor newStatus) {
        // 这里的输入是方块的坐标,需要转换为区域坐标
        
        zoneStatus.put(new Pair<>(blockX, blockZ), newStatus);
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

    @Override
    public void initialize() {
        world.setPVP(true);
    }

    @Override
    public void reset() {
        world.setPVP(false);
    }

}
