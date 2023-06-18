package in.prismar.game.unlockable;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.scheduler.Scheduler;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class UnlockableService {

    private static final String UNLOCKING_TEXT = "Unlocked";
    private static final char[] UNLOCKING_TEXT_CHARS = UNLOCKING_TEXT.toCharArray();

    @Getter
    private final UserProvider<User> userProvider;

    @Getter
    private final UnlockableFile file;
    private final Game game;

    public UnlockableService(Game game) {
        this.game = game;
        this.file = new UnlockableFile(game.getDefaultDirectory());
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
    }

    public Unlockable create(String id, String displayName, Location location) {
        Unlockable unlockable = new Unlockable();
        unlockable.setId(id.toLowerCase());
        unlockable.setDisplayName(displayName);
        unlockable.setLocation(location);

        file.getEntity().put(unlockable.getId(), unlockable);
        file.save();
        return unlockable;
    }

    public Unlockable delete(Unlockable unlockable) {
        file.getEntity().remove(unlockable.getId().toLowerCase());
        file.save();
        return unlockable;
    }

    public boolean existsUnlockable(String id) {
        return file.getEntity().containsKey(id.toLowerCase());
    }

    public Unlockable getUnlockable(String id) {
        return file.getEntity().get(id.toLowerCase());
    }

    public boolean unlock(Player player, Unlockable unlockable) {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        if (user.getSeasonData().getAttachments().containsKey("unlock." + unlockable.getId())) {
            return false;
        }
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1f);
        player.playSound(player.getLocation(), "unlock", 0.9f, 1f);
        user.getSeasonData().getAttachments().put("unlock." + unlockable.getId(), System.currentTimeMillis());
        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                if (index >= UNLOCKING_TEXT_CHARS.length) {
                    player.sendTitle("§8- §a" + UNLOCKING_TEXT + " §8-", unlockable.getDisplayName(), 1, 40, 20);
                    cancel();
                    return;
                }
                StringBuilder word = new StringBuilder();
                for (int i = 0; i < index + 1; i++) {
                    word.append(UNLOCKING_TEXT_CHARS[i]);
                }
                index++;
                player.sendTitle("§8- §a" + word + " §8-", "", 1, 20, 1);
            }
        }.runTaskTimer(game, 2, 3);
        return true;
    }

}
