package in.prismar.game.missions.impl;

import in.prismar.api.PrismarinApi;
import in.prismar.api.mission.AbstractMission;
import in.prismar.api.mission.MissionType;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Kill25ZombiesMission extends AbstractMission {
    public Kill25ZombiesMission() {
        super("kill25zombies", MissionType.DAILY, 1, Material.ZOMBIE_HEAD, "§cZombie slayer");
    }

    @Override
    public String getDescription(int stage) {
        return "§cKill 25 Zombies";
    }

    @Override
    public String[] getRewards() {
        return new String[]{
                "§a+10.000$"
        };
    }

    @Override
    public void onReceiveReward(Player player) {
        UserProvider<User> userProvider = PrismarinApi.getProvider(UserProvider.class);
        User user = userProvider.getUserByUUID(player.getUniqueId());
        user.getSeasonData().setBalance(user.getSeasonData().getBalance() + 10000);
    }

    @Override
    public long getMaxProgress(int stage) {
        return 25;
    }
}
