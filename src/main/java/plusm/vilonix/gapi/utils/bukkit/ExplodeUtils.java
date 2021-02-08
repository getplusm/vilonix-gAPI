package plusm.vilonix.gapi.utils.bukkit;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.List;

@Getter
public class ExplodeUtils {
    private final List<Block> blocklist;
    private final boolean can_skip_Blacklist;
    private final boolean ceiling_Blocks_Collapsing;
    private final boolean fly_Blocks;
    private final List<String> listMaterials;

    public ExplodeUtils(List<Block> explode, boolean fly_Blocks, boolean ceiling_Blocks_Collapsing,
                        boolean can_skip_Blacklist, List<String> listMaterials) {
        this.blocklist = explode;
        this.fly_Blocks = fly_Blocks;
        this.ceiling_Blocks_Collapsing = ceiling_Blocks_Collapsing;
        this.can_skip_Blacklist = can_skip_Blacklist;
        this.listMaterials = listMaterials;
    }

    private boolean canUseBlock(Block b) {
        boolean canUse = true;

        for (String m : this.listMaterials) {
            if (b.getType().toString().equalsIgnoreCase(m)) {
                canUse = false;
            }
        }

        return canUse;
    }

    private void destroy(Block b) {
        for (Location l = b.getLocation().clone(); l.getBlockY() < 255; l.setY((double) (l.getBlockY() + 1))) {
            if (!l.getBlock().getType().equals(Material.AIR)) {
                if (this.canUseBlock(l.getBlock())) {
                    @SuppressWarnings("deprecation")
                    FallingBlock falling = l.getWorld().spawnFallingBlock(l, l.getBlock().getType(),
                            l.getBlock().getData());
                    falling.setDropItem(false);
                    l.getBlock().setType(Material.AIR);
                } else if (!this.can_skip_Blacklist) {
                    return;
                }
            }
        }

    }

    public void run() {
        for (Block b : this.blocklist) {
            if (!b.getType().equals(Material.AIR) && this.fly_Blocks && this.canUseBlock(b)) {
                Vector direction = new Vector();
                direction.setX(0.0D + Math.random() - Math.random());
                direction.setY(Math.random());
                direction.setZ(0.0D + Math.random() - Math.random());
                @SuppressWarnings("deprecation")
                FallingBlock falling = b.getLocation().getWorld().spawnFallingBlock(b.getLocation(), b.getType(),
                        b.getData());
                b.setType(Material.AIR);
                falling.setDropItem(false);
                falling.setVelocity(direction);
            }

            if (this.ceiling_Blocks_Collapsing) {
                this.destroy(b);
            }
        }
    }
}