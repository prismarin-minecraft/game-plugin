package in.prismar.game.bounty.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.game.bounty.BountyService;
import in.prismar.game.bounty.model.Bounty;
import in.prismar.game.bounty.model.BountySupplier;
import in.prismar.library.common.math.NumberFormatter;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Optional;
import java.util.UUID;

@AutoListener
public class PlayerDeathListener implements Listener {

    @Inject
    private BountyService service;

    @EventHandler
    public void onCall(PlayerDeathEvent event) {
        if(event.getEntity().getKiller() != null) {
            Player entity = event.getEntity();
            Player killer = event.getEntity().getKiller();
            Optional<Bounty> optional = service.getBounty(event.getEntity());
            if(optional.isPresent()) {
                Bounty bounty = optional.get();
                final String arrow = PrismarinConstants.ARROW_RIGHT + " §7";
                Bukkit.broadcastMessage(PrismarinConstants.BORDER);
                Bukkit.broadcastMessage("§c");
                Bukkit.broadcastMessage(arrow + "§c" + entity.getPlayer().getName() + "'s §7bounty of §6" + NumberFormatter.formatDoubleToThousands(bounty.getMoney()) + "$");
                Bukkit.broadcastMessage(arrow + "has been claimed by §c" + killer.getName());
                Bukkit.broadcastMessage(arrow + "The ranking of this bounty was §c#" + service.getRank(bounty));
                Bukkit.broadcastMessage("§c");
                Bukkit.broadcastMessage(PrismarinConstants.BORDER);

                User user = service.getUserProvider().getUserByUUID(killer.getUniqueId());
                user.getSeasonData().setBalance(user.getSeasonData().getBalance() + bounty.getMoney());
                service.getUserProvider().saveAsync(user, true);
                killer.playSound(killer.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_1, 0.8f, 1f);
                for(BountySupplier supplier : bounty.getSuppliers()) {
                    Player supplierPlayer = Bukkit.getPlayer(UUID.fromString(supplier.getUuid()));
                    if(supplierPlayer != null) {
                        supplierPlayer.playSound(supplierPlayer.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 0.8f, 1f);
                    }
                }
                service.getRepository().deleteAsync(bounty);
            }
        }
    }
}
