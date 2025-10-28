package slworks.templeLockout.arena;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Door {
    private boolean isOpen;
    private Set<Block> doorBlocks;
    private int doorHeight;
    private int raised;

    public Door() {
        this.isOpen = false;
        this.raised = 0;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean raiseOneBlock(boolean ifRaise) {
        // Logic to raise the door by one block
        if (ifRaise && raised >= doorHeight) return false;
        if (!ifRaise && raised <= 0) return false;

        raised += ifRaise ? 1 : -1;
        Map<Location, Material> raisedBlocks = new HashMap<>();
        for (Block block : doorBlocks) {
            int dy = ifRaise ? 1 : -1;
            raisedBlocks.put(block.getLocation().add(0, dy, 0), block.getType());
        }

        for (Block block : doorBlocks) {
            if (raisedBlocks.containsKey(block.getLocation())) {
                block.setType(raisedBlocks.get(block.getLocation()));
                raisedBlocks.remove(block.getLocation());
            } else {
                block.setType(Material.AIR);
                doorBlocks.remove(block);
            }
        }

        for (Location loc : raisedBlocks.keySet()) {
            loc.getBlock().setType(raisedBlocks.get(loc));
            doorBlocks.add(loc.getBlock());
        }

        return true;
    }

    public void open() {
        isOpen = true;
    }

    public void close() {
        isOpen = false;
    }

}
