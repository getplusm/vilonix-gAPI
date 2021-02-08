package plusm.vilonix.gapi.game.perk;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.effect.ParticleEffect;
import plusm.vilonix.api.event.game.PlayerKillEvent;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.MiniGameType;
import plusm.vilonix.api.game.TeamManager;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.player.constans.PurchaseType;
import plusm.vilonix.gapi.game.perk.type.bowbreakblock.ArrowBlockBreakListener;
import plusm.vilonix.gapi.game.perk.type.vampirism.VampListener;
import plusm.vilonix.gapi.game.team.SelectionTeam;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.gapi.utils.core.PlayerUtil;
import plusm.vilonix.libraries.LibAPI;
import plusm.vilonix.libraries.interfaces.NmsManager;

public class VanillaPerkListener extends DListener {

    public static void clearListeners() {
        VampListener.getPlayerVampirism().clear();
        ArrowBlockBreakListener.getPlayerArrowPerk().clear();
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onKill(PlayerKillEvent e) {
        if (!(e.getKiller() instanceof Player))
            return;
        Player killer = (Player) e.getKiller();
        if (VampListener.getPlayerVampirism().containsKey(killer.getName())) {
            double health = killer.getHealth();
            double n = health + (double) VampListener.getPlayerVampirism().get(killer.getName());
            if (n > killer.getMaxHealth()) {
                killer.setHealth(killer.getMaxHealth());
            } else
                killer.setHealth(n);
            killer.sendMessage("§aЗдоровье восполнено!");
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Arrow) {
            Arrow arrow = (Arrow) entity;
            World world = arrow.getWorld();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                VilonixNetwork.getTitlesAPI().sendTitle(shooter, "§c-" + event.getHitEntity().getLastDamageCause().getDamage() + "♥");
                if (!ArrowBlockBreakListener.getPlayerArrowPerk().contains(shooter.getName()))
                    return;
                BlockIterator bi = new BlockIterator(world, arrow.getLocation().toVector(),
                        arrow.getVelocity().normalize(), 0, 4);
                Block hit = null;

                while (bi.hasNext()) {
                    hit = bi.next();
                    if (hit.getType() != Material.AIR && hit.getType().isBlock() && hit.getType().isSolid()) {
                        break;
                    }
                }

                assert hit != null;
                if (hit.getType() == Material.CHEST)
                    return;
                if (hit.getType() == Material.BED || hit.getType() == Material.BED_BLOCK)
                    return;
                if (hit.getType() == Material.TRAPPED_CHEST)
                    return;
                if (hit.getType() == Material.LAVA)
                    return;
                if (hit.getType() == Material.WATER)
                    return;
                if (hit.getType() == Material.OBSIDIAN)
                    return;

                final Location loc = hit.getLocation();
                for (Player nearby : PlayerUtil.getNearbyPlayers(hit.getLocation(), 10)) {
                    if (nearby.getLocation().getWorld().equals(loc.getWorld()))
                        nearby.playSound(nearby.getLocation(), ArrowBlockBreakListener.PlaySound(hit), 10.0f, 1.0f);
                }
                arrow.remove();
                hit.breakNaturally();
                VilonixNetwork.getParticleAPI().sendEffect(ParticleEffect.SMOKE_LARGE, loc, 0.005F, 11);
                hit.setType(Material.AIR);
            }
        }
    }
}
