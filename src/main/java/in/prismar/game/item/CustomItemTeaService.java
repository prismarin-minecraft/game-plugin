package in.prismar.game.item;

import in.prismar.api.PrismarinApi;
import in.prismar.api.item.TeaProvider;
import in.prismar.api.item.TeaType;
import in.prismar.api.meta.Provider;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.item.impl.tea.TeaItem;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.task.TeaTask;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Service
@Getter
public class CustomItemTeaService implements TeaProvider {

    private final Game game;

    @Provider
    private UserProvider<User> userProvider;

    private ScoreboardProvider scoreboardProvider;

    public CustomItemTeaService(Game game) {
        this.game = game;
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
        this.scoreboardProvider = PrismarinApi.getProvider(ScoreboardProvider.class);

        Bukkit.getScheduler().runTaskTimerAsynchronously(game, new TeaTask(this), 40, 40);
    }

    public void load(CustomItemRegistry registry) {
        for(CustomItem item : registry.getItems().values()) {
            if(item instanceof TeaItem tea) {
                tea.buildLore(game);
            }
        }
    }
    @Override
    public int getMultiplier(Player player, TeaType type) {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        final String id = type.name().toLowerCase() + "tea";
        if(user.isTimestampAvailable(id)) {
           return 1;
        }
        final int tier = (int) user.getSeasonData().getAttachments().getOrDefault(id, 1);
        if(tier == 1) {
            return 2;
        }
        if(tier == 2) {
            return Math.random() > 0.5 ? 3 : 2;
        }
        return 3;
    }

    @Override
    public long getDuration(Player player, TeaType type) {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        final String id = type.name().toLowerCase() + "tea";
        if(user.isTimestampAvailable(id)) {
            return -1;
        }
        return user.getTimestamp(id) - System.currentTimeMillis();
    }

    @Override
    public boolean hasAnyTea(Player player) {
        for(TeaType type : TeaType.values()) {
            if(getMultiplier(player, type) >= 2) {
                return true;
            }
        }
        return false;
    }
}
