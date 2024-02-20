package in.prismar.game.missions;

import in.prismar.api.PrismarinApi;
import in.prismar.api.mission.AbstractMission;
import in.prismar.api.mission.MissionType;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.library.common.math.NumberFormatter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MoneyBasedMission extends AbstractMission {

    private final double amount;
    private final long maxProgress;

    private final String task;

    public MoneyBasedMission(String id, MissionType type, Material icon, String title, String task, long maxProgress, double amount) {
        super(id, type, 1, icon, title);
        this.amount = amount;
        this.maxProgress = maxProgress;
        this.task = task;
    }

    @Override
    public String getDescription(int stage) {
        return task;
    }

    @Override
    public String[] getRewards() {
        return new String[]{
                "§a+"+ NumberFormatter.formatDoubleToThousands(amount) +"$"
        };
    }

    @Override
    public void onReceiveReward(Player player) {
        UserProvider<User> userProvider = PrismarinApi.getProvider(UserProvider.class);
        User user = userProvider.getUserByUUID(player.getUniqueId());
        user.getSeasonData().setBalance(user.getSeasonData().getBalance() + amount);
    }

    @Override
    public long getMaxProgress(int stage) {
        return maxProgress;
    }
}
