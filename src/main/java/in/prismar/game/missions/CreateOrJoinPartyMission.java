package in.prismar.game.missions;

import in.prismar.api.PrismarinApi;
import in.prismar.api.mission.AbstractMission;
import in.prismar.api.mission.MissionType;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CreateOrJoinPartyMission extends AbstractMission {
    public CreateOrJoinPartyMission() {
        super("create-or-join-party", MissionType.SEASONAL, 1, Material.ARMOR_STAND, "§dTeam up");
    }

    @Override
    public String getDescription(int stage) {
        return "§dCreate or join a party";
    }

    @Override
    public String[] getRewards() {
        return new String[]{
                "§a+2.000$"
        };
    }

    @Override
    public void onReceiveReward(Player player) {
        UserProvider<User> userProvider = PrismarinApi.getProvider(UserProvider.class);
        User user = userProvider.getUserByUUID(player.getUniqueId());
        user.getSeasonData().setBalance(user.getSeasonData().getBalance() + 2000);
    }

    @Override
    public long getMaxProgress(int stage) {
        return 1;
    }
}
