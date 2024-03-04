package in.prismar.game.warzone.combatlog.npc;

import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.BiConsumer;

@Getter
public class TemporaryNpc {

    private final Plugin plugin;
    private NPC.Global npc;

    private long createdAt;

    private final String text;

    private final Player player;

    private double health;

    public TemporaryNpc(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.health = player.getHealth();
        this.text = "§e" + player.getName() + "\\n§cLogged off §8(§c%s§8)";
        this.npc = NPCLib.getInstance().generateGlobalNPC(plugin, player.getUniqueId().toString(), findLocationBased(player));
        this.npc.lookAt(player.getLocation().getYaw(), player.getLocation().getPitch());
        this.npc.setSkin(player);
        this.npc.setText(text);
        if(player.getInventory().getHelmet() != null) {
            this.npc.setHelmet(player.getInventory().getHelmet());
        }
        if(player.getInventory().getChestplate() != null) {
            this.npc.setChestplate(player.getInventory().getChestplate());
        }
        if(player.getInventory().getLeggings() != null) {
            this.npc.setLeggings(player.getInventory().getLeggings());
        }
        if(player.getInventory().getBoots() != null) {
            this.npc.setBoots(player.getInventory().getBoots());
        }
        if(player.getInventory().getItemInMainHand() != null) {
            this.npc.setItemInMainHand(player.getInventory().getItemInMainHand());
        }
        if(player.getInventory().getItemInOffHand() != null) {
            this.npc.setItemInOffHand(player.getInventory().getItemInOffHand());
        }
        this.createdAt = System.currentTimeMillis();
        npc.addCustomClickAction(NPC.Interact.ClickType.LEFT_CLICK, (npc, player1) -> {
            TemporaryNpcService.getInstance().damage(this, 5);
            npc.getLocation().getWorld().playSound(npc.getLocation(), Sound.ENTITY_PLAYER_HURT, 0.8F, 1F);
        });
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            this.npc.forceUpdate();
        }, 10L);

    }

    public void updateText(String text) {
        npc.setText(text);
        npc.forceUpdateText();
    }

    private Location findLocationBased(Player player) {
        final int checkDown = 20;
        Location start = player.getLocation().clone().add(0, 1, 0).getBlock().getLocation();
        Location location = start.clone();
        for (int i = 0; i < checkDown; i++) {
            location = start.clone().subtract(0, i, 0);
            if (location.getBlock().getType().isSolid() && location.getBlock().getType() != Material.BARRIER) {
                break;
            }
        }
        return location.add(0, 1, 0);
    }

    public boolean damage(double damage) {
        this.health -= damage;
        npc.playAnimation(NPC.Animation.TAKE_DAMAGE);
        if(this.health <= 0) {
            return true;
        }
        return false;
    }

    public void despawn() {
        NPCLib.getInstance().removeNPC(npc);
    }

    public Location getEyeLocation() {
        return this.npc.getEyeLocation();
    }
    public Location getLocation() {
        return this.npc.getLocation();
    }
}
