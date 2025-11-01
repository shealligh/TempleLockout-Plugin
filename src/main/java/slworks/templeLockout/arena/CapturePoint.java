package slworks.templeLockout.arena;

import java.util.Set;

import org.bukkit.util.Vector;

public class CapturePoint {

    private final Set<Vector> blocks;
    private final Vector center;

    public CapturePoint(Vector center) {
        this.center = center;
        this.blocks = Set.of(
                center,
                center.clone().add(new Vector(1, 0, 0)),
                center.clone().add(new Vector(-1, 0, 0)),
                center.clone().add(new Vector(0, 0, 1)),
                center.clone().add(new Vector(0, 0, -1)),
                center.clone().add(new Vector(1, 0, 1)),
                center.clone().add(new Vector(1, 0, -1)),
                center.clone().add(new Vector(-1, 0, 1)),
                center.clone().add(new Vector(-1, 0, -1))
        );
    }

    public Set<Vector> getBlocks() {
        return blocks;
    }

    public Vector getCenter() {
        return center;
    }
}
