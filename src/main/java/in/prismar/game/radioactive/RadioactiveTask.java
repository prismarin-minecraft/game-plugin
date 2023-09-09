package in.prismar.game.radioactive;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.region.RegionProvider;
import in.prismar.game.Game;
import in.prismar.game.item.impl.armor.misc.GasMaskHelmet;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.meta.anno.Service;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class RadioactiveTask implements Runnable {

    private final Game game;
    private final RegionProvider regionProvider;
    private final ConfigStore configStore;

    private Map<UUID, Integer> sounds;

    public RadioactiveTask(Game game) {
        this.game = game;
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
        this.regionProvider = PrismarinApi.getProvider(RegionProvider.class);
        this.sounds = new HashMap<>();
        Bukkit.getScheduler().runTaskTimerAsynchronously(game, this, 20, 20);
    }

    @Override
    public void run() {
        final double damage = Double.valueOf(configStore.getProperty("radioactive.damage"));
        final int gasMaskDamage = Integer.valueOf(configStore.getProperty("radioactive.damage.gasmask"));
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(regionProvider.isInRegionWithFlag(player.getLocation(), "radioactive")) {
                int currentSeconds = sounds.getOrDefault(player.getUniqueId(), 0);
                if(currentSeconds == 0) {
                    player.playSound(player.getLocation(), "radioactive", SoundCategory.AMBIENT, 0.25f, 1f);
                }
                currentSeconds++;
                if(currentSeconds >= 24) {
                    currentSeconds = 0;
                }
                sounds.put(player.getUniqueId(), currentSeconds);
                if(player.getInventory().getHelmet() != null) {
                    ItemStack helmet = player.getInventory().getHelmet();
                    CustomItem item = game.getItemRegistry().getItemByStack(helmet);
                    if(item != null) {
                        if(item instanceof GasMaskHelmet gasMaskHelmet) {
                            Bukkit.getScheduler().runTask(game, () -> {
                                if(gasMaskHelmet.damage(game, helmet, gasMaskDamage)) {
                                    player.getInventory().setHelmet(new ItemStack(Material.AIR));
                                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.5f, 1f);
                                }
                            });

                            continue;
                        }
                    }
                }
                if(player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
                    Bukkit.getScheduler().runTask(game, () -> {
                        player.damage(damage);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 1));
                        player.sendTitle("§2Radioactive Zone", "§7Put on your gas mask", 5, 20, 5);
                    });
                }
            } else {
                sounds.remove(player.getUniqueId());
                player.stopSound("radioactive", SoundCategory.AMBIENT);
            }
        }
    }
}
