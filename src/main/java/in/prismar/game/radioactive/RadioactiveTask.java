package in.prismar.game.radioactive;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.region.RegionProvider;
import in.prismar.game.Game;
import in.prismar.game.item.impl.armor.misc.GasMaskHelmet;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.meta.anno.Service;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Service
public class RadioactiveTask implements Runnable {

    private final Game game;
    private final RegionProvider regionProvider;
    private final ConfigStore configStore;

    public RadioactiveTask(Game game) {
        this.game = game;
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
        this.regionProvider = PrismarinApi.getProvider(RegionProvider.class);
        Bukkit.getScheduler().runTaskTimerAsynchronously(game, this, 20, 20);
    }

    @Override
    public void run() {
        final double damage = Double.valueOf(configStore.getProperty("radioactive.damage"));
        final int gasMaskDamage = Integer.valueOf(configStore.getProperty("radioactive.damage.gasmask"));
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(regionProvider.isInRegionWithFlag(player.getLocation(), "radioactive")) {
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
                        player.sendTitle("§2Radioactive", "§2Zone", 5, 20, 5);
                    });
                }

            }
        }
    }
}
