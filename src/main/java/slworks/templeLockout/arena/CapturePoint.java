package slworks.templeLockout.arena;

import java.util.Set;

import org.bukkit.util.Vector;

public class CapturePoint {

    private Set<Vector> blocks;

    public CapturePoint(Set<Vector> blocks) {
        this.blocks = blocks;
    }

    public Set<Vector> getBlocks() {
        return blocks;
    }
}
