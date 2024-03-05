package in.prismar.game.party.task;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.party.Party;
import in.prismar.game.party.PartyRegistry;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PartyLimitCheckerTask implements Runnable{

    private final PartyRegistry registry;

    @Override
    public void run() {
        for(Party party : registry.getAll()) {
            if(!registry.isPartyOverLimit(party)) {
                party.setOverLimitAt(0);
                party.setNextStaffNotificationAt(0);
                party.setNextStaffNotificationAt(0);
                continue;
            }
            if(party.getOverLimitAt() == 0) {
                party.setOverLimitAt(System.currentTimeMillis() + getPartyOverLimitTime());
            }
            if(System.currentTimeMillis() >= party.getOverLimitAt()) {
                if(party.getNextStaffNotificationAt() == 0) {
                    party.setNextStaffNotificationAt(System.currentTimeMillis() + getPartyNotificationStaffTime());
                }
                if(System.currentTimeMillis() >= party.getNextNotificationAt()) {
                    party.setNextNotificationAt(System.currentTimeMillis() + getPartyNotificationTime());
                    notify(party);
                }
            }
        }
    }

    public void notify(Party party) {
        if(System.currentTimeMillis() >= party.getNextStaffNotificationAt()) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(!player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "party.limit.notify")) {
                    continue;
                }
                player.sendMessage(PrismarinConstants.PREFIX + String.format("§cThe party §e" + party.getOwner().getName() + " §chas a member count that is over the limit. §8(§cMax allowed size§8: §e%d§8)", registry.getAllowedPartySize()));
            }
        }
        final String message = String.format("§cYour party reached the maximum allowed amount of players, please kick someone from your party. §8(§cMax allowed size§8: §e%d§8)", registry.getAllowedPartySize());
        for(Player player : party.getMembers()) {
            player.sendMessage(PrismarinConstants.PREFIX + message);
            player.playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, 0.4f, 1f);
        }
        party.getOwner().sendMessage(PrismarinConstants.PREFIX + message);
        party.getOwner().playSound(party.getOwner().getLocation(), Sound.ENTITY_CAT_HISS, 0.4f, 1f);
    }

    public long getPartyNotificationStaffTime() {
        return registry.getConfigStore().getLongPropertyOrDefault("party.limit.staff.notification.time", 30) * 1000;
    }

    public long getPartyNotificationTime() {
        return registry.getConfigStore().getLongPropertyOrDefault("party.limit.notification.time", 30) * 1000;
    }

    public long getPartyOverLimitTime() {
        return registry.getConfigStore().getLongPropertyOrDefault("party.limit.overlimit.time", 30) * 1000;
    }
}
